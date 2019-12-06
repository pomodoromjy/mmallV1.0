package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerReponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * Created by Administrator on 2019/11/12 0012.
 */
public interface IProductService {

    ServerReponse saveOrUpdateProduct(Product product);

    ServerReponse<String> setSaleStatus(Integer productId,Integer status);

    ServerReponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerReponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerReponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    ServerReponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerReponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);

}
