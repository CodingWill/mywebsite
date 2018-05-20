package com.eCommerce.common;

/**
 * Created by chao on 5/19/18.
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface Role {
        int ROLE_CUSTOMER = 0; //USERS
        int ROLE_ADMIN = 1; // ADMINISTER
    }
}
