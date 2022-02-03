package tests.requests.at219;

import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.add.AddNewUser;
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
    public void addUserWithRequesterRole()
    {
        sideBar = new DashboardPage().getSideBar();

        AddNewUser addNewUser = sideBar.clickAdministration().clickManageUsersTab().clickAddNewUser();

        addNewUser.setFirstName("USER_AT219_Requester");
        addNewUser.setEmail("arthur.khasanov+at219_requester@parleypro.com");
        addNewUser.clickAddRole().setRole("Requester");
        addNewUser.clickAddUser();

        $(byText("USER_AT219_Requester")).shouldBe(Condition.visible);
    }

    @Test(priority = 2)
    public void addRequestField()
    {
        Fields fields = sideBar.clickAdministration().clickFieldsTab();

        ContractFields contractFields = fields.clickContractFields();

        // Field to trigger workflow
        contractFields.createNewFiled("Contract Request", "ReqField_AT219_Trigger", FieldType.SELECT, false);
        contractFields.addValues("ReqField_AT219_Trigger", "v1");

        fields.clickSave();

        $(".notification-stack").shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test(priority = 3)
    public void addRoutingWorkflow()
    {
        ContractRoutingWorkflow contractRoutingWorkflow = sideBar.clickAdministration().clickWorkflowsTab()
                .clickAddNewWorkflow().clickContractRouting(false);

        contractRoutingWorkflow.setName("AT-219_Routing_Workflow");

        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant( Const.PREDEFINED_USER_CN_ROLE.getEmail() );

        contractRoutingWorkflow.addFieldAndValue("ReqField_AT219_Trigger", "v1");
        contractRoutingWorkflow.clickSave();

        $(byText("AT-219_Routing_Workflow")).shouldBe(Condition.visible);
    }
}
