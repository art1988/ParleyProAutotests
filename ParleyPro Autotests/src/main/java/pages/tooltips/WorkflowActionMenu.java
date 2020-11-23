package pages.tooltips;

import com.codeborne.selenide.Selenide;
import forms.DeleteWorkflow;
import org.apache.log4j.Logger;
import org.testng.Assert;

/**
 * Popup that appears after clicking on 3 dots button on Workflows tab page for given workflowName
 */
public class WorkflowActionMenu
{
    private String workflowName;


    private Logger logger = Logger.getLogger(WorkflowActionMenu.class);

    public WorkflowActionMenu(String workflowName)
    {
        try
        {
            Thread.sleep(1_000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        Assert.assertEquals(Selenide.executeJavaScript("return $('#dropdown-icon').next().text()"), "EditDelete");

        this.workflowName = workflowName;
    }

    /**
     * May return Approval form or Contract routing form
     */
    public void clickEdit()
    {
        Selenide.executeJavaScript("$('.dropdown-menu.dropdown-menu-right a:contains(\"Edit\")')[0].click()");

        logger.info("Edit was clicked");
    }

    public DeleteWorkflow clickDelete()
    {
        Selenide.executeJavaScript("$('.dropdown-menu.dropdown-menu-right a:contains(\"Delete\")')[0].click()");

        logger.info("Delete was clicked");

        return new DeleteWorkflow(workflowName);
    }
}
