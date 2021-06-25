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
import pages.OpenedContract;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginToPopoversTenantAsViewer
{
    private static Logger logger = Logger.getLogger(LoginToPopoversTenantAsViewer.class);

    private String host = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

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
    @Description("Under [Viewer role] user (VL) this test hovers over user icons and checks that MESSAGE button is present. Sends message and verify that email has been received.")
    public void checkTooltipsOverUserIconsAndSendMessages() throws InterruptedException
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("POP ctr");
        new OpenedContract();

        // ~~~ CCN start (PP) ~~~
        logger.info("Hover over PP icon and check that Message button is available...");
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true}); ");
        jsCode.append("$('.header-users span:contains(\"PP\")')[0].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());

        $(".rc-tooltip-inner button").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("MESSAGE")).shouldBe(Condition.enabled);
        Screenshoter.makeScreenshot();

        // Sending message
        $(".rc-tooltip-inner button").click();
        new SendMessage("POP CCN fn POP CCN ln").setMessage("Message from Viewer to CCN").clickSend();
        $(".notification-stack").waitUntil(Condition.appear, 10_000).shouldHave(Condition.exactText("Message has been successfully sent"));
        $(".notification-stack").waitUntil(Condition.disappear, 20_000);

        logger.info("Waiting for 50 seconds to make sure that email has been delivered...");
        Thread.sleep(50_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Contract \"POP ctr\": new message"),
                "Email with subject: Contract \"POP ctr\": new message was not found !!!");
        EmailChecker.assertEmailBodyText("*Message:* Message from Viewer to CCN");
        // ~~~~~~~~~~~~~~~~~~~

        // ~~~ (My team member) start (AL) ~~~
        logger.info("Hover over VL icon and check that Message button is available...");
        jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true}); ");
        jsCode.append("$('.header-users span:contains(\"AL\")')[0].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());

        $(".rc-tooltip-inner button").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("MESSAGE")).shouldBe(Condition.enabled);
        Screenshoter.makeScreenshot();

        // Sending message
        $(".rc-tooltip-inner button").click();
        new SendMessage("autotest_cn fn ln").setMessage("Message from Viewer to AL").clickSend();
        $(".notification-stack").waitUntil(Condition.appear, 10_000).shouldHave(Condition.exactText("Message has been successfully sent"));
        $(".notification-stack").waitUntil(Condition.disappear, 20_000);

        logger.info("Waiting for 50 seconds to make sure that email has been delivered...");
        Thread.sleep(50_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Contract \"POP ctr\": new message"),
                "Email with subject: Contract \"POP ctr\": new message was not found !!!");
        EmailChecker.assertEmailBodyText("*Message:* Message from Viewer to AL");
        // ~~~~~~~~~~~~~~~~~~~
    }
}
