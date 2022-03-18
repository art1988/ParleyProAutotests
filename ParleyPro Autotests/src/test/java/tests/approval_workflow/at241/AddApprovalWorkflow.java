package tests.approval_workflow.at241;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.workflows.ApprovalWorkflow;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddApprovalWorkflow
{
    private String wrkflowName = "AT-241 workflow";

    @Test
    public void addApprovalWorkflow()
    {
        ApprovalWorkflow approvalWorkflow =  new DashboardPage().getSideBar()
                                                                .clickAdministration()
                                                                .clickWorkflowsTab()
                                                                .clickAddNewWorkflow()
                                                                .clickApproval(false);

        approvalWorkflow.setName(wrkflowName);

        approvalWorkflow.clickPriorToNegotiate();
        approvalWorkflow.setPriorToNegotiateParticipant(Const.PREDEFINED_APPROVER_USER_1.getEmail());

        approvalWorkflow.clickPriorToSign();
        approvalWorkflow.setPriorToSignParticipant(Const.PREDEFINED_APPROVER_USER_1.getEmail());

        approvalWorkflow.setMinValue("250000");
        approvalWorkflow.setMaxValue("250000");

        approvalWorkflow.clickSave();

        $(byText(wrkflowName)).shouldBe(Condition.visible);
    }
}
