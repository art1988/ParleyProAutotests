package tests.approval_workflow;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import forms.*;
import forms.workflows.ApprovalWorkflow;
import io.qameta.allure.Description;
import model.AuditTrailEvent;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.*;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ ScreenShotOnFailListener.class})
public class StartPreSignApproval
{
    private static Logger logger = Logger.getLogger(StartPreNegotiateApproval.class);

    @Test(priority = 1)
    @Description("This test moves document to negotiate stage")
    public void moveToNegotiate() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        StartNegotiation startNegotiationForm = openedContract.switchDocumentToNegotiate("pramata", "", false);
        startNegotiationForm.clickNext(false);

        EmailWillBeSentToTheCounterparty emailWillBeSentToTheCounterpartyForm = startNegotiationForm.clickNext(false);
        emailWillBeSentToTheCounterpartyForm.setCounterpartyOrganization("CounterpartyAT");
        Thread.sleep(1_000);
        emailWillBeSentToTheCounterpartyForm.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        emailWillBeSentToTheCounterpartyForm.clickStart();

        logger.info("Assert visible to the counterparty notification...");
        $(".notification-stack").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Contract Approval workflow positive is now visible to the Counterparty. The email was sent to arthur.khasanov+cpat@parleypro.com"));

        logger.info("Assert that status was changed to NEGOTIATE...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test starts pre-sign approval. Verifies that approvers list consist of Approval_User_2 and Team #2")
    public void startPreSignApproval()
    {
        OpenedContract openedContract = new OpenedContract();

        ConfirmApprovers confirmApproversForm = openedContract.switchDocumentToPreSignApproval("pramata").clickNext();

        logger.info("Assert that we see Approval_User_2 as first approver and Team #2 as second approver...");
        $$(".document-approval__user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("1\nAr\nApproval_User_2 (arthur.khasanov+approval2@parleypro.com)", "2\nTeam #2\n2 members"));

        confirmApproversForm.clickStartApproval();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));

        logger.info("Assert that approvers icons are visible: Approval_User_2 and Team#2");
        $(".header-users .user").should(Condition.appear);
        $(".header-users .team").should(Condition.appear);
        $$(".header-users .user").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("Ar")); // one User...
        $$(".header-users .team").shouldHave(CollectionCondition.size(1)); // ...and one Team

        Screenshoter.makeScreenshot();

        LoginPage loginPage = new DashboardPage().getSideBar().logout(); // Logout
    }

    @Test(priority = 3)
    @Description("This test logins as Team 2 member and verifies that he can’t approve")
    public void loginAsTeamMemeber2() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_INTERNAL_USER_2.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_INTERNAL_USER_2.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");
        DashboardPage dashboardPage = loginPage.clickSignIn();
        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(false);

        inProgressContractsPage.selectContract("Approval workflow positive");
        OpenedContract openedContract = new OpenedContract();

        logger.info("Assert that Approve and Reject buttons are not present...");

        Thread.sleep(3_000);

        boolean approveButtonDoesntExist = Selenide.executeJavaScript("return ($('#APPROVE_DOCUMENT').length === 0)"),
                rejectButtonDoesntExist  = Selenide.executeJavaScript("return ($('#REJECT_DOCUMENT').length === 0)");
        Assert.assertTrue(approveButtonDoesntExist && rejectButtonDoesntExist);

        Screenshoter.makeScreenshot();

        loginPage = new DashboardPage().getSideBar().logout(); // Logout
    }

    @Test(priority = 4)
    @Description("This test logins as Approval_User_2 and approves document")
    public void loginAsApproverUser2AndApprove()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_APPROVER_USER_2.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_APPROVER_USER_2.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");
        DashboardPage dashboardPage = loginPage.clickSignIn();
        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(false);

        inProgressContractsPage.selectContract("Approval workflow positive");
        OpenedContract openedContract = new OpenedContract();

        logger.info("Assert that Approve and Reject buttons are present...");
        $("#APPROVE_DOCUMENT").waitUntil(Condition.visible, 5_000).shouldBe(Condition.visible);
        $("#REJECT_DOCUMENT").waitUntil(Condition.visible, 5_000).shouldBe(Condition.visible);

        boolean approveButtonDoesntExist = Selenide.executeJavaScript("return ($('#APPROVE_DOCUMENT').length === 0)"),
                rejectButtonDoesntExist  = Selenide.executeJavaScript("return ($('#REJECT_DOCUMENT').length === 0)");
        Assert.assertFalse(approveButtonDoesntExist && rejectButtonDoesntExist);

        openedContract.clickApproveButton("pramata").clickApproveButton(); // Click approve

        Screenshoter.makeScreenshot();

        loginPage = new DashboardPage().getSideBar().logout(); // Logout
    }

    @Test(priority = 5)
    @Description("This test logins as Team 2 member and approves document")
    public void loginAsTeamMemeber2AndApprove()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_INTERNAL_USER_2.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_INTERNAL_USER_2.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");
        DashboardPage dashboardPage = loginPage.clickSignIn();
        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(false);

        inProgressContractsPage.selectContract("Approval workflow positive");
        OpenedContract openedContract = new OpenedContract();

        logger.info("Assert that Approve and Reject buttons are present...");
        $("#APPROVE_DOCUMENT").waitUntil(Condition.visible, 5_000).shouldBe(Condition.visible);
        $("#REJECT_DOCUMENT").waitUntil(Condition.visible, 5_000).shouldBe(Condition.visible);

        boolean approveButtonDoesntExist = Selenide.executeJavaScript("return ($('#APPROVE_DOCUMENT').length === 0)"),
                rejectButtonDoesntExist  = Selenide.executeJavaScript("return ($('#REJECT_DOCUMENT').length === 0)");
        Assert.assertFalse(approveButtonDoesntExist && rejectButtonDoesntExist);

        openedContract.clickApproveButton("pramata").clickApproveButton(); // Click approve

        Screenshoter.makeScreenshot();

        loginPage = new DashboardPage().getSideBar().logout(); // Logout
    }

    @Test(priority = 6)
    @Description("This test logins as Chief Negotiator ( under main user ) and verify approvers checkmarks")
    public void loginAsCNAndVerifyCheckmarks()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");
        DashboardPage dashboardPage = loginPage.clickSignIn();
        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(false);

        inProgressContractsPage.selectContract("Approval workflow positive");
        OpenedContract openedContract = new OpenedContract();

        logger.info("Assert that approvers icons have checkmarks...");
        $(".header-users .user").waitUntil(Condition.appear, 10_000); // wait until users icons will appear
        Assert.assertTrue(Selenide.executeJavaScript("return $('.team-icon.team').hasClass(\"_show-checkbox_yes\")")); // team has checkmark
        $$(".header-users .user .user-icon-checked").shouldHave(CollectionCondition.size(1)); // user has checkmark

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 7)
    @Description("This test opens Audit trail and checks that all messages are correct")
    public void checkAuditTrail() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        AuditTrail auditTrailPage = openedContract.clickAuditTrail();

        logger.info("Assert that all messages are correct");
        $(".timeline-heading h4").waitUntil(Condition.visible, 7_000);

        List<AuditTrailEvent> actualEvents = auditTrailPage.collectAllEvents();

        List<AuditTrailEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new AuditTrailEvent("Approval completed"));
        expectedEvents.add(new AuditTrailEvent("Approved by team"));
        expectedEvents.add(new AuditTrailEvent("Team request sent"));
        expectedEvents.add(new AuditTrailEvent("Approved"));
        expectedEvents.add(new AuditTrailEvent("User approve request was sent"));
        expectedEvents.add(new AuditTrailEvent("Pre-signature approval started"));
        expectedEvents.add(new AuditTrailEvent("Team assigned"));
        expectedEvents.add(new AuditTrailEvent("Document user assigned"));
        expectedEvents.add(new AuditTrailEvent("Online negotiation started"));
        expectedEvents.add(new AuditTrailEvent("Approval completed"));
        expectedEvents.add(new AuditTrailEvent("Approved"));
        expectedEvents.add(new AuditTrailEvent("User approve request was sent"));
        expectedEvents.add(new AuditTrailEvent("Approved"));
        expectedEvents.add(new AuditTrailEvent("User approve request was sent"));
        expectedEvents.add(new AuditTrailEvent("Pre-negotiation approval started"));
        expectedEvents.add(new AuditTrailEvent("Document user assigned"));
        expectedEvents.add(new AuditTrailEvent("Document user assigned"));
        expectedEvents.add(new AuditTrailEvent("Document uploaded"));
        expectedEvents.add(new AuditTrailEvent("Contract created"));

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(actualEvents, expectedEvents, "Some events are missing !!! \n Actual events:" + actualEvents + " \n Expected events:" + expectedEvents);
        softAssert.assertAll();

        Screenshoter.makeScreenshot();

        auditTrailPage.clickOk();
    }

    @Test(priority = 8)
    @Description("This test move document to Sign stage and complete sign")
    public void moveAndCompleteSign()
    {
        OpenedContract openedContract = new OpenedContract();

        SignContract signContractForm = openedContract.switchDocumentToSign("pramata", false);

        try
        {
            signContractForm.clickStart();

            logger.info("Assert that file was downloaded...");

            Assert.assertTrue(new WebDriverWait(WebDriverRunner.getWebDriver(), 20).
                    until(d -> Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), "pramata.pdf").toFile().exists()));
        }
        catch (FileNotFoundException e)
        {
            logger.error("FileNotFoundException", e);
        }

        // Wait until Complete Sign is visible...
        $("#COMPLETE_MANUAL_DOCUMENT").waitUntil(Condition.visible, 15_000);

        CompleteSign completeSignForm = openedContract.clickCompleteSign("pramata");
        ContractInfo signContractInfo = completeSignForm.clickComplete();

        signContractInfo.setSignatureDate();
        signContractInfo.setEffectiveDate();
        signContractInfo.clickSave();

        logger.info("Assert update notification...");
        $(".notification-stack").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Contract has been updated."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that status was changed to MANAGED...");
        $(".lifecycle").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("MANAGED"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 9)
    @Description("Check of PAR-12894: This test goes to workflow settings, removes 'Prior to Sign' condition add new user for Prior to negotiate saves workflow and validate saving")
    public void checkConditionRemovingInWorkflow() throws InterruptedException
    {
        DashboardPage dashboardPage = new DashboardPage();

        dashboardPage.getSideBar().clickAdministration().clickWorkflowsTab().clickActionMenu("Approval_WFL_AT").clickEdit();

        ApprovalWorkflow approvalWorkflow = new ApprovalWorkflow(true);
        approvalWorkflow.deleteCondition("Prior to Sign");
        approvalWorkflow.setPriorToNegotiateParticipant( Const.PREDEFINED_INTERNAL_USER_1.getFirstName() );
        approvalWorkflow.clickSave();

        logger.info("Edit workflow again and assert that changes were applied...");
        dashboardPage.getSideBar().clickAdministration().clickWorkflowsTab().clickActionMenu("Approval_WFL_AT").clickEdit();
        Thread.sleep(2_000);

        Screenshoter.makeScreenshot();

        boolean priorToSignExists = Selenide.executeJavaScript("return $('.workflows-approval-events-event__title:contains(\"Prior to Sign\")').length === 1");
        Assert.assertFalse(priorToSignExists);

        String usersOfPriorToNegotiateCondition = Selenide.executeJavaScript("return $('.workflows-approval-events-event__title:contains(\"Prior to Negotiate\")').parent().parent().find(\".workflows-approval-users-list .workflows-approval-users-list__item-name\").text()");
        Assert.assertTrue(usersOfPriorToNegotiateCondition.contains("Internal user1 Internal user1 last name (arthur.khasanov+team1@parleypro.com)") &&
                                   usersOfPriorToNegotiateCondition.contains("Autotest_TEAM_3 [EDITED] (3 members)") &&
                                   usersOfPriorToNegotiateCondition.contains("Approval_User_1 (arthur.khasanov+approval1@parleypro.com)"));

        approvalWorkflow.clickCancel();
    }
}
