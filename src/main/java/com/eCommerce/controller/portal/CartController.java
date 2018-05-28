package com.eCommerce.controller.portal;

import com.eCommerce.common.Const;
import com.eCommerce.common.ResponseCode;
import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.User;
import com.eCommerce.service.ICartService;
import com.eCommerce.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;



@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    // 1. add to shopping cart
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        return iCartService.add(user.getId(), productId, count);
    }

    // 2. update cart
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        return iCartService.update(user.getId(), productId, count);
    }

    // 3. delete product
    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(HttpSession session,
                                                String productIds) { // pass multiple product, separate by comma
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }

    // 4. get product list in cart
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        return iCartService.list(user.getId());
    }

    // 5. select product
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        return iCartService.selectOrUnselect(user.getId(), productId, Const.Cart.CHECKED);
    }

    // 6.un_select product
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        return iCartService.selectOrUnselect(user.getId(), productId, Const.Cart.UN_CHECKED);
    }


    // 7. select all
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        // select all-> productId = null
        return iCartService.selectOrUnselect(user.getId(), null, Const.Cart.CHECKED);
    }

    // 8. un_select all
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login ");
        }
        return iCartService.selectOrUnselect(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    // 9. get count
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCount(user.getId());
    }



}
