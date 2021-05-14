package tests.requests.at124;

import constants.Const;
import forms.ContractRequest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsRequesterAndSubmitRequest
{
    @Test(priority = 1)
    public void loginAsRequester() throws InterruptedException
    {
        // Logout first
        LoginPage loginPage = new DashboardPage().getSideBar().logout();

        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        ContractRequest contractRequest = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewRequestButton();

        contractRequest.setValueForSelect("Contracting region", "region1");
        contractRequest.setValueForSelect("Contracting country", "country2");
        contractRequest.setValueForSelect("Contract entity", "entity1");
        contractRequest.setValueForSelect("Contracting department", "department2");
        contractRequest.setValueForSelect("Contract category", "category2");
        contractRequest.setContractType("type3");

        Thread.sleep(10_000);
    }
}
