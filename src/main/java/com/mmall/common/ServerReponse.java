package com.mmall.common;

import com.sun.org.apache.regexp.internal.RE;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/11/5 0005.
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候，如果是null的对象，key也会消失
public class ServerReponse<T> implements Serializable{
    private int status;
    private String msg;
    private T data;

    private ServerReponse(int status){
        this.status = status;
    }

    private ServerReponse(int status,T data){
        this.status = status;
        this.data = data;
    }

    private ServerReponse(int status,String msg,T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerReponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    //使其不在json序列化结果当中
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return status;
    }

    public T getData(){
        return data;
    }

    public String getMsg(){
        return msg;
    }

    public static <T> ServerReponse<T> createBySuccess() {
        return new ServerReponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerReponse<T> createBySuccessMessage(String msg){
        return new ServerReponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerReponse<T> createBySuccess(T data) {
        return new ServerReponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

    public static <T> ServerReponse<T> createBySuccess(String msg,T data) {
        return new ServerReponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static <T> ServerReponse<T> createByError(){
        return new ServerReponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerReponse<T> createByErrorMessage(String errorMessage){
        return new ServerReponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static <T> ServerReponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerReponse<T>(errorCode,errorMessage);
    }

}
