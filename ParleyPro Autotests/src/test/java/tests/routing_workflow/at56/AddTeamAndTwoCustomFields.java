package tests.routing_workflow.at56;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.FieldType;
import forms.add.AddMembers;
import forms.add.AddNewTeam;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.Teams;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AddTeamAndTwoCustomFields
{
    @Test(priority = 1)
    @Description("This test adds two custom fields in Summary: 'Enable Routing' and 'Enable Approval'")
    public void addTwoCustomFields()
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFieldsPage = fieldsTab.clickContractFields();


        contractFieldsPage.createNewFiled("Summary", "Enable Routing", FieldType.SELECT, false);
        contractFieldsPage.addValues("Enable Routing", "Yes");
        contractFieldsPage.addValues("Enable Routing", "No");

        contractFieldsPage.createNewFiled("Summary", "Enable Approval", FieldType.SELECT, false);
        contractFieldsPage.addValues("Enable Approval", "Yes");
        contractFieldsPage.addValues("Enable Approval", "No");

        fieldsTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test adds team by the name Team123 with users 'vp1 ln', 'vp2' and 'vp3'.")
    public void addTeam()
    {
        Teams teamsPage = new DashboardPage().getSideBar().clickAdministration().clickTeamsTab();

        AddNewTeam addNewTeam = teamsPage.clickAddNewTeam();

        addNewTeam.setTeamName("Team123");
        AddMembers addMembers = addNewTeam.clickAddMembersButton();
        addMembers.addParticipant("vp1");
        addMembers.addParticipant("vp2");
        addMembers.addParticipant("vp3");
        addMembers.clickAdd();

        addNewTeam.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Team added successfully."));

        // assert that record was added
        $$(".teams-list__row .teams-list__name").shouldHave(CollectionCondition.size(1))
                                                           .shouldHave(CollectionCondition.exactTexts("Team123"));

        Screenshoter.makeScreenshot();
    }
}
