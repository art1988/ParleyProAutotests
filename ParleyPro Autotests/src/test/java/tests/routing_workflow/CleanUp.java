package tests.routing_workflow;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.administration.Workflows;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

public class CleanUp
{
    private static Logger logger = Logger.getLogger(CleanUp.class);

    @Test(priority = 1)
    public void cleanUpDownloadDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }

    @Test(priority = 2)
    public void deleteContract()
    {
        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();

        logger.info("Assert delete contract notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract Contract routing workflow positive has been deleted."));
    }

    @Test(priority = 3)
    public void deleteWorkflow()
    {
        DashboardPage dashboardPage = new DashboardPage();

        Workflows workflowsTabPage = dashboardPage.getSideBar().clickAdministration().clickWorkflowsTab();

        workflowsTabPage.clickActionMenu("Contract_routing_WFL_AT").clickDelete().clickDelete();

        try
        {
            Thread.sleep(1_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        logger.info("Assert that there is no Contract routing workflow anymore...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.workflows-list .workflows-list__row').length === 1"));
    }
}
