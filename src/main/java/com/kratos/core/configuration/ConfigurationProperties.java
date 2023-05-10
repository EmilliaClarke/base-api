package com.kratos.core.configuration;

/**
 * This class is aimed to store different properties.
 */
public interface ConfigurationProperties {

    interface REST {
        public static String BASE_URI = "base.uri";
        public static String TOKEN_BASE_URL = "token.base.url";
        public static String IES_USERNAME = "ies.username";
        public static String IES_PASSWORD = "ies.password";
        public static String IES_USER_ID = "ies.userId";
    }
}
