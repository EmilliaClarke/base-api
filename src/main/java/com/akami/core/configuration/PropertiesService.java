package com.akami.core.configuration;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * This class keeps and uses Configuration to work with properties.
 */
public class PropertiesService {

    private static final String API_AUTOMATION_PROPERTIES = "config/apiautomation.properties";
    private static final String API_AUTOMATION_PROPERTIES1 = "config/apiautomation1.properties";

    private PropertiesService() {

    }

    public static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(resolvePath(API_AUTOMATION_PROPERTIES)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            properties.load(new FileInputStream(resolvePath(API_AUTOMATION_PROPERTIES1)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private static String resolvePath(final String propertyWithPath) {
        File file = new File(propertyWithPath);
        return file.getAbsoluteFile().getAbsolutePath();
    }

    /**
     * Gets property or returns empty string if no property found.
     *
     * @param property the property name
     * @return the property
     */
    public static String getProperty(String property) {
        return getProperty(property, StringUtils.EMPTY);
    }

    /**
     * Gets property by placeholder or returns empty string if no property found.
     *
     * @param propertyPlaceholder the property name
     * @return the property
     */
    public static List<String> getPropertyByPlaceholder(String propertyPlaceholder) {
        List<String> propertyNames = getStringFromPropertyPlaceholder(propertyPlaceholder);
        return propertyNames.stream()
                .map(PropertiesService::getProperty)
                .collect(Collectors.toList());
    }

    private static List<String> getStringFromPropertyPlaceholder(String propertyPlaceholder) {
        final Pattern PROPERTY_PATTERN = Pattern.compile("\\{(.+?)\\}");
        Matcher matcher = PROPERTY_PATTERN.matcher(propertyPlaceholder);
        List<String> propertiesList = new ArrayList<>();
        while (matcher.find()) {
            propertiesList.add(matcher.group(1));
        }
        if (propertiesList.isEmpty()) {
            try {
                throw new Exception("Property placeholder have an incorrect format");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return propertiesList;
    }

    /**
     * Gets property or returns default value if no property found.
     *
     * @param property     the property name
     * @param defaultValue the default value to return
     * @return the property value
     */
    public static String getProperty(String property, String defaultValue) {
        return loadProperties().getProperty(property, defaultValue).trim();
    }
}

