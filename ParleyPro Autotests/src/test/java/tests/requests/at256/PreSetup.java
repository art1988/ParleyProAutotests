package tests.requests.at256;

import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.add.AddMembers;
import forms.add.AddNewTeam;
import forms.workflows.ApprovalWorkflow;
import forms.workflows.ContractRoutingWorkflow;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.Teams;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
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

        contractFields.createNewFiled("Contract Request", "AT256-RequestF", FieldType.SELECT, false);
        contractFields.addValues("AT256-RequestF", "v1");
        contractFields.addValues("AT256-RequestF", "v2");
        fields.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));


        ContractRoutingWorkflow contractRoutingWorkflow = sideBar.clickAdministration().clickWorkflowsTab().clickAddNewWorkflow().clickContractRouting(false);
        contractRoutingWorkflow.setName("Routing Workflow AT256");
        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        contractRoutingWorkflow.addFieldAndValue("AT256-RequestF", "All");
        contractRoutingWorkflow.clickSave();

        $(byText("Routing Workflow AT256")).shouldBe(Condition.visible);
    }

    @Test(priority = 2)
    public void addTeams()
    {
        Teams teamsPage = sideBar.clickAdministration().clickTeamsTab();

        AddNewTeam addNewTeam = teamsPage.clickAddNewTeam();

        addNewTeam.setTeamName("T1");
        AddMembers addMembers = addNewTeam.clickAddMembersButton();
        addMembers.addParticipant(Const.PREDEFINED_INTERNAL_USER_1.getEmail());
        addMembers.addParticipant(Const.PREDEFINED_INTERNAL_USER_2.getEmail());
        addMembers.clickAdd();
        addNewTeam.clickSave();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Team added successfully"));
        $(withText("T1")).shouldBe(Condition.visible);

        ////////////

        addNewTeam = teamsPage.clickAddNewTeam();

        addNewTeam.setTeamName("T2");
        addMembers = addNewTeam.clickAddMembersButton();
        addMembers.addParticipant(Const.USER_GREG.getEmail());
        addMembers.clickAdd();
        addNewTeam.clickSave();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Team added successfully"));
        $(withText("T2")).shouldBe(Condition.visible);
    }

    @Test(priority = 3)
    public void addApprovalWorkflow()
    {
        ApprovalWorkflow approvalWorkflow = sideBar.clickAdministration().clickWorkflowsTab().clickAddNewWorkflow().clickApproval(false);
        approvalWorkflow.setName("Approval Workflow AT256");
        approvalWorkflow.clickContractRequest();
        approvalWorkflow.setContractRequestParticipant("T1");
        approvalWorkflow.addFieldAndValue("AT256-RequestF", "All");
        approvalWorkflow.clickSave();

        $(byText("Approval Workflow AT256")).shouldBe(Condition.visible);
    }
}
