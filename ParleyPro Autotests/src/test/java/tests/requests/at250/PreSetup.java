package tests.requests.at250;

import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.workflows.ApprovalWorkflow;
import forms.workflows.ContractRoutingWorkflow;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class PreSetup
{
    private SideBar sideBar;


    @Test(priority = 1)
    public void addRoutingWorkflow()
    {
        sideBar = new DashboardPage().getSideBar();

        Fields fields = sideBar.clickAdministration().clickFieldsTab();
        ContractFields contractFields = fields.clickContractFields();

        contractFields.createNewFiled("Contract Request", "AT250-RequestF", FieldType.SELECT, false);
        contractFields.addValues("AT250-RequestF", "v1");
        contractFields.addValues("AT250-RequestF", "v2");
        fields.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));


        ContractRoutingWorkflow contractRoutingWorkflow = sideBar.clickAdministration().clickWorkflowsTab().clickAddNewWorkflow().clickContractRouting(false);
        contractRoutingWorkflow.setName("Routing Workflow AT250");
        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        contractRoutingWorkflow.addFieldAndValue("AT250-RequestF", "All");
        contractRoutingWorkflow.clickSave();

        $(byText("Routing Workflow AT250")).shouldBe(Condition.visible);
    }

    @Test(priority = 2)
    public void addApprovalWorkflow()
    {
        ApprovalWorkflow approvalWorkflow = sideBar.clickAdministration().clickWorkflowsTab().clickAddNewWorkflow().clickApproval(false);
        approvalWorkflow.setName("Approval Workflow AT250");
        approvalWorkflow.clickContractRequest();
        approvalWorkflow.switchTumblerApprovalOrderOfContractRequest();
        approvalWorkflow.setContractRequestParticipant(Const.PREDEFINED_APPROVER_USER_1.getEmail());
        approvalWorkflow.setContractRequestParticipant(Const.PREDEFINED_APPROVER_USER_2.getEmail());
        approvalWorkflow.addFieldAndValue("AT250-RequestF", "All");
        approvalWorkflow.clickSave();

        $(byText("Approval Workflow AT250")).shouldBe(Condition.visible);
    }
}
