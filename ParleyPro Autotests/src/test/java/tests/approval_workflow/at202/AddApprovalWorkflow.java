package tests.approval_workflow.at202;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.workflows.ApprovalWorkflow;
import org.testng.annotations.Test;
import pages.DashboardPage;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;


public class AddApprovalWorkflow
{
    private String wrkflowName = "AT-202 approval wkfl";

    @Test
    public void addApprovalWorkflow()
    {
        ApprovalWorkflow approvalWorkflow =  new DashboardPage().getSideBar()
                                                                .clickAdministration()
                                                                .clickWorkflowsTab()
                                                                .clickAddNewWorkflow()
                                                                .clickApproval(false);

        approvalWorkflow.setName(wrkflowName);
        approvalWorkflow.setCategory("category1");

        approvalWorkflow.clickPriorToNegotiate();
        approvalWorkflow.setPriorToNegotiateParticipant(Const.PREDEFINED_USER_CN_ROLE.getEmail());

        approvalWorkflow.clickPriorToSign();
        approvalWorkflow.setPriorToSignParticipant(Const.PREDEFINED_USER_CN_ROLE.getEmail());

        approvalWorkflow.clickSave();

        $(byText(wrkflowName)).shouldBe(Condition.visible);
    }
}
