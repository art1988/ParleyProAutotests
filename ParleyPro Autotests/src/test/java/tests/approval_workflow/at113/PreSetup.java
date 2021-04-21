package tests.approval_workflow.at113;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.FieldType;
import forms.add.AddMembers;
import forms.add.AddNewTeam;
import forms.add.AddNewUser;
import forms.workflows.ApprovalWorkflow;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.Workflows;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class PreSetup
{
    @Test(priority = 1, enabled = false)
    public void createUser()
    {
        AddNewUser addNewUserForm = new DashboardPage().getSideBar().clickAdministration().clickManageUsersTab().clickAddNewUser();

        addNewUserForm.setFirstName("U1");
        addNewUserForm.setEmail("arthur.khasanov+u1@parleypro.com");
        addNewUserForm.clickAddRole();
        addNewUserForm.setRole("Viewer Plus");
        addNewUserForm.clickAddUser();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("User U1 added successfully"));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);


    }

    @Test(priority = 2)
    public void createTeam()
    {
        AddNewTeam addNewTeamForm = new DashboardPage().getSideBar().clickAdministration().clickTeamsTab().clickAddNewTeam();

        addNewTeamForm.setTeamName("T1");
        AddMembers addMembers = addNewTeamForm.clickAddMembersButton();
        addMembers.addParticipant("U1");
        addMembers.addParticipant("Mary");
        addMembers.clickAdd();

        addNewTeamForm.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Team added successfully."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);
    }

    @Test(priority = 3)
    public void createField()
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.createNewFiled("Summary", "Approve T1", FieldType.SELECT, false);
        contractFields.addValues("Approve T1", "YES");
        contractFields.addValues("Approve T1", "NO");

        fieldsTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test(priority = 4)
    public void createApprovalWorkflow()
    {
        Workflows workflowsTab = new DashboardPage().getSideBar().clickAdministration().clickWorkflowsTab();

        ApprovalWorkflow approvalWorkflowForm = workflowsTab.clickAddNewWorkflow().clickApproval(false);

        approvalWorkflowForm.setName("W1");
        approvalWorkflowForm.clickPriorToNegotiate();
        approvalWorkflowForm.setPriorToNegotiateParticipant("T1");
        approvalWorkflowForm.addFieldAndValue("Approve T1", "YES");
        approvalWorkflowForm.clickSave();

        Assert.assertEquals((long) Selenide.executeJavaScript("return $('.workflows-list__cell.type_name:contains(\"W1\")').length"), 1,
                "Approval Workflow W1 wasn't added !!!");
    }
}
