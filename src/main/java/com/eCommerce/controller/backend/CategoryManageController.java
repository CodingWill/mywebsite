package com.eCommerce.controller.backend;

import com.eCommerce.common.Const;
import com.eCommerce.common.ResponseCode;
import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.User;
import com.eCommerce.service.ICategoryService;
import com.eCommerce.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by chao on 5/21/18.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    // 1. add category
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,
                                      String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0")
                                      int parentId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login and try again");
        }
        // verify if current user is admin
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // if true, admin
            return iCategoryService.addCategory(categoryName, parentId);
        }
        else {
            return ServerResponse.createByErrorMessage("NO authority");
        }
    }

    // 2. update category
    @RequestMapping("update_category_name.do")
    @ResponseBody
    public ServerResponse updateCategoryName(HttpSession session,
                                             Integer categoryId,
                                             String categoryName) {
        // check authority and login status
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login and try again");
        }
        // verify if current user is admin
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // if true, admin
            return iCategoryService.updateCategoryName(categoryId, categoryName);
        }
        else {
            return ServerResponse.createByErrorMessage("NO authority");
        }
    }

    // 3. get parallel subcategory
    @RequestMapping("get_parallel_subcategory.do")
    @ResponseBody
    public ServerResponse getParallelSubcategory (HttpSession session,
                                                  @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        // check authority and login status
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login and try again");
        }
        // verify if current user is admin
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // if true, admin
            return iCategoryService.getParallelSubcategory(categoryId);
        }
        else {
            return ServerResponse.createByErrorMessage("NO authority");
        }
    }

    // 4. get subcategory with recursion
    @RequestMapping("get_subcategory.do")
    @ResponseBody
    public ServerResponse getSubcategory (HttpSession session,
                                          @RequestParam(value = "categoryId", defaultValue = "0")
                                          Integer categoryId) {
        // check authority and login status
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "Please login and try again");
        }
        // verify if current user is admin
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // if true, admin
            return iCategoryService.getSubcategory(categoryId);

        }
        else {
            return ServerResponse.createByErrorMessage("NO authority");
        }
    }

}
