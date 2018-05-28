package com.eCommerce.service.impl;

import com.eCommerce.common.ServerResponse;
import com.eCommerce.dao.ShippingAddressMapper;
import com.eCommerce.pojo.ShippingAddress;
import com.eCommerce.service.IShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingAddressMapper shippingAddressMapper;

    public ServerResponse add(Integer userId, ShippingAddress shippingAddress) {
        shippingAddress.setUserId(userId);
        // change shippingaddressmapper(xml), after insertion generate key and save to shippingAdderss
        int count = shippingAddressMapper.insert(shippingAddress);
        if (count > 0) {
            Map res = Maps.newHashMap();
            res.put("shippingId", shippingAddress.getId());
            return ServerResponse.createBySuccess("New address is added", res);
        }
        return ServerResponse.createByErrorMessage("Add address failed");
    }

    public ServerResponse<String> delete(Integer userId, Integer shippingId) {
        int count = shippingAddressMapper.deleteByShippingIdUserId(userId, shippingId);
        if (count > 0) {
            return ServerResponse.createBySuccess("Address deleted");
        }
        return ServerResponse.createByErrorMessage("Delete address failed");
    }

    public ServerResponse update(Integer userId, ShippingAddress shippingAddress) {
        shippingAddress.setUserId(userId);
        int count = shippingAddressMapper.updateByShipping(shippingAddress);
        if (count > 0) {
            return ServerResponse.createBySuccess("Address updated");
        }
        return ServerResponse.createByErrorMessage("Update address failed");
    }

    public ServerResponse<ShippingAddress> select(Integer userId, Integer shippingId) {
        ShippingAddress shippingAddress = shippingAddressMapper.selectByShippingIdUserId(userId, shippingId);
        if (shippingAddress == null) {
            return ServerResponse.createByErrorMessage("no such address");
        }
        return ServerResponse.createBySuccess("Success", shippingAddress);
    }

    public ServerResponse<PageInfo> list(Integer userId, int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        List<ShippingAddress> shippingList = shippingAddressMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}
