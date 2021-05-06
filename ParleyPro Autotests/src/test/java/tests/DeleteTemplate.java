package tests;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

/**
 * General test for deleting templates from 'Templates' page
 */
@Listeners({ScreenShotOnFailListener.class})
public class DeleteTemplate
{
    private static Logger logger = Logger.getLogger(DeleteTemplate.class);

    @Test
    @Parameters("templateName")
    public void deleteTemplate(String templateName)
    {
        new DashboardPage().getSideBar().clickTemplates(false).clickActionMenu(templateName).clickDelete().clickDelete();

        logger.info("Assert that delete template notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 45_000).shouldHave(Condition.exactText("Template " + templateName + " has been deleted."));
        $(".notification-stack .notification__close").click();
    }
}
