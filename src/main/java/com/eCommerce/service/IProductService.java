package com.eCommerce.service;

import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.Product;
import com.eCommerce.vo.ProductDetailVo;
import com.github.pagehelper.PageInfo;

/**
 * Created by chao on 5/22/18.
 */
public interface IProductService {

    ServerResponse saveOrUpdateProduct (Product product);

    ServerResponse<String> setProductStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNumber, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName,
                                           Integer productId,
                                           int pageNumber,
                                           int pageSize);

}
