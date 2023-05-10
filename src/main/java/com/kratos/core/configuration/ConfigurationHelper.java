package com.kratos.core.configuration;

/**
 * The type Configuration helper.
 */
public final class ConfigurationHelper {

    private ConfigurationHelper() {
    }

    public static String getURI() {
        return ConfigurationService.getProperty(ConfigurationProperties.REST.BASE_URI);
    }

    public static String getTokenURI() {
        return ConfigurationService.getProperty(ConfigurationProperties.REST.TOKEN_BASE_URL);
    }

    public static String getIESUserName() {
        return ConfigurationService.getProperty(ConfigurationProperties.REST.IES_USERNAME);
    }

    public static String getIESPassword() {
        return ConfigurationService.getProperty(ConfigurationProperties.REST.IES_PASSWORD);
    }

    public static String getIESUserId() {
        return ConfigurationService.getProperty(ConfigurationProperties.REST.IES_USER_ID);
    }

}
