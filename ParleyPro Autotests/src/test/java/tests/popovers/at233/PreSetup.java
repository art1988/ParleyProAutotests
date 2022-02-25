package tests.popovers.at233;

import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.workflows.ContractRoutingWorkflow;
import io.qameta.allure.Step;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.ManageUsers;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class PreSetup
{
    private SideBar sideBar;

    @Test
    public void preSetup()
    {
        sideBar = new DashboardPage().getSideBar();

        addViewerPlusRole();
        addRequestField();
        addRequestWorkflow();

        sideBar.logout();
    }

    @Step("Add Viewer Plus role for Requester_User")
    public void addViewerPlusRole()
    {
        ManageUsers manageUsers = sideBar.clickAdministration().clickManageUsersTab();

        manageUsers.clickActionMenu("Requester_User ln")
                   .clickEdit()
                   .clickAddRole()
                   .setRole("Viewer Plus")
                   .clickUpdateUser();

        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("updated successfully"));
    }

    @Step("Add Request Field")
    public void addRequestField()
    {
        Fields fieldsTab = sideBar.clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.createNewFiled("Contract Request", "AT-233_trigger_field", FieldType.SELECT, false);
        contractFields.addValues("AT-233_trigger_field", "a");
        contractFields.addValues("AT-233_trigger_field", "b");
        fieldsTab.clickSave();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract fields have been saved."));
    }

    @Step("Add request workflow")
    public void addRequestWorkflow()
    {
        ContractRoutingWorkflow contractRoutingWorkflow = sideBar.clickAdministration()
                                                                 .clickWorkflowsTab()
                                                                 .clickAddNewWorkflow()
                                                                 .clickContractRouting(false);

        contractRoutingWorkflow.setName("Routing_Workflow_for_AT-233");
        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        contractRoutingWorkflow.addFieldAndValue("AT-233_trigger_field", "All");
        contractRoutingWorkflow.clickSave();

        $(byText("Routing_Workflow_for_AT-233")).shouldBe(Condition.visible);
    }
}
