package com.eCommerce.dao;

import com.eCommerce.pojo.ShippingAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShippingAddress record);

    int insertSelective(ShippingAddress record);

    ShippingAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShippingAddress record);

    int updateByPrimaryKey(ShippingAddress record);

    int deleteByShippingIdUserId(@Param("userId") Integer userId,
                                 @Param("shippingId") Integer shippingId);

    int updateByShipping(ShippingAddress record);

    ShippingAddress selectByShippingIdUserId(@Param("userId") Integer userId,
                                             @Param("shippingId") Integer shippingId);

    List<ShippingAddress> selectByUserId(@Param("userId")Integer userId);
}