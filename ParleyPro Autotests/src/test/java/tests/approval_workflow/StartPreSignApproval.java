package tests.approval_workflow;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import forms.*;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import utils.Screenshoter;
import utils.Waiter;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class StartPreSignApproval
{
    private static Logger logger = Logger.getLogger(StartPreNegotiateApproval.class);

    @Test(priority = 1)
    @Description("This test moves document to negotiate stage")
    public void moveToNegotiate() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        StartNegotiation startNegotiationForm = openedContract.switchDocumentToNegotiate("pramata");
        startNegotiationForm.clickNext();

        EmailWillBeSentToTheCounterparty emailWillBeSentToTheCounterpartyForm = startNegotiationForm.clickNext();
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

        ConfirmApprovers confirmApproversForm = openedContract.switchDocumentToPreSignApproval("pramata");

        logger.info("Assert that we see Approval_User_2 as first approver and Team #2 as second approver...");
        $$(".document-approval__user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("A\nApproval_User_2 (arthur.khasanov+approval2@parleypro.com)", "Team #2\n2 members"));

        confirmApproversForm.clickStartApproval();

        Waiter.smartWaitUntilVisible("$('.document__header-row span[title]:contains(\"pramata\")').parent().parent().parent().next().find('.lifecycle:contains(\"APPROVAL\")')");

        logger.info("Assert that approvers icons are visible: Approval_User_2 and Team#2");
        $(".header-users .user").waitUntil(Condition.appear, 15_000); // wait until users icons will appear
        $$(".header-users .user").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("A")); // one User...
        $$(".header-users .team").shouldHave(CollectionCondition.size(1)); // ...and one Team

        Screenshoter.makeScreenshot();

        LoginPage loginPage = new DashboardPage().getSideBar().logout(); // Logout
    }

    @Test(priority = 3)
    @Description("This test logins as Team 2 member and verifies that he can’t approve")
    public void loginAsTeamMemeber2()
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
        $$(".header-users .user .user__checkbox").shouldHave(CollectionCondition.size(1)); // user has checkmark

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 7)
    @Description("This test opens Audit trail and checks that all messages are correct")
    public void checkAuditTrail()
    {
        OpenedContract openedContract = new OpenedContract();

        AuditTrail auditTrailPage = openedContract.clickAuditTrail();

        logger.info("Assert that all messages are correct");
        String actual = Selenide.executeJavaScript("return $('.timeline-heading h4').text()");
        Assert.assertEquals(actual, "Approval completedApproved by teamTeam request sentApprovedUser approve request was " +
                "sentPre-signature approval startedDocument user assignedDocument user assignedDocument user assignedOnline negotiation " +
                "startedApproval completedApprovedUser approve request was sentApprovedUser approve request was sentPre-negotiation approval " +
                "startedDocument user assignedDocument user assignedDocument uploadedContract created");

        Screenshoter.makeScreenshot();

        auditTrailPage.clickOk();
    }

    @Test(priority = 8)
    @Description("This test move document to Sign stage and complete sign")
    public void moveAndCompleteSign()
    {
        OpenedContract openedContract = new OpenedContract();

        SignContract signContractForm = openedContract.switchDocumentToSign("pramata");

        try
        {
            signContractForm.clickStart();

            logger.info("Assert that file was downloaded...");

            new WebDriverWait(WebDriverRunner.getWebDriver(), 20).
                    until(d -> Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), "pramata.pdf").toFile().exists());
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

        logger.info("Assert that status was changed to MANAGED...");
        $(".lifecycle").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("MANAGED"));

        Screenshoter.makeScreenshot();
    }
}
