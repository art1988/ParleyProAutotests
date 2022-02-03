package tests.requests.at171;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import constants.SideBarItems;
import forms.ContractRequest;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.LoginPage;
import utils.Screenshoter;

import java.io.File;

import static com.codeborne.selenide.Selenide.$$;
import static constants.SideBarItems.*;


public class LoginAsRequesterAndCreateTwoRequests
{
    private DashboardPage dashboardPage;
    private static Logger logger = Logger.getLogger(LoginAsRequesterAndCreateTwoRequests.class);

    @Test(priority = 1)
    @Description("This test creates two requests.")
    public void loginAsRequesterAndCreateTwoRequests() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{ PRIORITY_DASHBOARD, IN_PROGRESS_CONTRACTS, EXECUTED_CONTRACTS, DASHBOARD});

        logger.info("Making first request...");
        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(true);
        ContractRequest contractRequest = inProgressContractsPage.clickNewRequestButton();

        contractRequest.selectValueForField("reqField", "val1");
        contractRequest.clickSubmitRequest();
        $$(".contract-item .contracts-list__contract-name").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("Contract request"));

        logger.info("Making second request...");
        contractRequest = inProgressContractsPage.clickNewRequestButton();

        contractRequest.selectValueForField("reqField", "val1");
        contractRequest.uploadCounterpartyDocuments(new File[]{ Const.DOCUMENT_CLASSIC_AT40 });
        contractRequest.clickSubmitRequest();

        logger.info("Making sure that both requests were created and are displayed...");
        $$(".contract-item .contracts-list__contract-name").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("Contract request", "Contract request"));
        $$(".label_theme_purple").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REQUEST", "REQUEST"));
        $$(".contract-status").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REQUEST", "REQUEST"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logoutAsRequester()
    {
        logger.info("Logout as Requester...");
        dashboardPage.getSideBar().logout();
    }
}
