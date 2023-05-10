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
}
