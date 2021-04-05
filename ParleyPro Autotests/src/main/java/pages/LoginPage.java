package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import constants.SideBarItems;
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
        $(".auth").waitUntil(Condition.visible, 20_000);

        emailField.waitUntil(Condition.visible, 20_000);
        passwordField.waitUntil(Condition.visible, 20_000);
        signInButton.waitUntil(Condition.visible, 20_000);

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

    /**
     * Use this method if you are sure that no Select tenant will appear afterwards.
     * Setup default side bar.
     * @return
     */
    public DashboardPage clickSignIn()
    {
        logger.info("Trying to login as: " + emailField.getValue());

        signInButton.click();

        logger.info("Sign In button was clicked");

        return new DashboardPage();
    }

    /**
     * Use this method to Sign In and setup custom sidebar.
     * @param sideBarItems items of sidebar that should be present on page
     * @return
     */
    public DashboardPage clickSignIn(SideBarItems[] sideBarItems)
    {
        logger.info("Trying to login as: " + emailField.getValue());

        signInButton.click();

        logger.info("Sign In button was clicked");

        return new DashboardPage(sideBarItems);
    }

    /**
     * Use this method if you are sure that Select tenant will appear on the next page
     * @param withSelectTenant just marker. Has no meaning
     * @return
     */
    public SelectTenantPage clickSignIn(boolean withSelectTenant)
    {
        logger.info("Trying to login as: " + emailField.getValue());

        signInButton.click();

        logger.info("Sign In button was clicked");

        return new SelectTenantPage();
    }
}
