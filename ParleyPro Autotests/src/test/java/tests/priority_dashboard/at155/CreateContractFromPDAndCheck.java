package tests.priority_dashboard.at155;

import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.PriorityDashboardPage;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractFromPDAndCheck
{
    private static Logger logger = Logger.getLogger(CreateContractFromPDAndCheck.class);

    @Test
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

        Assert.assertEquals(priorityDashboardPage.getCountOfAllInProgressContracts(), "1");
    }
}
