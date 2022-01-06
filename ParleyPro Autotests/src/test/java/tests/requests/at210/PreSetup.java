package tests.requests.at210;

import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.workflows.ContractRoutingWorkflow;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class PreSetup
{
    @Test(priority = 1)
    public void addRequestField()
    {
        Fields fields = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fields.clickContractFields();

        contractFields.createNewFiled("Contract Request", "ReqField_AT210", FieldType.SELECT, false);
        contractFields.addValues("ReqField_AT210", "val_1");
        contractFields.addValues("ReqField_AT210", "val_2");

        fields.clickSave();

        $(".notification-stack").shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test(priority = 2)
    public void addRoutingWorkflow()
    {
        ContractRoutingWorkflow contractRoutingWorkflow = new DashboardPage().getSideBar().clickAdministration()
                .clickWorkflowsTab().clickAddNewWorkflow().clickContractRouting(false);

        contractRoutingWorkflow.setName("AT-210 routing_workflow");

        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant( Const.PREDEFINED_USER_CN_ROLE.getEmail() );

        contractRoutingWorkflow.addFieldAndValue("ReqField_AT210", "val_1");
        contractRoutingWorkflow.clickSave();

        $(byText("AT-210 routing_workflow")).shouldBe(Condition.visible);
    }

    @Test(priority = 3)
    public void logoutAsCN()
    {
        new DashboardPage().getSideBar().logout();
    }
}
