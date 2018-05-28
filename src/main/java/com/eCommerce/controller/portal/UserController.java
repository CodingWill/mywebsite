package com.eCommerce.controller.portal;

import com.eCommerce.common.Const;
import com.eCommerce.common.ResponseCode;
import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.User;
import com.eCommerce.service.IUserService;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;


    // 1. login
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    // serialize return value to json using jackson plugin
    @ResponseBody
    public ServerResponse<User> login(String username,
                                      String password,
                                      HttpSession session) {
        ServerResponse<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }


    // 2. logout
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        // delete from session
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccessMessage("Already logout");
    }

    // 3. register
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){

        return iUserService.register(user);
    }

    // 4. check valid
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid (String string, String type) {
        return iUserService.checkValid(string, type);
    }

    // 5. get user information
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("Please login to get info");
    }

    // 6.password security question
    @RequestMapping(value = "security_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> securityQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    // 7. verify answers
    @RequestMapping(value = "verify_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> verifyAnswer(String username,
                                               String question,
                                               String answer) {
        return iUserService.verifyAnswer(username,question,answer);
    }


    // 8. reset password when forget
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username,
                                                      String newPassword,
                                                      String forgetToken) {
        return iUserService.forgetResetPassword(username,newPassword,forgetToken);
    }

    // 9. reset password after login
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,
                                                String oldPassword,
                                                String newPassword) {
        // check status, login or not
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorMessage("Please login and try again");
        }
        return iUserService.resetPassword(oldPassword, newPassword, user);
    }

    // 10. update user information
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session,User user){
        // check status, login or not
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorMessage("Please login and try again");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    // 11. get user information
    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation (HttpSession session) {
        // check status, login or not
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            // login first
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),
                    "login needed, status 10");
        }
        return iUserService.getInformation(currentUser.getId());
    }

}
