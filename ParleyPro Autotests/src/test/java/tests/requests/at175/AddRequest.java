package tests.requests.at175;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import constants.SideBarItems;
import forms.ContractRequest;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.LoginPage;

import static com.codeborne.selenide.Selenide.$$;
import static constants.SideBarItems.*;


public class AddRequest
{
    private DashboardPage dashboardPage;
    private static Logger logger = Logger.getLogger(AddRequest.class);


    @Test(priority = 1)
    public void addRequest()
    {
        logger.info("Login as requester...");
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{PRIORITY_DASHBOARD, IN_PROGRESS_CONTRACTS, EXECUTED_CONTRACTS, DASHBOARD});

        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(true);
        ContractRequest contractRequest = inProgressContractsPage.clickNewRequestButton();

        contractRequest.setRequestTitle("AT-175 request");
        contractRequest.setValueForSelect("Who is CN", "CN1");
        contractRequest.clickSubmitRequest();

        $$(".contracts-list__table .contracts-list__contract-name").shouldHave(CollectionCondition.size(1))
                                                                            .shouldHave(CollectionCondition.exactTexts("AT-175 request"));
    }

    @Test(priority = 2)
    public void logoutAsRequester()
    {
        dashboardPage.getSideBar().logout();
    }
}
