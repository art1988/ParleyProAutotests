package tests.approval_workflow.reject;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.RejectDocument;
import io.qameta.allure.Description;
import model.AuditTrailEvent;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.*;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsApproverAndReject
{
    private static Logger logger = Logger.getLogger(LoginAsApproverAndReject.class);

    @Test(priority = 1)
    @Description("This test logins as PREDEFINED_APPROVER_USER_1 and make reject")
    public void loginAsApproverAndMakeReject() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_APPROVER_USER_1.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_APPROVER_USER_1.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");
        DashboardPage dashboardPage = loginPage.clickSignIn();
        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(false);

        inProgressContractsPage.selectContract("Approval rejection contract");
        logger.info("Checking that [Approve] and [Reject] buttons are present...");

        Thread.sleep(3_000);

        // According to jQuery documentation if length == 0 it means that element doesn't exist
        boolean approveButtonDoesntExist = Selenide.executeJavaScript("return ($('#APPROVE_DOCUMENT').length === 0)"),
                rejectButtonDoesntExist  = Selenide.executeJavaScript("return ($('#REJECT_DOCUMENT').length === 0)");
        Assert.assertFalse(approveButtonDoesntExist && rejectButtonDoesntExist);

        RejectDocument rejectDocumentForm = new OpenedContract().clickRejectButton("pramata");

        logger.info("Assert that REJECT button is disabled without comment...");
        Assert.assertTrue($(".button.btn-common.btn.btn-primary").is(Condition.disabled));
        Assert.assertTrue(Selenide.executeJavaScript("return $('.button.btn-common.btn.btn-primary')[0].hasAttribute(\"disabled\")"));

        String rejectComment = "This is simple comment to make reject button available.";
        rejectDocumentForm.setComments(rejectComment);
        rejectDocumentForm.clickReject();

        Screenshoter.makeScreenshot();

        logger.info("Assert reject notification...");
        $(".notification-stack").waitUntil(Condition.visible, 25_000).shouldHave(Condition.exactText("Document pramata has been rejected\nReason: " + rejectComment));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        logger.info("Check that contract was removed from in-progress list...");
        $(".contracts__empty-greetings").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Welcome to your contracts!"));
        $(".contracts__empty-message").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("You haven't been assigned a role in any contracts.\nYou'll receive a notification when you're assigned a role."));

        dashboardPage.getSideBar().logout();
    }

    @Test(priority = 2)
    @Description("This test logins back as CN and check that document status was switched back to review")
    public void loginBackAsCN()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        loginPage.clickSignIn();

        InProgressContractsPage inProgressContractsPage = new DashboardPage().getSideBar().clickInProgressContracts(false);
        inProgressContractsPage.selectContract("Approval rejection contract");

        logger.info("Assert that document status was switched back to review...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        Screenshoter.makeScreenshot();

        logger.info("Checking audit trail events...");
        AuditTrail auditTrail = new OpenedContract().clickAuditTrail();

        List<AuditTrailEvent> actualEvents = auditTrail.collectAllEvents();

        List<AuditTrailEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new AuditTrailEvent("Pre-negotiation approval rejected"));
        expectedEvents.add(new AuditTrailEvent("User approve request was sent"));
        expectedEvents.add(new AuditTrailEvent("Pre-negotiation approval started"));
        expectedEvents.add(new AuditTrailEvent("Document user assigned"));
        expectedEvents.add(new AuditTrailEvent("Document uploaded"));
        expectedEvents.add(new AuditTrailEvent("Contract created"));

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualEvents, expectedEvents, "Some events are missing !!! \n Actual events:" + actualEvents + " \n Expected events:" + expectedEvents);
        softAssert.assertAll();

        auditTrail.clickOk();

        logger.info("Checking CN e-mail...");
        Assert.assertTrue(EmailChecker.assertEmailBySubject("pop.gmail.com", "arthur.khasanov@parleypro.com", "ParGd881", "Document \"pramata\" rejected by arthur.khasanov+approval1@parleypro.com"),
                "Email with subject: Document \"pramata\" rejected by arthur.khasanov+approval1@parleypro.com was not found !!!");
    }
}
