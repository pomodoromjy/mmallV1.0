package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerReponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.invoke.empty.Empty;

import java.util.UUID;

/**
 * Created by Administrator on 2019/11/5 0005.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerReponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0){
            return ServerReponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerReponse.createByErrorMessage("密码错误");
        }

        user.setPassword(StringUtils.EMPTY);
        return ServerReponse.createBySuccess("登录成功",user);

    }

    public ServerReponse<String> register(User user){
        //校验用户名
        ServerReponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        //校验email
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOM);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerReponse.createByErrorMessage("注册失败");
        }
        return ServerReponse.createBySuccessMessage("注册成功");
    }

    public ServerReponse<String> checkValid(String str,String type){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0){
                    return ServerReponse.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServerReponse.createByErrorMessage("email已存在");
                }
            }
        }else{
            ServerReponse.createByErrorMessage("参数错误");
        }
        return ServerReponse.createBySuccessMessage("校验成功");
    }

    public ServerReponse selectQuestion(String username){
        ServerReponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerReponse.createByErrorMessage("用户不存在");
        }

        String question = userMapper.selectQuestionByUsername(username);
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
            return ServerReponse.createBySuccess(question);
        }
        return ServerReponse.createByErrorMessage("找回密码的问题是空的");
    }


    public ServerReponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //说明该用户的密码以及问题是匹配的
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerReponse.createBySuccess(forgetToken);
        }
        return ServerReponse.createByErrorMessage("问题的答案错误");
    }


    public ServerReponse<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return ServerReponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerReponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerReponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return ServerReponse.createByErrorMessage("token无效或者过期");
        }
        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);

            if(rowCount > 0){
                return ServerReponse.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerReponse.createByErrorMessage("token错误，请重新获取重置密码的token");
        }
        return ServerReponse.createByErrorMessage("重置密码失败");
    }


    public ServerReponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //防止横向越权，要校验一下这个影虎的旧密码，一定要是这个用户的

        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if(resultCount == 0){
            return ServerReponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>0){
            return ServerReponse.createBySuccessMessage("密码更新成功");
        }
        return ServerReponse.createByErrorMessage("密码更新失败");
    }


    public ServerReponse<User> updateInformation(User user){
        //username不能被更新
        //需要校验email,校验这个email是否已经存在，并且如果存在的话，该email不能是当前用户的
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServerReponse.createByErrorMessage("email已经存在，请更换email再尝试更新");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount>0){
            return ServerReponse.createBySuccess("更新个人信息成功",user);
        }
        return ServerReponse.createByErrorMessage("更新个人信息失败");

    }

    public ServerReponse<User> getInformation(Integer userId){

        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerReponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerReponse.createBySuccess(user);
    }


    //backend

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    public ServerReponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerReponse.createBySuccess();
        }
        return ServerReponse.createByError();
    }












}
