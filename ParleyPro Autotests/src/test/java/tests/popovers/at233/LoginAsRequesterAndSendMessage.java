package tests.popovers.at233;

import com.codeborne.selenide.Condition;
import constants.Const;
import constants.SideBarItems;
import forms.SendMessage;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static constants.SideBarItems.*;
import static constants.SideBarItems.DASHBOARD;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsRequesterAndSendMessage
{
    private static Logger logger = Logger.getLogger(LoginAsRequesterAndSendMessage.class);


    @Test
    public void loginAsRequesterAndSendMessage() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );
        DashboardPage dashboardPage = loginPage.clickSignIn(new SideBarItems[]{ PRIORITY_DASHBOARD, IN_PROGRESS_CONTRACTS, EXECUTED_CONTRACTS, DASHBOARD});

        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract("Request_for_AT223");

        logger.info("Hover over CN’s avatar...");
        $$(".contract-header-users .user").filterBy(Condition.exactText("AL")).first().hover();
        $(".rc-tooltip-content").should(Condition.appear);
        $(".rc-tooltip-inner .spinner").should(Condition.disappear);

        // Sending message
        $(".rc-tooltip-content button").shouldBe(Condition.visible, Condition.enabled).shouldHave(Condition.text("MESSAGE")).click();
        new SendMessage("autotest_cn fn ln").setMessage("AT-233 // Message from requester to CN").clickSend();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Message has been successfully sent"));
        $(".notification-stack").should(Condition.disappear);

        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Contract \"Request_for_AT223\": new message"),
                "Email with subject: Contract \"POP ctr\": new message was not found !!!");
        EmailChecker.assertEmailBodyText("*Message:* AT-233 // Message from requester to CN");

        dashboardPage.getSideBar().logout();
    }
}
