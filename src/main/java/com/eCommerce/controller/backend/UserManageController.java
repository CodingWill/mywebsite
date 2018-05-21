package com.eCommerce.controller.backend;

import com.eCommerce.common.Const;
import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.User;
import com.eCommerce.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by chao on 5/20/18.
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username,
                                      String password,
                                      HttpSession session) {
        ServerResponse<User> reponse = iUserService.login(username, password);
        if (reponse.isSuccess()) {
            User user = reponse.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                // administer
                session.setAttribute(Const.CURRENT_USER, user);
                return reponse;
            }
            else {
                return ServerResponse.createByErrorMessage("NOT administer, NO authority");
            }
        }
        return reponse;
    }

}
