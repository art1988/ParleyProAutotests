package tests.routing_workflow;

import constants.Const;
import forms.workflows.ContractRoutingWorkflow;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Workflows;

public class CreateContractRoutingWorkflow
{
    private static Logger logger = Logger.getLogger(CreateContractRoutingWorkflow.class);

    @Test
    public void createContractRoutingWorkflow() throws InterruptedException
    {
        Workflows workflowsTabPage = new DashboardPage().getSideBar().clickAdministration().clickWorkflowsTab();

        ContractRoutingWorkflow contractRoutingWorkflowForm = workflowsTabPage.clickAddNewWorkflow().clickContractRouting();

        String workflowName = "Contract_routing_WFL_AT";

        contractRoutingWorkflowForm.setName(workflowName);
        contractRoutingWorkflowForm.setCategory("category1");
        contractRoutingWorkflowForm.setType("type2");
        contractRoutingWorkflowForm.setDepartment("department1");
        contractRoutingWorkflowForm.setCurrency("EUR"); // TODO: switch back to JPY after fixing of PAR-12786
        contractRoutingWorkflowForm.setMinValue("15400");
        contractRoutingWorkflowForm.setMaxValue("32700");

        contractRoutingWorkflowForm.clickDraftToReview();
        contractRoutingWorkflowForm.setDraftToReviewParticipant( Const.PREDEFINED_INTERNAL_USER_1.getFirstName() ); // As User #1

        contractRoutingWorkflowForm.clickTextChanged();
        contractRoutingWorkflowForm.setTextChangedParticipant( Const.PREDEFINED_INTERNAL_USER_2.getFirstName() ); // As User #2

        contractRoutingWorkflowForm.clickSignatureDeclined();
        contractRoutingWorkflowForm.setSignatureDeclinedParticipant( Const.PREDEFINED_APPROVER_USER_1.getFirstName() ); // As User #3

        contractRoutingWorkflowForm.clickUploadCounterpartyDocument();
        contractRoutingWorkflowForm.setUploadCounterpartyDocumentParticipant( Const.PREDEFINED_APPROVER_USER_2.getFirstName() ); // As User #4

        contractRoutingWorkflowForm.setRoleForUser(Const.PREDEFINED_APPROVER_USER_2.getFirstName(), "Lead");
        contractRoutingWorkflowForm.clickSave();

        logger.info("Edit of just created workflow and assert that is was saved correctly...");
        workflowsTabPage.clickActionMenu(workflowName).clickEdit();
        contractRoutingWorkflowForm = new ContractRoutingWorkflow();

        Assert.assertEquals(contractRoutingWorkflowForm.getName(), workflowName);
        Assert.assertEquals(contractRoutingWorkflowForm.getCategory(), "category1");
        Assert.assertEquals(contractRoutingWorkflowForm.getType(), "type2");
        Assert.assertEquals(contractRoutingWorkflowForm.getDepartment(), "department1");
        Assert.assertEquals(contractRoutingWorkflowForm.getCurrency(), "EUR");
        Assert.assertEquals(contractRoutingWorkflowForm.getMinValue(), "15,400.00");
        Assert.assertEquals(contractRoutingWorkflowForm.getMaxValue(), "32,700.00");

        Thread.sleep(5_000);
    }
}
