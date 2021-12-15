package tests.approval_workflow.at201;

import forms.workflows.ApprovalWorkflow;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class TryToSaveWkflowWithEmptyRequestApprover
{
    @Test
    public void tryToSaveWkflowWithEmptyRequestApprover() throws InterruptedException
    {
        ApprovalWorkflow approvalWorkflow =  new DashboardPage().getSideBar()
                                                                .clickAdministration()
                                                                .clickWorkflowsTab()
                                                                .clickAddNewWorkflow()
                                                                .clickApproval(false);

        approvalWorkflow.setName("Empty_Request_Approver");
        approvalWorkflow.clickContractRequest();
        approvalWorkflow.clickSave();


    }
}
