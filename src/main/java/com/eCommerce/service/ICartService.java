package com.eCommerce.service;

import com.eCommerce.common.ServerResponse;
import com.eCommerce.vo.CartVo;

/**
 * Created by chao on 5/24/18.
 */
public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    ServerResponse<CartVo> list(Integer userId);

    ServerResponse<CartVo> selectOrUnselect (Integer userId, Integer productId, Integer checked);

    ServerResponse<Integer> getCount(Integer userId);
}