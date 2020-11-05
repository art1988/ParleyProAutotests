package tests;

import forms.ApprovalWorkflow;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.AdministrationPage;
import pages.DashboardPage;
import pages.administration.Workflows;

public class CreateApprovalWorkflow
{
    @Test(priority = 1)
    @Description("This test go to Administration, creates new approval workflow")
    public void createApprovalWorkflow() throws InterruptedException
    {
        DashboardPage dashboardPage = new DashboardPage();

        AdministrationPage administrationPage = dashboardPage.getSideBar().clickAdministration();

        Workflows workflowsTabPage = administrationPage.clickWorkflowsTab();

        ApprovalWorkflow approvalWorkflowForm = workflowsTabPage.clickAddNewWorkflow().clickApproval();

        String workflowName = "Approval_WFL_AT";

        approvalWorkflowForm.setName(workflowName);
        approvalWorkflowForm.setCategory("category2");
        approvalWorkflowForm.setType("type2");
        approvalWorkflowForm.setDepartment("department2");
        approvalWorkflowForm.setCurrency("GBP");
        approvalWorkflowForm.setMinValue("250");
        approvalWorkflowForm.setMaxValue("1300");

        approvalWorkflowForm.clickPriorToNegotiate();
        approvalWorkflowForm.setPriorToNegotiateParticipant("Autotest_TEAM_3 [EDITED]");
        approvalWorkflowForm.setPriorToNegotiateParticipant("Approval_User_1");

        approvalWorkflowForm.clickPriorToSign();
        approvalWorkflowForm.setPriorToSignParticipant("Team #2");
        approvalWorkflowForm.setPriorToSignParticipant("Approval_User_2");

        approvalWorkflowForm.switchTumblerApprovalOrderOfPriorToSign();
        approvalWorkflowForm.switchTumblerAllowToModifyApproversOfPriorToSign();

        Thread.sleep(5_000);
    }
}
