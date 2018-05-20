package com.eCommerce.dao;

import com.eCommerce.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    // check if username is valid
    int checkUsername(String username);

    // check if email is valid
    int checkEmail(String email);

    // when pass multiple arguments, use @Para
    User selectLogin(@Param("username") String username,
                     @Param("password")String password);

    // s_question
    String selectQuestionByUsername(String username);

    // verify answer
    int verifyAnswer(@Param("username") String username,
                    @Param("question") String question,
                    @Param("answer") String answer);

    // update password
    int updatePasswordByUsername(@Param("username") String username,
                                 @Param("password") String password);

    // check if old password is correct
    int checkPassword(@Param(value = "password") String password,
                      @Param(value = "userId") Integer userId);

    int checkEmailByUserId(@Param(value = "email") String email,
                           @Param(value = "userId") Integer userId);

}