package com.parley.testing;

import com.parley.testing.listener.UITestListener;
import com.parley.testing.pages.impl.LoginPage;
import com.parley.testing.runner.AbstractIT;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Test(groups = "dashboard")
//@Listeners(UITestListener.class)
public class LoginTest extends AbstractIT {


    @Test
    public void testSuccessfulLogin() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria@parleypro.com","Parley650!");

    }

    @Test
    public void testInvalidPasswordLogin() throws Throwable {
//        throw new SkipException("message");
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria@parleypro.com","Test123");
        loginPage.checkLoginContainsErrorMessage();

    }

    @Test
    public void testInvalidUsernameLogin() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("1111@parleypro.com","Test123");
        loginPage.checkLoginContainsErrorMessage();
    }
}
