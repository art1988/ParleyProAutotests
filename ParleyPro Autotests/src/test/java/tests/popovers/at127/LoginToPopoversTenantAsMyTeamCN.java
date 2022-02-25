package tests.popovers.at127;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.SendMessage;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.EmailChecker;
import utils.LoginBase;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Listeners({ScreenShotOnFailListener.class})
public class LoginToPopoversTenantAsMyTeamCN extends LoginBase
{
    private final static int MAX_RETRY_COUNT = 5;
    private static Logger logger = Logger.getLogger(LoginToPopoversTenantAsMyTeamCN.class);

    private String host = "pop.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    @Test(priority = 1)
    private void setup()
    {
        Configuration.proxyEnabled   = true;
        Configuration.fileDownload   = FileDownloadMode.PROXY;
        Configuration.startMaximized = true;
        Configuration.browserCapabilities.setCapability("acceptInsecureCerts", true);

        Const.DOWNLOAD_DIR.mkdirs();
        Const.SCREENSHOTS_DIR.mkdirs();

        Configuration.downloadsFolder = Const.DOWNLOAD_DIR.getAbsolutePath();
        Configuration.reportsFolder   = Const.SCREENSHOTS_DIR.getAbsolutePath();

        int retryCount = 0;
        while(true)
        {
            try
            {
                open( getPopoversTenantUrl() );
                break;
            }
            catch(WebDriverException e)
            {
                if( retryCount > MAX_RETRY_COUNT )
                {
                    throw new RuntimeException("Too many retries...", e);
                }

                logger.warn("encountered exception : ", e);
                logger.warn("Trying again...");

                retryCount++;
                try
                {
                    Thread.sleep(2_000);
                }
                catch (InterruptedException interruptedException)
                {
                    interruptedException.printStackTrace();
                }
                continue;
            }
        }
    }

    @Test(priority = 2)
    public void loginToDashboard()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.POPOVERS_MYTEAM_CN.getEmail() );
        loginPage.setPassword( Const.POPOVERS_MYTEAM_CN.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");
        DashboardPage dashboardPage = loginPage.clickSignIn();
        dashboardPage.getSideBar().clickInProgressContracts(false);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("Under [My Team CN] user (AL) this test hovers over user icons and checks that MESSAGE button is present. Sends message and verify that email has been received.")
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
        new SendMessage("POP CCN fn POP CCN ln").setMessage("Message to CCN #1").clickSend();
        $(".notification-stack").waitUntil(Condition.appear, 10_000).shouldHave(Condition.exactText("Message has been successfully sent"));
        $(".notification-stack").waitUntil(Condition.disappear, 20_000);

        logger.info("Waiting for 50 seconds to make sure that email has been delivered...");
        Thread.sleep(50_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Contract \"POP ctr\": new message"),
                "Email with subject: Contract \"POP ctr\": new message was not found !!!");
        EmailChecker.assertEmailBodyText("*Message:* Message to CCN #1");
        // ~~~~~~~~~~~~~~~~~~~


        // ~~~ Viewer start (VL) ~~~
        logger.info("Hover over Viewer icon and check that Message button is available...");
        jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true}); ");
        jsCode.append("$('.header-users span:contains(\"VL\")')[0].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());
        $(".rc-tooltip-inner button").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("MESSAGE")).shouldBe(Condition.enabled);
        Screenshoter.makeScreenshot();

        // Sending message
        $(".rc-tooltip-inner button").click();
        new SendMessage("Viewer fn ln").setMessage("Message to Viewer").clickSend();
        $(".notification-stack").waitUntil(Condition.appear, 10_000).shouldHave(Condition.exactText("Message has been successfully sent"));
        $(".notification-stack").waitUntil(Condition.disappear, 20_000);

        logger.info("Waiting for 50 seconds to make sure that email has been delivered...");
        Thread.sleep(50_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Contract \"POP ctr\": new message"),
                "Email with subject: Contract \"POP ctr\": new message was not found !!!");
        EmailChecker.assertEmailBodyText("*Message:* Message to Viewer");
        // ~~~~~~~~~~~~~~~~~~~
    }

    @Test(priority = 4)
    public void logout()
    {
        new DashboardPage().getSideBar().logout();
    }
}
