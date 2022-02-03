package tests.requests.at215;

import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.workflows.ContractRoutingWorkflow;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.subelements.SideBar;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;


public class PreSetup
{
    private SideBar sideBar;


    @Test(priority = 1)
    public void addRequestField()
    {
        sideBar = new DashboardPage().getSideBar();

        Fields fields = sideBar.clickAdministration().clickFieldsTab();

        ContractFields contractFields = fields.clickContractFields();

        // Field to trigger workflow
        contractFields.createNewFiled("Contract Request", "ReqField_AT215_Trigger", FieldType.SELECT, false);
        contractFields.addValues("ReqField_AT215_Trigger", "v1");

        fields.clickSave();

        $(".notification-stack").shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test(priority = 2)
    public void addRoutingWorkflow()
    {
        ContractRoutingWorkflow contractRoutingWorkflow = sideBar.clickAdministration().clickWorkflowsTab()
                .clickAddNewWorkflow().clickContractRouting(false);

        contractRoutingWorkflow.setName("AT-215_Routing_Workflow");

        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant( Const.USER_FELIX.getEmail() );

        contractRoutingWorkflow.addFieldAndValue("ReqField_AT215_Trigger", "v1");
        contractRoutingWorkflow.clickSave();

        $(byText("AT-215_Routing_Workflow")).shouldBe(Condition.visible);
    }

    @Test(priority = 3)
    public void logoutAsCN()
    {
        sideBar.logout();
    }
}
