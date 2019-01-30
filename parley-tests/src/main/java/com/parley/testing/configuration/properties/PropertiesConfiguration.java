package com.parley.testing.configuration.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Properties Configuration
 */
@Configuration
public class PropertiesConfiguration {

    /**
     * Create Property Placeholder Configurer Bean
     *
     * @return Property Placeholder Configurer Bean
     */
    @Bean
    public CustomPropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new CustomPropertyPlaceholderConfigurer().setResources(
                new String[]{
                        "/test.properties"
                },
                null,
                new String[]{"/environment.properties"}
        );
    }
}
