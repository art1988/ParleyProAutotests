package tests.templates.at58;

import constants.Const;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.EditTemplatePage;
import pages.TemplatesPage;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class UploadAndPublishTemplate
{
    @Test
    public void uploadAndPublishTemplate()
    {
        TemplatesPage templatesPage = new SideBar().clickTemplates(true);

        templatesPage.clickUploadTemplatesButton(Const.TEMPLATE_AT58 );

        EditTemplatePage editTemplatePage = templatesPage.selectTemplate("nurix_date_problem");
        editTemplatePage.clickPublishButton();
    }
}
