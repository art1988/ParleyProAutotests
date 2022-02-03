package tests.approval_workflow.at202;

import com.codeborne.selenide.Selenide;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;


public class CleanUp
{
    @Test
    public void removeWorkflow()
    {
        new DashboardPage().getSideBar()
                           .clickAdministration()
                           .clickWorkflowsTab()
                           .clickActionMenu("AT-202 approval wkfl")
                           .clickDelete()
                           .clickDelete();

        Assert.assertTrue(Selenide.executeJavaScript("return $('.workflows-list__row').length === 1"),
                "Workflow 'AT-202 approval wkfl' is still in the list !!!");
    }
}
