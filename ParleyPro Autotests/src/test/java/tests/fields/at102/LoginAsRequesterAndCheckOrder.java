package tests.fields.at102;

import com.codeborne.selenide.Selenide;
import constants.Const;
import constants.SideBarItems;
import forms.ContractRequest;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static constants.SideBarItems.*;

@Listeners({ScreenShotOnFailListener.class})
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
                "linked2ff1ff2linked1",
                "The order of contract request fields ff1, ff2, linked1 and linked2 is wrong !!!");

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
