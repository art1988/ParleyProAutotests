package tests.regression.at161;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.EditTemplatePage;
import pages.TemplatesPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AddTemplateAndPublish
{
    @Test
    public void addTemplateAndPublish()
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(true);

        templatesPage.clickNewTemplate().clickUploadTemplatesButton(Const.REGRESSION_TEMPLATE_DOCX_AT161);

        $(".notification-stack").waitUntil(Condition.visible, 60_000).shouldHave(Condition.text("Template TemplateDOCXCapital_AT-161 was added."));

        EditTemplatePage editTemplatePage = templatesPage.selectTemplate("TemplateDOCXCapital_AT-161");
        Assert.assertEquals(editTemplatePage.getTitle(), "TemplateDOCXCapital_AT-161", "Title of opened template is wrong !!!");
        String wholeTextOfTemplate = editTemplatePage.getText();
        Assert.assertTrue(wholeTextOfTemplate.contains("CONTRACT AGREEMENT"));
        Assert.assertTrue(wholeTextOfTemplate.contains("Commonwealth employees or to breach"));
        Assert.assertTrue(wholeTextOfTemplate.contains("CONTRACTOR must also comply with the requirements"));

        editTemplatePage.clickPublishButton();
        $$(".template__status").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("Published"));

        Screenshoter.makeScreenshot();
    }
}
