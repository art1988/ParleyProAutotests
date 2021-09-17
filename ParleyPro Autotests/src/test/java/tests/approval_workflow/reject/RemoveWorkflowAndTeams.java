package tests.approval_workflow.reject;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AdministrationPage;
import pages.DashboardPage;
import pages.administration.Teams;
import pages.administration.Workflows;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class RemoveWorkflowAndTeams
{
    private static Logger logger = Logger.getLogger(RemoveWorkflowAndTeams.class);

    @Test(priority = 1)
    @Description("This test delete workflow: Approval_WFL_AT")
    public void deleteWorkflow()
    {
        Workflows workflowsTabPage = new DashboardPage().getSideBar().clickAdministration().clickWorkflowsTab();
        workflowsTabPage.clickActionMenu("Approval_WFL_AT").clickDelete().clickDelete();

        try
        {
            Thread.sleep(1_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        logger.info("Assert that there is no approval workflow anymore...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.workflows-list__row').length === 1"));
    }

    @Test(priority = 2)
    @Description("This test delete 2 teams: Autotest_TEAM_3 [EDITED] and Team #2")
    public void deleteTeams()
    {
        Teams teamsTabPage = new AdministrationPage().clickTeamsTab();

        // wait until spinner disappear and list of team should appear
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        teamsTabPage.clickActionMenu("Autotest_TEAM_3 [EDITED]").clickDelete().clickDelete();

        logger.info("Assert delete team notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Team deleted successfully."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        teamsTabPage.clickActionMenu("Team #2").clickDelete().clickDelete();

        logger.info("Assert delete team notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Team deleted successfully."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);
    }
}
