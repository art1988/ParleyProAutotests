package com.parley.testing.configuration;

import com.parley.testing.configuration.properties.PropertiesConfiguration;
import com.parley.testing.listener.UITestListener;
import com.parley.testing.pages.PageFactory;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;


@Configuration
@Import(value = {PropertiesConfiguration.class})
@ComponentScan(basePackages = "com.parley.testing.context*")
public class TestsConfiguration {

    @Value("${browser}")
    private String browser;

    @Value("${platform}")
    private String platform;

    @Value("${webdriver.gecko.driver}")
    private String firefoxDriver;

    @Value("${base.url}")
    private String baseUrl;

    @Bean
    FirefoxDriver driver(){
        System.setProperty("webdriver.gecko.driver", firefoxDriver);
        return new FirefoxDriver();
    }

    @Bean
    PageFactory pageFactory() {
        return new PageFactory(driver(), baseUrl);
    }


}
