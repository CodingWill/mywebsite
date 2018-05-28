package com.eCommerce.controller.backend;

import com.eCommerce.common.Const;
import com.eCommerce.common.ResponseCode;
import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.User;
import com.eCommerce.service.IOrderService;
import com.eCommerce.service.IUserService;
import com.eCommerce.vo.OrderVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;



@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IOrderService iOrderService;


    // 1. get order list
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpSession session,
                                              @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
                                              @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login and try again");
        }
        // verify if current user is admin
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // if true, admin
            return iOrderService.manageList(pageNumber, pageSize);
        }
        else {
            return ServerResponse.createByErrorMessage("NO authority");
        }
    }

    // 2. get order detail
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpSession session, Long orderNo) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login and try again");
        }
        // verify if current user is admin
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // if true, admin
            return iOrderService.manageDetail(orderNo);
        }
        else {
            return ServerResponse.createByErrorMessage("NO authority");
        }
    }

    // 3. search by order no.
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderSearch(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login and try again");
        }
        // verify if current user is admin
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // if true, admin
            return iOrderService.manageSearch(orderNo);
        }
        else {
            return ServerResponse.createByErrorMessage("NO authority");
        }
    }

    // 3. manage shipping
    @RequestMapping("shipping_item.do")
    @ResponseBody
    public ServerResponse<String> shippingItem(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login and try again");
        }
        // verify if current user is admin
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // if true, admin
            return iOrderService.manageShipping(orderNo);
        }
        else {
            return ServerResponse.createByErrorMessage("NO authority");
        }
    }
}
