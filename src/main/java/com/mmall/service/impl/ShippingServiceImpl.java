package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerReponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/11/22 0022.
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService{

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerReponse add(Integer userId,Shipping shipping){
        shipping.setUserId(userId);

        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerReponse.createBySuccess("新建地址成功",result);
        }
        return ServerReponse.createByErrorMessage("新建地址失败");
    }


    public ServerReponse<String> del(Integer userId,Integer shippingId){
        int resultCount = shippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if(resultCount > 0){
            return ServerReponse.createBySuccess("删除地址成功");
        }
        return ServerReponse.createByErrorMessage("删除地址失败");
    }

    public ServerReponse update(Integer userId,Shipping shipping){
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if(rowCount > 0){
            return ServerReponse.createBySuccess("更新地址成功");
        }
        return ServerReponse.createByErrorMessage("更新地址失败");
    }

    public ServerReponse<Shipping> select(Integer userId,Integer shippingId){
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping == null){
            return ServerReponse.createByErrorMessage("无法查询到该地址");
        }
        return ServerReponse.createBySuccess(shipping);

    }

    public ServerReponse<PageInfo> list(Integer userId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerReponse.createBySuccess(pageInfo);
    }









}
