package tests.routing_workflow.at56;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import forms.workflows.ApprovalWorkflow;
import forms.workflows.ContractRoutingWorkflow;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;


public class AddWorkflows
{
    @Test(priority = 1)
    @Description("This test adds routing workflow.")
    public void addRoutingWorkflow()
    {
        ContractRoutingWorkflow routingWorkflow = new DashboardPage().getSideBar()
                                                                     .clickAdministration()
                                                                     .clickWorkflowsTab()
                                                                     .clickAddNewWorkflow()
                                                                     .clickContractRouting(false);

        routingWorkflow.setName("Routing Workflow AT-56");

        routingWorkflow.clickDraftToReview();
        routingWorkflow.setDraftToReviewParticipant("Team123");
        routingWorkflow.setDraftToReviewParticipant(Const.PREDEFINED_USER_CN_ROLE.getEmail());

        routingWorkflow.clickTextChanged();
        routingWorkflow.setTextChangedParticipant("Team123");
        routingWorkflow.setTextChangedParticipant(Const.PREDEFINED_USER_CN_ROLE.getEmail());

        routingWorkflow.clickSignatureDeclined();
        routingWorkflow.setSignatureDeclinedParticipant("Team123");

        routingWorkflow.clickUploadCounterpartyDocument();
        routingWorkflow.setUploadCounterpartyDocumentParticipant("Team123");

        routingWorkflow.clickContractRequested();
        routingWorkflow.setContractRequestedParticipant(Const.PREDEFINED_USER_CN_ROLE.getEmail());

        routingWorkflow.addFieldAndValue("Enable Routing", "Yes");


        routingWorkflow.clickSave();

        // assert that record was added
        $$(".workflows-list__row:not(.type_header) .type_name").shouldHave(CollectionCondition.size(1))
                                                                         .shouldHave(CollectionCondition.exactTexts("Routing Workflow AT-56"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test adds approval workflow.")
    public void addApprovalWorkflow()
    {
        ApprovalWorkflow approvalWorkflow = new DashboardPage().getSideBar()
                                                               .clickAdministration()
                                                               .clickWorkflowsTab()
                                                               .clickAddNewWorkflow()
                                                               .clickApproval(false);

        approvalWorkflow.setName("Approval Workflow AT-56");

        approvalWorkflow.clickPriorToNegotiate();
        approvalWorkflow.setPriorToNegotiateParticipant(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        approvalWorkflow.setPriorToNegotiateParticipant("Team123");

        approvalWorkflow.clickPriorToSign();
        approvalWorkflow.switchTumblerApprovalOrderOfPriorToSign();
        approvalWorkflow.setPriorToSignParticipant(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        approvalWorkflow.setPriorToSignParticipant("Team123");

        approvalWorkflow.addFieldAndValue("Enable Approval", "Yes");

        approvalWorkflow.clickSave();

        // assert that record was added
        $$(".workflows-list__row:not(.type_header) .type_name").shouldHave(CollectionCondition.size(2))
                                                                         .shouldHave(CollectionCondition.textsInAnyOrder("Routing Workflow AT-56", "Approval Workflow AT-56"));

        Screenshoter.makeScreenshot();
    }
}
