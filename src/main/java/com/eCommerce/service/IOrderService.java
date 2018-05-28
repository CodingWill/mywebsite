package com.eCommerce.service;

import com.eCommerce.common.ServerResponse;
import com.eCommerce.vo.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;


public interface IOrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallback(Map<String,String> params);

    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);

    ServerResponse createOrder(Integer userId, Integer shippingId);

    ServerResponse<String> cancel(Integer userId, Long orderNo);

    ServerResponse getOrderCartProduct(Integer userId);

    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    public ServerResponse<PageInfo> getOrderList(Integer userId,
                                                 int pageNumber,
                                                 int pageSize);

    ServerResponse<PageInfo> manageList(int pageNumber, int pageSize);

    ServerResponse<OrderVo> manageDetail(Long orderNo);

    ServerResponse<OrderVo> manageSearch(Long orderNo);

    ServerResponse<String> manageShipping(Long orderNo);
}
