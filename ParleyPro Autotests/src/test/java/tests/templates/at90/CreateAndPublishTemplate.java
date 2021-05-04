package tests.templates.at90;

import com.codeborne.selenide.Condition;
import constants.Const;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.TemplatesPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateAndPublishTemplate
{
    @Test(priority = 1)
    public void createAndPublishTemplate()
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(true);

        templatesPage.clickNewTemplate().clickUploadTemplatesButton( Const.TEMPLATE_AT90_SILENT_ERROR );

        $(".notification-stack").waitUntil(Condition.appear, 7_000).shouldHave(Condition.exactText("Template Template_silent_error_AT-90 was added."));
        $(".notification-stack .notification__close").click();

        templatesPage.selectTemplate("Template_silent_error_AT-90").clickPublishButton();

        Screenshoter.makeScreenshot();
    }
}
