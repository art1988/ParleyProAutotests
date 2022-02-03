package tests.requests.at184;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractRequest;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;

import static com.codeborne.selenide.Selenide.$;


public class CreateRequest
{
    @Test
    public void createRequest()
    {
        // Logout as CN
        LoginPage loginPage = new DashboardPage().getSideBar().logout();

        // Login as REQUESTER
        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        ContractRequest contractRequest = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewRequestButton();

        contractRequest.setRequestTitle("Request_CP_at184");
        contractRequest.selectValueForField("ReqField_AT184", "val3");
        contractRequest.clickSubmitRequest();

        $(".notification-stack").shouldHave(Condition.text("Your contract request has been submitted."));

        // Logout as REQUESTER
        dashboardPage.getSideBar().logout();
    }
}
