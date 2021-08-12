package tests.priority_dashboard.at155;

import com.codeborne.selenide.CollectionCondition;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.PriorityDashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractFromPDAndCheck
{
    private static Logger logger = Logger.getLogger(CreateContractFromPDAndCheck.class);

    @Test
    @Description("This test creates new contract from Priority Dashboard page that goes to In-progress contracts.")
    public void createContractFromPriorityDashboardAndCheck()
    {
        PriorityDashboardPage priorityDashboardPage = new DashboardPage().getSideBar().clickPriorityDashboard();

        ContractInformation contractInformation = priorityDashboardPage.clickNewContractButtonForInProgress();

        contractInformation.setContractTitle("Contract for in-progress created from PD");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");

        contractInformation.clickSave();

        logger.info("Returning back to Priority Dashboard page and check contract counter for in-progress...");
        priorityDashboardPage = new DashboardPage().getSideBar().clickPriorityDashboard();

        Assert.assertEquals(priorityDashboardPage.getCountOfAllInProgressContracts(), "1", "Looks like that counter for 'All in-progress contracts' is wrong !!!");
        Assert.assertEquals(priorityDashboardPage.getCountOfAllExecutedContracts(), "0", "Looks like that counter for 'All executed contracts' is wrong !!!"); // and executed counter should be still 0

        logger.info("Going to in-progress page to make sure that contract was added...");
        InProgressContractsPage inProgressContractsPage = new DashboardPage().getSideBar().clickInProgressContracts(false);

        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("Contract for in-progress created from PD"));

        Screenshoter.makeScreenshot();
    }
}
