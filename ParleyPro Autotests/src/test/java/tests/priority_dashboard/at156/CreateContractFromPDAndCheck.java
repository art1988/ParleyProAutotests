package tests.priority_dashboard.at156;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.ExecutedContractsPage;
import pages.PriorityDashboardPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;


public class CreateContractFromPDAndCheck
{
    private static Logger logger = Logger.getLogger(CreateContractFromPDAndCheck.class);

    @Test
    @Description("This test creates new contract from Priority Dashboard page that goes to Executed contracts.")
    public void createContractFromPriorityDashboardAndCheck()
    {
        PriorityDashboardPage priorityDashboardPage = new DashboardPage().getSideBar().clickPriorityDashboard();

        ContractInformation contractInformation = priorityDashboardPage.clickNewContractButtonForExecuted();

        contractInformation.setContractTitle("Contract for executed created from PD");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");

        contractInformation.clickSave();

        new AddDocuments().clickUploadExecutedDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);

        logger.info("Returning back to Priority Dashboard page and check contract counter for executed...");
        priorityDashboardPage = new DashboardPage().getSideBar().clickPriorityDashboard();

        Assert.assertEquals(priorityDashboardPage.getCountOfAllExecutedContracts(), "1", "Looks like that counter for 'All executed contracts' is wrong !!!");
        Assert.assertEquals(priorityDashboardPage.getCountOfAllInProgressContracts(), "0", "Looks like that counter for 'All in-progress contracts' is wrong !!!"); // and in-progress counter should be still 0

        logger.info("Going to Executed page to make sure that contract was added...");
        ExecutedContractsPage executedContractsPage = new DashboardPage().getSideBar().clickExecutedContracts(false);

        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("Contract for executed created from PD"));

        Screenshoter.makeScreenshot();
    }
}
