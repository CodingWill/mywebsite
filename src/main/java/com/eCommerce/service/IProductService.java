package com.eCommerce.service;

import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.Product;
import com.eCommerce.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;


public interface IProductService {

    ServerResponse saveOrUpdateProduct (Product product);

    ServerResponse<String> setProductStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNumber, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName,
                                           Integer productId,
                                           int pageNumber,
                                           int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,
                                                         Integer categoryId,
                                                         int pageNumber,
                                                         int pageSize,
                                                         String orderBy);

}
