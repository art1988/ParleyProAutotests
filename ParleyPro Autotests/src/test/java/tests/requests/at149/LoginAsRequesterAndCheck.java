package tests.requests.at149;

import constants.Const;
import forms.ContractRequest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsRequesterAndCheck
{
    @Test
    public void loginAsRequesterAndAddRequest()
    {
        // Logout first
        LoginPage loginPage = new DashboardPage().getSideBar().logout();

        // Login as REQUESTER
        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        ContractRequest contractRequest = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewRequestButton();
    }
}
