package tests.requests.at251;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
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

        contractFields.createNewFiled("Contract Request", "AT251-RequestF", FieldType.SELECT, false);
        contractFields.addValues("AT251-RequestF", "v1");
        contractFields.addValues("AT251-RequestF", "v2");
        fields.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));


        ContractRoutingWorkflow contractRoutingWorkflow = sideBar.clickAdministration().clickWorkflowsTab().clickAddNewWorkflow().clickContractRouting(false);
        contractRoutingWorkflow.setName("Routing Workflow AT251");
        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        contractRoutingWorkflow.addFieldAndValue("AT251-RequestF", "All");
        contractRoutingWorkflow.clickSave();

        $(byText("Routing Workflow AT251")).shouldBe(Condition.visible);
    }

    @Test(priority = 2)
    public void addApprovalWorkflow()
    {
        ApprovalWorkflow approvalWorkflow = sideBar.clickAdministration().clickWorkflowsTab().clickAddNewWorkflow().clickApproval(false);
        approvalWorkflow.setName("Approval Workflow AT251");
        approvalWorkflow.clickContractRequest();
        approvalWorkflow.setContractRequestParticipant(Const.PREDEFINED_APPROVER_USER_1.getEmail());
        approvalWorkflow.addFieldAndValue("AT251-RequestF", "All");
        approvalWorkflow.clickSave();

        $(byText("Approval Workflow AT251")).shouldBe(Condition.visible);
    }
}
