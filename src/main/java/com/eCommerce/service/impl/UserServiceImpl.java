package com.eCommerce.service.impl;

import com.eCommerce.common.Const;
import com.eCommerce.common.ServerResponse;
import com.eCommerce.common.TokenCache;
import com.eCommerce.dao.UserMapper;
import com.eCommerce.pojo.User;
import com.eCommerce.service.IUserService;
import com.eCommerce.util.MD5Util;
import com.github.pagehelper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * Created by chao on 5/19/18.
 */
// inject to controller, interface
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            // no user name in database
            return ServerResponse.createByErrorMessage("Invalid User Name");
        }

        // use encrypted password login
        String encryptedPassword = MD5Util.MD5EncodeUtf8(password);
        User user  = userMapper.selectLogin(username,encryptedPassword);
        if(user == null){
            return ServerResponse.createByErrorMessage("Invalid Password");
        }

        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);

        return ServerResponse.createBySuccess("Login Successfully",user);
    }

    public ServerResponse<String> register (User user) {
        // check user name
        int usernameCount = userMapper.checkUsername(user.getUsername());
        if (usernameCount > 0) {
            return ServerResponse.createByErrorMessage("User name already exists");
        }
        // check user email
        int emailCount = userMapper.checkEmail(user.getEmail());
        if (emailCount > 0) {
            return ServerResponse.createByErrorMessage("Email already exists");
        }

        // set role(limits of authority), user/administer
        user.setRole(Const.Role.ROLE_CUSTOMER);
        // encrypt using MD5
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int userCount = userMapper.insert(user);
        if (userCount == 0) {
            // insert failed
            return ServerResponse.createByErrorMessage("Registration failed");
        }
        return ServerResponse.createBySuccessMessage("You have successfully registered");
    }

    public ServerResponse<String> checkValid(String string, String type) {
        // blank is different from empty!!
        // " " blank -> meaningless to type
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            if(Const.USERNAME.equals(type)){
                int nameCount = userMapper.checkUsername(string);
                if(nameCount > 0 ){
                    return ServerResponse.createByErrorMessage("User name already exists");
                }
            }
            if(Const.EMAIL.equals(type)){
                int emailCount = userMapper.checkEmail(string);
                if(emailCount > 0 ){
                    return ServerResponse.createByErrorMessage("Email already exists");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("Invalid");
        }
        return ServerResponse.createBySuccessMessage("Valid");
    }

    public ServerResponse selectQuestion(String username) {
        // check valid
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        // use "NOT" ->
        if(validResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("User doesn't exist");
        }
        String question = userMapper.selectQuestionByUsername(username);
        // blank question is invalid
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("Security question is invalid");
    }

    public ServerResponse<String> verifyAnswer(String username,
                                               String question,
                                               String answer) {
        int count = userMapper.verifyAnswer(username,question,answer);
        if(count>0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("Wrong answer");
    }

    public ServerResponse<String> forgetResetPassword(String username,
                                                      String newPassword,
                                                      String forgetToken){
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("Error, token is needed");
        }
        // check user name
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if(validResponse.isSuccess()) {
            // user doesn't exist
            return ServerResponse.createByErrorMessage("User doesn't exist");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if(org.apache.commons.lang3.StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("Invalid token");
        }

        // use StringUtils.equals, safer, avoid null pointer problem
        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)) {
            String encryptedPassword  = MD5Util.MD5EncodeUtf8(newPassword);
            int rowCount = userMapper.updatePasswordByUsername(username,encryptedPassword);

            if(rowCount > 0) {
                return ServerResponse.createBySuccessMessage("Reset password successfully");
            }
        }else{
            return ServerResponse.createByErrorMessage("Wrong token, try again");
        }
        return ServerResponse.createByErrorMessage("Reset password failed");
    }


    // reset password
    public ServerResponse<String> resetPassword(String oldPassword,
                                                String newPassword,
                                                User user) {
        // verify old password according to user id
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword),user.getId());
        if(resultCount == 0) {
            return ServerResponse.createByErrorMessage("Old password is wrong");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0) {
            return ServerResponse.createBySuccessMessage("Reset password successfully");
        }
        return ServerResponse.createByErrorMessage("Reset password failed");
    }

    // update info
    public ServerResponse<User> updateInformation(User user) {
        int count = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(count > 0) {
            return ServerResponse.createByErrorMessage("Email already exists, please use other email");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponse.createBySuccess("Profile updated",updateUser);
        }
        return ServerResponse.createByErrorMessage("Failed to update profile");
    }

    // get user info
    public ServerResponse<User> getInformation (Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.createByErrorMessage("User cannot be found");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }


    // backend part
    // check if current user is admin
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage("NOT an administer");
    }
























}
