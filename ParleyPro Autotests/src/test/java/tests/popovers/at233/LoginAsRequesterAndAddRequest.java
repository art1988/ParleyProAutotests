package tests.popovers.at233;

import com.codeborne.selenide.Condition;
import constants.Const;
import constants.SideBarItems;
import forms.ContractRequest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;

import java.io.File;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static constants.SideBarItems.*;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsRequesterAndAddRequest
{
    @Test
    public void loginAsRequesterAndAddRequest() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );
        DashboardPage dashboardPage = loginPage.clickSignIn(new SideBarItems[]{ PRIORITY_DASHBOARD, IN_PROGRESS_CONTRACTS, EXECUTED_CONTRACTS, DASHBOARD});

        ContractRequest contractRequest = new InProgressContractsPage(true).clickNewRequestButton();
        contractRequest.setRequestTitle("Request_for_AT223");
        contractRequest.selectValueForField("AT-233_trigger_field", "a");
        contractRequest.uploadMyTeamDocuments(new File[]{Const.DOCUMENT_DISCUSSIONS_SAMPLE});
        contractRequest.clickSubmitRequest();

        $(byText("Request_for_AT223")).shouldBe(Condition.visible);

        dashboardPage.getSideBar().logout();
    }
}
