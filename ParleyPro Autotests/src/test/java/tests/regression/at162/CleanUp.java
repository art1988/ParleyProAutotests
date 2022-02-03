package tests.regression.at162;

import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;


public class CleanUp
{
    private static String workflowName = "Prior to Sign";
    private static Logger logger = Logger.getLogger(CleanUp.class);

    @Test
    public void removeWorkflow()
    {
        new DashboardPage().getSideBar()
                           .clickAdministration()
                           .clickWorkflowsTab()
                           .clickActionMenu(workflowName)
                           .clickDelete()
                           .clickDelete();

        logger.info("Assert that there is no workflow in the list...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.workflows-list__row').length === 1"));
    }
}
