package tests;

import com.codeborne.selenide.CollectionCondition;
import forms.AddMembers;
import forms.AddNewTeam;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.AdministrationPage;
import pages.administration.Teams;
import pages.subelements.SideBar;

import static com.codeborne.selenide.Selenide.$$;

public class CreateTeam
{
    @Test
    @Description("This test go to Administration, creates new Team, adds 3 predefined users")
    public void createTeam()
    {
        SideBar sideBar = new SideBar();

        AdministrationPage administrationPage = sideBar.clickAdministration();

        Teams teamsTabPage = administrationPage.clickTeamsTab();

        AddNewTeam addNewTeam = teamsTabPage.clickAddNewTeam();

        String teamName = "Autotest_TEAM_3";
        String teamDescription = "This is autotest team description. Consist of 3 members.";

        addNewTeam.setTeamName(teamName);
        addNewTeam.setTeamDescription(teamDescription);

        AddMembers addMembersForm = addNewTeam.clickAddMembersButton();

        // pick 3 predefined users
        addMembersForm.addParticipant("CN autotest FN LN");
        addMembersForm.addParticipant("autotest_cn fn ln");
        addMembersForm.addParticipant("Internal user1 Internal user1 last name");

        addMembersForm.clickAdd();

        // assert that members are in table
        $$(".teams-users-list__name").shouldHaveSize(3).shouldHave(CollectionCondition.exactTexts("CN autotest FN LN", "autotest_cn fn ln", "Internal user1 Internal user1 last name"));
    }
}
