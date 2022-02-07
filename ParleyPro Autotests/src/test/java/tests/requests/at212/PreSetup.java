package tests.requests.at212;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.add.AddNewUser;
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
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class PreSetup
{
    private SideBar sideBar;


    @Test(priority = 1)
    public void addCMRoleForRequesterUser()
    {
        sideBar = new DashboardPage().getSideBar();

        sideBar.clickAdministration().clickManageUsersTab().clickActionMenu("Requester_User ln").clickEdit()
                .clickAddRole().setRole("Contract Manager").clickUpdateUser();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("User Requester_User ln updated successfully"));

        // reopen user and check that both roles are in place
        AddNewUser addNewUser = sideBar.clickAdministration().clickManageUsersTab().clickActionMenu("Requester_User ln").clickEdit();
        $$(".user-edit-roles__role .new-select__single-value").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("Contract Manager", "Requester"));
        addNewUser.clickCancel();
    }

    @Test(priority = 2)
    public void addRequestFields()
    {
        Fields fields = sideBar.clickAdministration().clickFieldsTab();

        ContractFields contractFields = fields.clickContractFields();

        // Field to trigger workflow
        contractFields.createNewFiled("Contract Request", "ReqField_AT212_Trigger", FieldType.SELECT, false);
        contractFields.addValues("ReqField_AT212_Trigger", "val_1");

        // Additional fields for request form
        contractFields.createNewFiled("Contract Request", "ReqField_AT212_SELECT", FieldType.SELECT, false);
        contractFields.addValues("ReqField_AT212_SELECT", "SEL_val_1");
        contractFields.addValues("ReqField_AT212_SELECT", "SEL_val_2");
        contractFields.createNewFiled("Contract Request", "ReqField_AT212_TEXT", FieldType.TEXT, false);
        contractFields.createNewFiled("Contract Request", "ReqField_AT212_NUM", FieldType.NUMERIC, false);

        fields.clickSave();

        $(".notification-stack").shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test(priority = 3)
    public void addRoutingWorkflow()
    {
        ContractRoutingWorkflow contractRoutingWorkflow = sideBar.clickAdministration().clickWorkflowsTab()
                .clickAddNewWorkflow().clickContractRouting(false);

        contractRoutingWorkflow.setName("AT-212_Routing_Workflow");

        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant( Const.PREDEFINED_USER_CN_ROLE.getEmail() );

        contractRoutingWorkflow.addFieldAndValue("ReqField_AT212_Trigger", "val_1");
        contractRoutingWorkflow.clickSave();

        $(byText("AT-212_Routing_Workflow")).shouldBe(Condition.visible);
    }

    @Test(priority = 4)
    public void logoutAsCN()
    {
        sideBar.logout();
    }
}
