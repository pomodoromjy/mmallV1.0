package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerReponse;
import com.mmall.pojo.Shipping;

/**
 * Created by Administrator on 2019/11/22 0022.
 */
public interface IShippingService {

    ServerReponse add(Integer userId, Shipping shipping);

    ServerReponse<String> del(Integer userId,Integer shippingId);

    ServerReponse update(Integer userId,Shipping shipping);

    ServerReponse<Shipping> select(Integer userId,Integer shippingId);

    ServerReponse<PageInfo> list(Integer userId, int pageNum, int pageSize);

}
