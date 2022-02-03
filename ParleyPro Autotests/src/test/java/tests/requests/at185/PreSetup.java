package tests.requests.at185;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.workflows.ApprovalWorkflow;
import forms.workflows.ContractRoutingWorkflow;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class PreSetup
{
    private static Logger logger = Logger.getLogger(PreSetup.class);

    @Test(priority = 1)
    public void addRequestFields()
    {
        Fields fields = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fields.clickContractFields();

        contractFields.createNewFiled("Contract Request", "ReqField_1_AT185_routing", FieldType.SELECT, false);
        contractFields.addValues("ReqField_1_AT185_routing", "f1_v1");
        contractFields.addValues("ReqField_1_AT185_routing", "f1_v2");

        contractFields.createNewFiled("Contract Request", "ReqField_2_AT185_approval", FieldType.SELECT, false);
        contractFields.addValues("ReqField_2_AT185_approval", "f2_v1");
        contractFields.addValues("ReqField_2_AT185_approval", "f2_v2");

        contractFields.createNewFiled("Contract Request", "Counterparty organization", FieldType.TEXT, false);
        contractFields.createNewFiled("Contract Request", "Counterparty Chief Negotiator", FieldType.TEXT, false);

        fields.clickSave();

        $(".notification-stack").shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test(priority = 2)
    public void addRoutingWorkflow()
    {
        ContractRoutingWorkflow contractRoutingWorkflow = new DashboardPage().getSideBar().clickAdministration()
                .clickWorkflowsTab().clickAddNewWorkflow().clickContractRouting(false);

        contractRoutingWorkflow.setName("AT-185 routing_workflow");

        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant( Const.PREDEFINED_USER_CN_ROLE.getEmail() );

        contractRoutingWorkflow.addFieldAndValue("ReqField_1_AT185_routing", "f1_v1");
        contractRoutingWorkflow.clickSave();
    }

    @Test(priority = 3)
    public void addApprovalWorkflow()
    {
        ApprovalWorkflow approvalWorkflow = new DashboardPage().getSideBar().clickAdministration()
                .clickWorkflowsTab().clickAddNewWorkflow().clickApproval(false);

        approvalWorkflow.setName("AT-185 approval_workflow");

        approvalWorkflow.clickContractRequest();
        approvalWorkflow.setContractRequestParticipant( Const.PREDEFINED_APPROVER_USER_1.getEmail() );

        approvalWorkflow.addFieldAndValue("ReqField_2_AT185_approval", "f2_v2");
        approvalWorkflow.clickSave();

        logger.info("Making sure that both workflows are in the list...");
        $$(".workflows-list__cell.type_name").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("Name", "AT-185 approval_workflow", "AT-185 routing_workflow"));
    }

    @Test(priority = 4)
    public void logoutCN()
    {
        new DashboardPage().getSideBar().logout();
    }
}
