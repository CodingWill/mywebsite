package com.eCommerce.dao;

import com.eCommerce.pojo.ShippingAddress;

public interface ShippingAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ShippingAddress record);

    int insertSelective(ShippingAddress record);

    ShippingAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShippingAddress record);

    int updateByPrimaryKey(ShippingAddress record);
}