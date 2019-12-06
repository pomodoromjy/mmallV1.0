package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerReponse;
import com.mmall.vo.OrderVo;

import java.util.Map;

/**
 * Created by Administrator on 2019/11/27 0027.
 */
public interface IOrderService {

    ServerReponse pay(Long orderNo, Integer userId, String path);
    ServerReponse aliCallback(Map<String,String> params);
    ServerReponse queryOrderPayStatus(Integer userId,Long orderNo);
    ServerReponse createOrder(Integer userId,Integer shippingId);
    ServerReponse<String> cancel(Integer userId,Long orderNo);
    ServerReponse getOrderCartProduct(Integer userId);
    ServerReponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);
    ServerReponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);


    //backend
    ServerReponse<PageInfo> manageList(int pageNum,int pageSize);
    ServerReponse<OrderVo> manageDetail(Long orderNo);
    ServerReponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);
    ServerReponse<String> manageSendGoods(Long orderNo);

}
