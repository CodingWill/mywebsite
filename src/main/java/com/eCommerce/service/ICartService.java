package com.eCommerce.service;

import com.eCommerce.common.ServerResponse;
import com.eCommerce.vo.CartVo;

/**
 * Created by chao on 5/24/18.
 */
public interface ICartService {

    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);
}
