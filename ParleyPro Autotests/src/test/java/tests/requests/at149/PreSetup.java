package tests.requests.at149;

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
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class PreSetup
{
    @Test(priority = 1)
    public void addCNRoleForPREDEFINED_REQUESTERUser()
    {
        AddNewUser addNewUserForm = new DashboardPage().getSideBar()
                                                       .clickAdministration()
                                                       .clickManageUsersTab()
                                                       .clickActionMenu(Const.PREDEFINED_REQUESTER.getFirstName() + " " + Const.PREDEFINED_REQUESTER.getLastName())
                                                       .clickEdit();

        addNewUserForm.clickAddRole();
        addNewUserForm.setRole("Chief Negotiator");
        addNewUserForm.clickUpdateUser();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("updated successfully"));
    }

    @Test(priority = 2)
    public void addRequestField()
    {
        Fields fieldsPage = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsPage.clickContractFields();

        contractFields.createNewFiled("Contract Request", "requestField", FieldType.MULTI_SELECT, false);
        contractFields.addValues("requestField", "v1");
        contractFields.addValues("requestField", "v2");

        fieldsPage.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        $("input[value='requestField']").shouldBe(Condition.visible);
    }

    @Test(priority = 3)
    public void  addRoutingWorkflow()
    {
        ContractRoutingWorkflow contractRoutingWorkflow = new DashboardPage().getSideBar()
                                                                             .clickAdministration()
                                                                             .clickWorkflowsTab()
                                                                             .clickAddNewWorkflow()
                                                                             .clickContractRouting(false);

        contractRoutingWorkflow.setName("Routing workflow at-149");
        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant( Const.PREDEFINED_REQUESTER.getFirstName() );
        contractRoutingWorkflow.setMinValue("15000");
        contractRoutingWorkflow.addFieldAndValue("requestField", "v1");
        contractRoutingWorkflow.clickSave();

        $(".workflows-list__row:not(.type_header) .type_name").shouldHave(Condition.exactText("Routing workflow at-149"));
    }
}
