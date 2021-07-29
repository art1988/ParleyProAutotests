package tests.popovers.at127;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import constants.SideBarItems;
import forms.SendMessage;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import tests.LoginBase;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Listeners({ScreenShotOnFailListener.class})
public class LoginToPopoversTenantAsCCN extends LoginBase
{
    private String host = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private DashboardPage dashboardPage;
    private Logger logger = Logger.getLogger(LoginToPopoversTenantAsCCN.class);

    @Test(priority = 1)
    public void loginToDashboard()
    {
        // Since there is it's own tenant for CCN, login to app.parleypro.net => should redirect to counterpartyat1.parleypro.net after sign in
        if( LoginBase.isRC )
        {
            open( "https://app.parleypro.net/rc/index.html" );
        }
        else
        {
            open( "https://app.parleypro.net/master/index.html" );
        }

        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.POPOVERS_CCN.getEmail() );
        loginPage.setPassword( Const.POPOVERS_CCN.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS});

        Assert.assertTrue( WebDriverRunner.getWebDriver().getCurrentUrl().startsWith("https://counterpartyat1.parleypro.net") ||
                                    WebDriverRunner.getWebDriver().getCurrentUrl().startsWith("https://counterpartyat2.parleypro.net"),
                "Looks like that tenant name is wrong !!! Should be counterpartyat1/counterpartyat2 !!!");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("Under [Counterparty CN] user (PP) this test hovers over user icons and checks that MESSAGE button is present. Sends message and verify that email has been received.")
    public void checkTooltipsOverUserIconsAndSendMessages() throws InterruptedException
    {
        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract("POP ctr");
        new OpenedContract();

        // ~~~ AL start (AL) ~~~
        logger.info("Hover over AL icon and check that Message button is available...");
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true}); ");
        jsCode.append("$('.header-users span:contains(\"AL\")')[0].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());
        $(".rc-tooltip-inner button").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("MESSAGE")).shouldBe(Condition.enabled);
        Screenshoter.makeScreenshot();

        // Sending message
        $(".rc-tooltip-inner button").click();
        new SendMessage("autotest_cn fn ln").setMessage("Message to MyTeamCN").clickSend();
        $(".notification-stack").waitUntil(Condition.appear, 10_000).shouldHave(Condition.exactText("Message has been successfully sent"));
        $(".notification-stack").waitUntil(Condition.disappear, 20_000);

        logger.info("Waiting for 50 seconds to make sure that email has been delivered...");
        Thread.sleep(50_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Contract \"POP ctr\": new message"),
                "Email with subject: Contract \"POP ctr\": new message was not found !!!");
        EmailChecker.assertEmailBodyText("*Message:* Message to MyTeamCN");
        // ~~~~~~~~~~~~~~~~~~~


        // ~~~ CC start (CC) ~~~
        logger.info("Hover over CC icon and check that Message button is available...");
        jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true}); ");
        jsCode.append("$('.header-users span:contains(\"CC\")')[0].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());
        $(".rc-tooltip-inner button").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("MESSAGE")).shouldBe(Condition.enabled);
        Screenshoter.makeScreenshot();

        // Sending message
        $(".rc-tooltip-inner button").click();
        new SendMessage("CCN Lead fn CCN Lead ln").setMessage("Message to CCN #2").clickSend();
        $(".notification-stack").waitUntil(Condition.appear, 10_000).shouldHave(Condition.exactText("Message has been successfully sent"));
        $(".notification-stack").waitUntil(Condition.disappear, 20_000);

        logger.info("Waiting for 50 seconds to make sure that email has been delivered...");
        Thread.sleep(50_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Contract \"POP ctr\": new message"),
                "Email with subject: Contract \"POP ctr\": new message was not found !!!");
        EmailChecker.assertEmailBodyText("*Message:* Message to CCN #2");
        // ~~~~~~~~~~~~~~~~~~~
    }

    @Test(priority = 3)
    public void logout()
    {
        dashboardPage.getSideBar().logout();
    }
}
