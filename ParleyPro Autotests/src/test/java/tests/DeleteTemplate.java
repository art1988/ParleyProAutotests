package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DashboardPage;

import static com.codeborne.selenide.Selenide.$;

/**
 * General test for deleting templates from 'Templates' page
 */

public class DeleteTemplate
{
    private static Logger logger = Logger.getLogger(DeleteTemplate.class);

    @Test
    @Parameters("templateName")
    public void deleteTemplate(String templateName) throws InterruptedException
    {
        // Before deletion - refresh page, because previous opened modal forms may still be active preventing clicking by sidebar icons
        Selenide.refresh();

        Thread.sleep(3_000);

        new DashboardPage().getSideBar()
                           .clickTemplates(false)
                           .clickActionMenuTemplate(templateName)
                           .clickDelete()
                           .clickDelete();

        logger.info("Assert that delete template notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 45_000).shouldHave(Condition.exactText("Template " + templateName + " has been deleted."));
        $(".notification-stack .notification__close").click();
    }
}
