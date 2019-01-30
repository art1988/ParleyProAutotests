package com.parley.testing.pages;

import com.parley.testing.exceptions.IllegalPageException;
import com.parley.testing.pages.impl.*;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

public class PageFactory {

    private String baseUrl;

    @Value("${webdriver.implicit.wait.timeout}")
    protected long implicitWaitTimeout;

    private WebDriver webDriverProvider;

    private LoginPage loginPage;
    private InProgressContractsPage inProgressContractsPage;
    private InProgressContractPage inProgressContractPage;
    private ExecutedContractsPage executedContractsPage;
    private ExecutedContractPage executedContractPage;

    public PageFactory(WebDriver webDriverProvider, String baseUrl) {
        this.webDriverProvider = webDriverProvider;
        this.baseUrl = baseUrl;
        initIt();
    }

    public void initIt(){
        webDriverProvider.manage().timeouts().implicitlyWait(implicitWaitTimeout, TimeUnit.SECONDS);
        webDriverProvider.get(baseUrl);
    }

    public void maximize(){
        webDriverProvider.manage().window().maximize();
    }

    public LoginPage loginPage() throws IllegalPageException {
        if (loginPage == null || loginPage.isSessionLost()) {
            loginPage = new LoginPage(webDriverProvider);
        }
        loginPage.checkCurrentPage();
        return loginPage;
    }

    public InProgressContractsPage inProgressContractsPage() throws IllegalPageException {
        if (inProgressContractsPage == null || inProgressContractsPage.isSessionLost()) {
            inProgressContractsPage = new InProgressContractsPage(webDriverProvider);
        }
        inProgressContractsPage.checkCurrentPage();
        return inProgressContractsPage;
    }

    public InProgressContractPage inProgressContractPage() throws Throwable {
        if (inProgressContractPage == null || inProgressContractPage.isSessionLost()) {
            inProgressContractPage = new InProgressContractPage(webDriverProvider);
        }
        return inProgressContractPage;
    }

    public ExecutedContractsPage executedContractsPage() throws Throwable {
        if (executedContractsPage == null || executedContractsPage.isSessionLost()) {
            executedContractsPage = new ExecutedContractsPage(webDriverProvider);
        }
        return executedContractsPage;
    }

    public ExecutedContractPage executedContractPage() throws Throwable {
        if (executedContractPage == null || executedContractPage.isSessionLost()) {
            executedContractPage = new ExecutedContractPage(webDriverProvider);
        }
        return executedContractPage;
    }
}
