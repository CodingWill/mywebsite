package com.eCommerce.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by chao on 5/19/18.
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc", "price_asc");
    }

    public interface Role {
        int ROLE_CUSTOMER = 0; //USERS
        int ROLE_ADMIN = 1; // ADMINISTER
    }

    public enum ProductStatusEnum {
        AVAILABLE_NOW(1, "available");
        private int code;
        private String value;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
