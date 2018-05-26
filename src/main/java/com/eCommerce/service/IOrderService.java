package com.eCommerce.service;

import com.eCommerce.common.ServerResponse;

import java.util.Map;

/**
 * Created by chao on 5/25/18.
 */
public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallback(Map<String,String> params);

    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
}
