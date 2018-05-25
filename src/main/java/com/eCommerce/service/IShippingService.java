package com.eCommerce.service;

import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.ShippingAddress;
import com.github.pagehelper.PageInfo;

/**
 * Created by chao on 5/25/18.
 */
public interface IShippingService {

    ServerResponse add(Integer userId, ShippingAddress shippingAddress);

    ServerResponse<String> delete(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId, ShippingAddress shippingAddress);

    ServerResponse<ShippingAddress> select(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId, int pageNumber, int pageSize);
}
