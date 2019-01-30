package com.parley.testing.configuration.properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

public class CustomPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements InitializingBean {

    /**
     * Null Value For Configurer
     */
    public static final String NULL_VALUE = "#{null}";

    /**
     * Environment Configuration Home Property Name
     */
    public static final String ENVIRONMENT_CONFIGURATION_HOME = "environment.configuration.home";

    /**
     * Environment Configuration Home
     */
    private String environmentConfigurationHome = null;

    /**
     * Set Class Path Resources And File System Resources
     *
     * @param classPathResources                           Resources From Class Path
     * @param fileSystemResources                          File System Resources Path
     * @param relativeConfigurationHomeFileSystemResources Resources From System Environment
     *                                                     Build System Resource Algorithm: environmentConfigurationHome + resource
     */
    public CustomPropertyPlaceholderConfigurer setResources(String[] classPathResources,
                                                            String[] fileSystemResources,
                                                            String[] relativeConfigurationHomeFileSystemResources) {
        Collection<Resource> resources = new ArrayList<Resource>();
        if (classPathResources != null) {
            for (String resource : classPathResources) {
                resources.add(new ClassPathResource(resource));
            }
        }
        if (fileSystemResources != null) {
            for (String resource : fileSystemResources) {
                resources.add(new FileSystemResource(resource));
            }
        }
        if (environmentConfigurationHome != null && relativeConfigurationHomeFileSystemResources != null) {
            for (String resource : relativeConfigurationHomeFileSystemResources) {
                logger.info("Adding environment properties " + environmentConfigurationHome + "/" + resource);
                resources.add(new FileSystemResource(environmentConfigurationHome + resource));
            }
        }
        this.setLocations(resources.toArray(new Resource[resources.size()]));
        return this;
    }

    /**
     * Custom PropertyPlaceholder Configurer Constructor
     * Initialisation of environmentConfigurationHome system property
     */
    public CustomPropertyPlaceholderConfigurer() {
        StandardEnvironment standardEnvironment = new StandardEnvironment();
        environmentConfigurationHome = standardEnvironment.getProperty(ENVIRONMENT_CONFIGURATION_HOME);
        setOrder(0);
        setLocalOverride(true);
        setNullValue(NULL_VALUE);
        setIgnoreResourceNotFound(true);
        setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        setSearchSystemEnvironment(true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Loading properties");
        try {
            // print logback's internal status
            Properties loadedProperties = this.mergeProperties();
            printProperties(loadedProperties);
            loadedProperties = System.getProperties();
            printProperties(loadedProperties);
            loadedProperties = new Properties();
            loadedProperties.putAll(System.getenv());
            printProperties(loadedProperties);
        } catch (Exception ex) {
            logger.error("Exception while loading properties", ex);
            ex.printStackTrace();
        }
    }

    private void printProperties(Properties loadedProperties) {
        for (Map.Entry<Object, Object> singleProperty : loadedProperties.entrySet()) {
            logger.info("LoadedProperty: " + singleProperty.getKey() + "=" + singleProperty.getValue());
        }
    }
}
