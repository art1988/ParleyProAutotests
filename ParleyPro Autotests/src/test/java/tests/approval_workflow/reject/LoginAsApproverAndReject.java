package tests.approval_workflow.reject;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.RejectDocument;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

public class LoginAsApproverAndReject
{
    private static Logger logger = Logger.getLogger(LoginAsApproverAndReject.class);

    @Test(priority = 1)
    public void loginAsApproverAndMakeReject() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_APPROVER_USER_1.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_APPROVER_USER_1.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");
        DashboardPage dashboardPage = loginPage.clickSignIn();
        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(false);

        inProgressContractsPage.selectContract("Approval rejection contract");
        OpenedContract openedContract = new OpenedContract();
        logger.info("Assert that Approve and Reject buttons are not present...");

        Thread.sleep(3_000);

        // According to jQuery documentation if length == 0 it means that element doesn't exist
        boolean approveButtonDoesntExist = Selenide.executeJavaScript("return ($('#APPROVE_DOCUMENT').length === 0)"),
                rejectButtonDoesntExist  = Selenide.executeJavaScript("return ($('#REJECT_DOCUMENT').length === 0)");
        Assert.assertFalse(approveButtonDoesntExist && rejectButtonDoesntExist);

        RejectDocument rejectDocument = openedContract.clickRejectButton("pramata");

        logger.info("Assert that REJECT button is disabled without comment...");
        Assert.assertTrue($(".button.btn-common.btn.btn-primary").is(Condition.disabled));
        Assert.assertTrue(Selenide.executeJavaScript("return $('.button.btn-common.btn.btn-primary')[0].hasAttribute(\"disabled\")"));
    }
}
