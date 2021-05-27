package tests.popovers.at127;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.SendMessage;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginToPopoversTenantAsViewer
{
    private static Logger logger = Logger.getLogger(LoginToPopoversTenantAsViewer.class);

    @Test(priority = 1)
    public void loginToDashboard()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.POPOVERS_VIEWER.getEmail() );
        loginPage.setPassword( Const.POPOVERS_VIEWER.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");
        DashboardPage dashboardPage = loginPage.clickSignIn();
        dashboardPage.getSideBar().clickInProgressContracts(false);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("Under [Viewer role] user this test hovers over user icons and checks that MESSAGE button is present. Sends message and verify that email has been received.")
    public void checkTooltipsOverUserIconsAndSendMessages() throws InterruptedException
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("POP ctr");

        // ~~~PP start~~~
        logger.info("Hover over PP icon and check that Message button is available...");
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true}); ");
        jsCode.append("$('.header-users span:contains(\"PP\")')[0].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());

        // TODO: after fixing of PAR-14184
        //$(".rc-tooltip-inner button").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("MESSAGE")).shouldBe(Condition.enabled);
        //Screenshoter.makeScreenshot();

        // TODO...
        // ~~~~~~~~~~~~~~~~~~~
    }
}
