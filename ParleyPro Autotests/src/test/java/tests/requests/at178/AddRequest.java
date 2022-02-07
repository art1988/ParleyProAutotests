package tests.requests.at178;

import com.codeborne.selenide.Condition;
import constants.Const;
import constants.SideBarItems;
import forms.ContractRequest;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static constants.SideBarItems.*;

@Listeners({ScreenShotOnFailListener.class})
public class AddRequest
{
    private DashboardPage dashboardPage;
    private static Logger logger = Logger.getLogger(AddRequest.class);

    @Test(priority = 1)
    public void loginAsRequesterAndAddRequest() throws InterruptedException
    {
        logger.info("Login as Requester...");

        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{ PRIORITY_DASHBOARD, IN_PROGRESS_CONTRACTS, EXECUTED_CONTRACTS, DASHBOARD});

        logger.info("Making request...");
        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(true);
        ContractRequest contractRequest = inProgressContractsPage.clickNewRequestButton();

        contractRequest.selectValueForField("REQ1", "Yes");
        contractRequest.uploadCounterpartyDocuments(new File[]{ Const.DOCUMENT_DISCUSSIONS_SAMPLE });
        $(".upload-field__file-name").waitUntil(Condition.visible, 10_000).shouldHave(Condition.text("AT-14.docx"));
        contractRequest.clickSubmitRequest();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Your contract request has been submitted."));
    }

    @Test(priority = 2)
    public void logoutAsRequester()
    {
        logger.info("Logout as Requester...");
        dashboardPage.getSideBar().logout();
    }
}
