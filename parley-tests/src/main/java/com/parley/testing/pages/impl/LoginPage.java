package com.parley.testing.pages.impl;

import com.parley.testing.exceptions.IllegalPageException;
import com.parley.testing.pages.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.parley.testing.utils.AsyncAssert.waitForSuccess;
import static junit.framework.Assert.assertEquals;

public class LoginPage extends AbstractPage {

    private static final By USERNAME = By.id("1");
    private static final By PSSWD = By.id("2");
    private static final By LOGIN_BTN = By.xpath("//button[text()='SIGN IN']");
    private static final By LOGIN_ERROR_MESSAGE_ELEMENT = By.xpath("//div[contains(@class,'auth__error')]");
    private static final String LOGIN_ERROR_MESSAGE_TEXT = "Oops! That email/password combination is not valid";
    private static final String PAGE_URL = "http://app.parleypro.net/master/index.html#/login";


    public LoginPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void checkCurrentPage() throws IllegalPageException {
        // known issue - html page is cached, so need to reload page few times
        refresh();
        try {
            String title = "Login";
//            waitForSuccess(50, 100, () -> title.equals(findElement(LOGIN_BTN).getAttribute("value")));
        } catch (Throwable e){
            throw new IllegalPageException("Not on LoginPage: " + e.getMessage());
        }
    }

    public void login(String userName, String password) {
        getDriver().navigate().to(PAGE_URL);
        getDriver().manage().deleteAllCookies();
        waitUntilElementIsDisplayed(LOGIN_BTN);
        findElement(USERNAME).clear();
        findElement(USERNAME).sendKeys(userName);
        findElement(PSSWD).clear();
        findElement(PSSWD).sendKeys(password);
        findElement(LOGIN_BTN).click();
    }

    public void checkLoginContainsErrorMessage(){
        waitUntilElementIsDisplayed(LOGIN_ERROR_MESSAGE_ELEMENT);
        assertEquals(LOGIN_ERROR_MESSAGE_TEXT, findElement(LOGIN_ERROR_MESSAGE_ELEMENT).getText());
    }


}
