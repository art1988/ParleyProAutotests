package pages;

import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage
{
    private SelenideElement emailField = $("#email");
    private SelenideElement passwordField = $("#password");
    private SelenideElement signInButton = $("button[type='submit']");


    private static Logger logger = Logger.getLogger(LoginPage.class);


    public LoginPage()
    {
        Assert.assertTrue(isInit());
    }

    private boolean isInit()
    {
        boolean hasLogo = $(".auth__head").getCssValue("background").contains("images/cc8124f8be69a02e7221cfaabe5a0ef1.svg");

        return ( hasLogo && emailField.isDisplayed() && passwordField.isDisplayed() && signInButton.isDisplayed() );
    }

    public void setEmail(String email)
    {
        emailField.clear();
        emailField.setValue(email);
    }

    public void setPassword(String password)
    {
        passwordField.clear();
        passwordField.setValue(password);
    }

    public DashboardPage clickSignIn()
    {
        logger.info("Trying to login as: " + emailField.getValue());

        signInButton.click();

        logger.info("Sign In button was clicked");

        return new DashboardPage();
    }
}
