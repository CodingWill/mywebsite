package com.eCommerce.service;

import com.eCommerce.common.ServerResponse;
import com.eCommerce.pojo.User;

import javax.servlet.http.HttpSession;

/**
 * Created by chao on 5/19/18.
 */
public interface IUserService {

    ServerResponse<User> login(String username, String password);

    ServerResponse<String> register (User user);

    ServerResponse<String> checkValid(String string, String type);

    ServerResponse selectQuestion(String username);

    ServerResponse<String> verifyAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username,
                                               String newPassword,
                                               String forgetToken);

    ServerResponse<String> resetPassword(String oldPassword,
                                         String newPassword,
                                         User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation (Integer userId);

    ServerResponse checkAdminRole(User user);
}
