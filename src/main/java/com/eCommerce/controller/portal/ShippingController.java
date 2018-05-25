package com.eCommerce.controller.portal;

import com.eCommerce.common.Const;
import com.eCommerce.common.ResponseCode;
import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.ShippingAddress;
import com.eCommerce.pojo.User;
import com.eCommerce.service.IShippingService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by chao on 5/25/18.
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    // 1. add new address
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session, ShippingAddress shippingAddress) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        return iShippingService.add(user.getId(),shippingAddress);
    }

    // 2. delete an address
    @RequestMapping("delete.do")
    @ResponseBody
    public ServerResponse delete(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        return iShippingService.delete(user.getId(), shippingId);
    }

    // 3. update address
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, ShippingAddress shippingAddress) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        return iShippingService.update(user.getId(), shippingAddress);
    }

    // 4. view
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<ShippingAddress> select(HttpSession session,Integer shippingId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        return iShippingService.select(user.getId(),shippingId);
    }

    // 5. address list
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum",defaultValue = "1") int pageNumber,
                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                         HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        return iShippingService.list(user.getId(),pageNumber,pageSize);
    }



}
