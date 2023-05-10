package com.akami.core.configuration;


import org.apache.commons.configuration2.CompositeConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


/**
 * This class keeps and uses Configuration to work with properties.
 */
public class ConfigurationService {

    private static final Object SYN_OBJ = new Object();
    private static Configuration configuration = null;
    private static final String API_AUTOMATION_PROPERTIES = "automation.properties.path";
    private static final String LOCAL_PROPERTIES = "local.properties.path";
    private static final String CUCUMBER_PROPERTIES = "cucumber.properties.path";

    private ConfigurationService() {

    }

    /**
     * Gets configuration instance.
     *
     * @return the config instance
     */
    public static Configuration getConfigInstance() {
        if (configuration == null) {
            synchronized (SYN_OBJ) {
                if (configuration == null) {
                    CompositeConfiguration compositeConfiguration = new CompositeConfiguration();

                    compositeConfiguration.addConfiguration(new SystemConfiguration());

                    loadOptionalConfiguration(resolvePath(LOCAL_PROPERTIES), "Local Properties")
                            .ifPresent(compositeConfiguration::addConfiguration);
                    compositeConfiguration.addConfiguration(
                            loadRequiredConfiguration(resolvePath(API_AUTOMATION_PROPERTIES), "Automation Properties"));
                    compositeConfiguration.addConfiguration(
                            loadRequiredConfiguration(resolvePath(CUCUMBER_PROPERTIES), "Cucumber Properties"));

                    configuration = compositeConfiguration;
                }
            }
        }
        return configuration;
    }

    private static String resolvePath(final String propertyWithPath) {
        return System.getProperties().getProperty(propertyWithPath);
    }

    private static Configuration loadRequiredConfiguration(final String path, final String printableName)
    {
        try
        {
            return loadConfiguration(path, printableName);
        }
        catch (ConfigurationException e)
        {
            throw new IllegalStateException("Unable to load required configuration", e);
        }
    }

    private static Optional<Configuration> loadOptionalConfiguration(final String path, final String printableName)
    {
        try
        {
            return Optional.of(loadConfiguration(path, printableName));
        }
        catch (ConfigurationException e)
        {
            return Optional.empty();
        }
    }

    private static Configuration loadConfiguration(final String path, final String printableName)
            throws ConfigurationException
    {
        final Configuration configuration = getFileConfigBuilder(path).getConfiguration();
        return configuration;
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
     * Gets property or returns default value if no property found.
     *
     * @param property     the property name
     * @param defaultValue the default value to return
     * @return the property value
     */
    public static String getProperty(String property, String defaultValue) {
        return getConfigInstance().getString(property, defaultValue).trim();
    }

	private static FileBasedConfigurationBuilder<FileBasedConfiguration> getFileConfigBuilder(String filePath) {
		Path path = Paths.get(filePath);
		return new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
				.configure(new Parameters().fileBased().setFile(path.toFile()));
	}
}
