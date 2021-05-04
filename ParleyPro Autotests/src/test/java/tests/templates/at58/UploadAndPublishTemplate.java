package tests.templates.at58;

import com.codeborne.selenide.Condition;
import constants.Const;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.EditTemplatePage;
import pages.TemplatesPage;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadAndPublishTemplate
{
    @Test
    public void uploadAndPublishTemplate()
    {
        TemplatesPage templatesPage = new SideBar().clickTemplates(true);

        templatesPage.clickNewTemplate().clickUploadTemplatesButton( Const.TEMPLATE_AT58 );

        $(".notification-stack").waitUntil(Condition.appear, 7_000).shouldHave(Condition.exactText("Template nurix_date_problem was added."));
        $(".notification-stack .notification__close").click();

        EditTemplatePage editTemplatePage = templatesPage.selectTemplate("nurix_date_problem");
        editTemplatePage.clickPublishButton();

        Screenshoter.makeScreenshot();
    }
}
