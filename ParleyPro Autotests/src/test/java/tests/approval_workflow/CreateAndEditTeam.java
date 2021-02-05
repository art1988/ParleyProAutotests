package tests.approval_workflow;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.AddMembers;
import forms.AddNewTeam;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AdministrationPage;
import pages.DashboardPage;
import pages.administration.Teams;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ ScreenShotOnFailListener.class})
public class CreateAndEditTeam
{
    private static Logger logger = Logger.getLogger(CreateAndEditTeam.class);

    @Test(priority = 1)
    @Description("This test go to Administration, creates new Team, adds 3 predefined users and edit")
    public void createAndEditTeam()
    {
        DashboardPage dashboardPage = new DashboardPage();

        AdministrationPage administrationPage = dashboardPage.getSideBar().clickAdministration();

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

        logger.info("Assert that members are in table");
        $$(".teams-users-list__name").shouldHaveSize(3).shouldHave(CollectionCondition.exactTexts("CN autotest FN LN", "autotest_cn fn ln", "Internal user1 Internal user1 last name"));

        teamsTabPage = addNewTeam.clickSave();

        logger.info("Assert adding notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Team added successfully."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that team is present in table...");
        $$(".teams-list .teams-list__name").shouldHave(CollectionCondition.texts("Team Name", teamName));

        logger.info("Assert that just added team has 3 members...");
        String memberCount = Selenide.executeJavaScript("return $('.teams-list__name:contains(\"" + teamName + "\")').next().next().text()");
        Assert.assertEquals(memberCount, "3");

        addNewTeam = teamsTabPage.clickActionMenu(teamName).clickEdit();

        // Delete user by name "CN autotest FN LN"
        addNewTeam.deleteUser("CN autotest FN LN");

        // set new team name
        addNewTeam.setTeamName(teamName += " [EDITED]");

        // set new team description
        teamDescription = "Just new team description";
        addNewTeam.setTeamDescription(teamDescription);

        // add one more user to team
        addMembersForm = addNewTeam.clickAddMembersButton();
        addMembersForm.addParticipant("Felix");
        addMembersForm.clickAdd();

        // Save changes
        addNewTeam.clickSave();

        logger.info("Assert saving notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Team saved successfully."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that changes were applied...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.teams-list__row td:nth(\"1\")').text()"), teamName); // assert team name
        Assert.assertEquals(Selenide.executeJavaScript("return $('.teams-list__row td:nth(\"2\")').text()"), teamDescription); // assert team description
        Assert.assertEquals(Selenide.executeJavaScript("return $('.teams-list__row td:nth(\"3\")').text()"), "3"); // assert member count

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test adds one more team with 2 users")
    public void addOneMoreTeamWith2Users()
    {
        Teams teamsTabPage = new Teams();

        AddNewTeam addNewTeam = teamsTabPage.clickAddNewTeam();

        String teamName = "Team #2";
        addNewTeam.setTeamName(teamName);

        AddMembers addMembersForm = addNewTeam.clickAddMembersButton();
        addMembersForm.addParticipant("Internal user2 Internal user2 last name");
        addMembersForm.addParticipant("CN autotest FN LN");

        addMembersForm.clickAdd();

        addNewTeam.clickSave();

        logger.info("Assert adding notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Team added successfully."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that both teams are in the list...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.teams-list tr').length"), Long.valueOf(3)); // check row count
        Assert.assertEquals(Selenide.executeJavaScript("return $('.teams-list__row').eq(1).find(\"td:nth('1')\").text()"), teamName); // assert second team name
        Assert.assertEquals(Selenide.executeJavaScript("return $('.teams-list__row').eq(1).find(\"td:nth('3')\").text()"), "2"); // assert member count

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3, enabled = false)
    @Description("This test deletes two teams that were created in previous tests")
    public void deleteTeams()
    {
        Teams teamsTabPage = new Teams();

        teamsTabPage.clickActionMenu("Autotest_TEAM_3 [EDITED]").clickDelete().clickDelete();

        logger.info("Assert delete team notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Team deleted successfully."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        teamsTabPage.clickActionMenu("Team #2").clickDelete().clickDelete();

        logger.info("Assert delete team notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Team deleted successfully."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        // Assert that we see "you haven't added teams yet span"
        $(".teams-list__empty").shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();
    }
}
