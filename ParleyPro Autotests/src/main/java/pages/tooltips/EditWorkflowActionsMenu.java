package pages.tooltips;

import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;

/**
 * Popup that appears after clicking on 3 dots button on Workflows tab page
 */
public class EditWorkflowActionsMenu
{
    private Logger logger = Logger.getLogger(EditWorkflowActionsMenu.class);

    public EditWorkflowActionsMenu()
    {
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        Assert.assertEquals(Selenide.executeJavaScript("return $('#dropdown-icon').next().text()"), "EditDelete");
    }

    /**
     * May return Approval form or Contract routing form
     */
    public void clickEdit()
    {
        Selenide.executeJavaScript("$('.dropdown-menu.dropdown-menu-right a:contains(\"Edit\")')[0].click()");

        logger.info("Edit was clicked");
    }
}
