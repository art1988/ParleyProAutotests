package tests.templates.at189;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.EditTemplatePage;
import pages.TemplatesPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadTemplateAndCheck
{
    private static Logger logger = Logger.getLogger(UploadTemplateAndCheck.class);


    @Test
    @Description("This test creates template with enabled 'integrations' checkbox, opens it and checks that no error happened.")
    public void uploadTemplateAndCheck()
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(true);
        templatesPage.clickNewTemplate().clickUploadTemplatesButton( Const.TEMPLATE_AT135 );

        $(".notification-stack").waitUntil(Condition.appear, 60_000).shouldHave(Condition.exactText("Template AT-135_Template_identical was added."));

        templatesPage.clickActionMenuTemplate("AT-135_Template_identical")
                     .clickTemplateInfo()
                     .clickIntegrationsCheckmark()
                     .clickSave();

        logger.info("Making sure that templates has been opened without errors...");

        EditTemplatePage editTemplatePage = templatesPage.selectTemplate("AT-135_Template_identical");

        Assert.assertEquals(editTemplatePage.getText(), "Text, and identical text.", "Text is missing in editor area !!!");

        Screenshoter.makeScreenshot();

        editTemplatePage.clickCancelButton();
    }
}
