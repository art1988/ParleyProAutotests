package com.parley.testing.configuration;

import com.parley.testing.aws.AwsService;
import com.parley.testing.configuration.properties.PropertiesConfiguration;
import com.parley.testing.listener.UITestListener;
import com.parley.testing.pages.PageFactory;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;


@Configuration
@Import(value = {PropertiesConfiguration.class})
@ComponentScan(basePackages = {"com.parley.testing.context", "com.parley.testing.listener"})
public class TestsConfiguration {

    @Value("${browser}")
    private String browser;

    @Value("${platform}")
    private String platform;

    @Value("${webdriver.gecko.driver}")
    private String firefoxDriver;

    @Value("${base.url}")
    private String baseUrl;

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${sent.to.aws}")
    private String isSentToAws;

    @Bean
    FirefoxDriver driver(){
        System.setProperty("webdriver.gecko.driver", firefoxDriver);
        return new FirefoxDriver();
    }

    @Bean
    AwsService awsService(){
        System.setProperty("sent.to.aws", isSentToAws);
        return new AwsService(accessKey, secretKey, awsRegion);
    }

    @Bean
    PageFactory pageFactory() {
        return new PageFactory(driver(), baseUrl);
    }


}
