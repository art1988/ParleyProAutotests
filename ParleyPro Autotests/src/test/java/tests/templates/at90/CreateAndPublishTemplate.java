package tests.templates.at90;

import constants.Const;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.TemplatesPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

@Listeners({ScreenShotOnFailListener.class})
public class CreateAndPublishTemplate
{
    @Test(priority = 1)
    public void createAndPublishTemplate()
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(true);

        templatesPage.clickUploadTemplatesButton( Const.TEMPLATE_AT90_SILENT_ERROR );

        templatesPage.selectTemplate("Template_silent_error_AT-90").clickPublishButton();

        Screenshoter.makeScreenshot();
    }
}
