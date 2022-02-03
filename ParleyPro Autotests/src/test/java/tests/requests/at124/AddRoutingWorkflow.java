package tests.requests.at124;

import constants.Const;
import forms.workflows.ContractRoutingWorkflow;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Workflows;
import utils.Screenshoter;


public class AddRoutingWorkflow
{
    @Test
    @Description("This test creates routing workflow.")
    public void addRoutingWorkflow()
    {
        Workflows workflowsPage = new DashboardPage().getSideBar().clickAdministration().clickWorkflowsTab();

        ContractRoutingWorkflow contractRoutingWorkflow = workflowsPage.clickAddNewWorkflow().clickContractRouting(false);

        contractRoutingWorkflow.setName("requests");
        contractRoutingWorkflow.setCurrency("EUR");
        contractRoutingWorkflow.setMinValue("3500");
        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant( Const.PREDEFINED_USER_CN_ROLE.getFirstName() );

        contractRoutingWorkflow.addFieldAndValue("Contracting region", "All");

        contractRoutingWorkflow.clickSave();

        Screenshoter.makeScreenshot();
    }
}
