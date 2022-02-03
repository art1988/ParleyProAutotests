package tests.fields.at96_99_100;

import com.codeborne.selenide.Selenide;
import constants.Const;
import constants.SideBarItems;
import forms.ContractRequest;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.Screenshoter;

import static constants.SideBarItems.*;


public class LoginAsRequesterAndCheckOrder
{
    private DashboardPage dashboardPage;
    private static Logger logger = Logger.getLogger(LoginAsRequesterAndCheckOrder.class);

    @Test(priority = 1)
    public void loginAsRequesterAndCheckOrder()
    {
        // Admin logout
        LoginPage loginPage = new DashboardPage().getSideBar().logout();

        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{PRIORITY_DASHBOARD, IN_PROGRESS_CONTRACTS, EXECUTED_CONTRACTS, DASHBOARD});

        ContractRequest contractRequestForm = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewRequestButton();

        logger.info("Check contract request fields...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.input__label-title').text()"),
                "Request titlecr_f2cr_f1",
                "The order of contract request fields cr_f1 and cr_f2 is wrong !!!");

        Screenshoter.makeScreenshot();

        contractRequestForm.clickCancel();
    }

    @Test(priority = 2)
    public void loginBackAsAdmin()
    {
        // Requester logout
        LoginPage loginPage = dashboardPage.getSideBar().logout();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        loginPage.clickSignIn();
    }
}
