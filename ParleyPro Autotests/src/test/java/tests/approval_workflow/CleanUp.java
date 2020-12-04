package tests.approval_workflow;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AdministrationPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.administration.Teams;
import pages.administration.Workflows;
import utils.ScreenShotOnFailListener;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ ScreenShotOnFailListener.class})
public class CleanUp
{
    private static Logger logger = Logger.getLogger(CleanUp.class);

    @Test(priority = 1)
    @Description("This test clean up download dir")
    public void cleanUpDownloadDir() throws IOException
    {
        // Clean download dir
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }

    @Test(priority = 2)
    @Description("This test delete contract: Approval workflow positive")
    public void deleteContract()
    {
        new DashboardPage().getSideBar().clickExecutedContracts().selectContract("Approval workflow positive");

        OpenedContract openedContract = new OpenedContract();

        openedContract.clickContractActionsMenu().clickDeleteContract().clickDelete();

        logger.info("Assert delete contract notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract Approval workflow positive has been deleted."));
    }

    @Test(priority = 3)
    @Description("This test delete workflow: Approval_WFL_AT")
    public void deleteWorkflow()
    {
        DashboardPage dashboardPage = new DashboardPage();

        Workflows workflowsTabPage = dashboardPage.getSideBar().clickAdministration().clickWorkflowsTab();

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
        Assert.assertTrue(Selenide.executeJavaScript("return $('.workflows-list .workflows-list__row').length === 1"));
    }

    @Test(priority = 4)
    @Description("This test delete 2 teams: Autotest_TEAM_3 [EDITED] and Team #2")
    public void deleteTeams()
    {
        AdministrationPage administrationPage = new AdministrationPage();

        Teams teamsTabPage = administrationPage.clickTeamsTab();

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

        LoginPage loginPage = new DashboardPage().getSideBar().logout(); // Logout
    }
}
