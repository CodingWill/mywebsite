package com.eCommerce.dao;

import com.eCommerce.pojo.PaymentInfo;

public interface PaymentInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PaymentInfo record);

    int insertSelective(PaymentInfo record);

    PaymentInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PaymentInfo record);

    int updateByPrimaryKey(PaymentInfo record);
}