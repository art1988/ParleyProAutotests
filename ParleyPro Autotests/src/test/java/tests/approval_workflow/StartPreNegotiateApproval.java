package tests.approval_workflow;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ConfirmApprovers;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ ScreenShotOnFailListener.class})
public class StartPreNegotiateApproval
{
    private static Logger logger = Logger.getLogger(StartPreNegotiateApproval.class);

    @Test(priority = 1)
    @Description("This test starts pre-negotiation approval. The order of approvers are: Approval_User_1, Approval_User_2")
    public void startPreNegotiateApproval() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        ConfirmApprovers confirmApproversForm = openedContract.switchDocumentToPreNegotiateApproval("pramata");

        logger.info("Assert that we see one team: Autotest_TEAM_3 and one user: Approval_User_1");
        String listOfApprovers = confirmApproversForm.getListOfApprovers();
        Assert.assertTrue(listOfApprovers.contains("Autotest_TEAM_3 [EDITED]") && listOfApprovers.contains("Approval_User_1"));

        confirmApproversForm.switchTumblerSetApprovalOrder();
        confirmApproversForm.addParticipant("Approval_User_2");
        confirmApproversForm.deleteApprover("TEAM");
        confirmApproversForm.clickStartApproval();

        $(".header-users .user").waitUntil(Condition.appear, 25_000); // wait until users icons will appear
        Thread.sleep(2_000);

        logger.info("Assert that status was changed to APPROVAL for both contract and document...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));

        logger.info("Assert that approvers icons are visible...");
        $(".header-users .user").waitUntil(Condition.appear, 25_000); // wait until users icons will appear
        $$(".header-users .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("Ar", "Ar"));
        $$(".contract-header-users .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.exactTexts("AL", "Ar", "Ar"));

        Screenshoter.makeScreenshot();

        LoginPage loginPage = new DashboardPage().getSideBar().logout(); // Logout
    }

    @Test(priority = 2)
    @Description("This test logins as Approval_User_2 and verifies that he can’t approve")
    public void loginAsApproverUser2AndCheckThatApproveNotVisible() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_APPROVER_USER_2.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_APPROVER_USER_2.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");
        DashboardPage dashboardPage = loginPage.clickSignIn();
        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(false);

        inProgressContractsPage.selectContract("Approval workflow positive");
        OpenedContract openedContract = new OpenedContract();
        logger.info("Assert that Approve and Reject buttons are not present...");

        Thread.sleep(3_000);

        // According to jQuery documentation if length == 0 it means that element doesn't exist
        boolean approveButtonDoesntExist = Selenide.executeJavaScript("return ($('#APPROVE_DOCUMENT').length === 0)"),
                rejectButtonDoesntExist  = Selenide.executeJavaScript("return ($('#REJECT_DOCUMENT').length === 0)");
        Assert.assertTrue(approveButtonDoesntExist && rejectButtonDoesntExist);

        Screenshoter.makeScreenshot();

        loginPage = new DashboardPage().getSideBar().logout(); // Logout
    }

    @Test(priority = 3)
    @Description("This test logins as Approval_User_1 and approves document")
    public void loginAsApproverUser1AndApprove()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_APPROVER_USER_1.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_APPROVER_USER_1.getPassword() );

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
        $$(".header-users .user .user-icon-checked").shouldHave(CollectionCondition.size(2)); // checkmarks checks

        Screenshoter.makeScreenshot();
    }
}
