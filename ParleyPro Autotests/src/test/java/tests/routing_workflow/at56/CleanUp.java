package tests.routing_workflow.at56;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.Teams;
import pages.administration.Workflows;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    @Test(priority = 1)
    public void removeWorkflows() throws InterruptedException
    {
        Workflows workflows = new DashboardPage().getSideBar().clickAdministration().clickWorkflowsTab();

        workflows.clickActionMenu("Approval Workflow AT-56").clickDelete().clickDelete();
        Thread.sleep(500);

        workflows.clickActionMenu("Routing Workflow AT-56").clickDelete().clickDelete();
        Thread.sleep(500);

        $(".workflows-list__row:not(.type_header)").shouldBe(Condition.disappear); // no records left
    }

    @Test(priority = 2)
    public void removeFields()
    {
        Fields fields = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fields.clickContractFields();

        contractFields.removeField("Enable Routing").clickDelete();
        contractFields.removeField("Enable Approval").clickDelete();

        fields.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.text("Contract fields have been saved."));
    }

    @Test(priority = 3)
    public void removeTeam()
    {
        Teams teams = new DashboardPage().getSideBar().clickAdministration().clickTeamsTab();

        teams.clickActionMenu("Team123").clickDelete().clickDelete();

        $(".teams-list__row .teams-list__name").shouldBe(Condition.disappear); // no records left
    }
}
