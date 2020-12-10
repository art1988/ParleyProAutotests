package tests.templates;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.editor_toolbar.Field;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.EditTemplatePage;
import pages.TemplatesPage;

import static com.codeborne.selenide.Selenide.$;

public class CreateTemplateAT48
{
    private static Logger logger = Logger.getLogger(CreateTemplateAT48.class);

    @Test
    public void createTemplate() throws InterruptedException
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(true);
        templatesPage.clickUploadTemplatesButton( Const.TEMPLATE_AT48 );

        $(".spinner").waitUntil(Condition.disappear, 7_000);

        EditTemplatePage editTemplatePage = templatesPage.selectTemplate("Template_AT48");
        editTemplatePage.addSmartField("Contract name");
        editTemplatePage.addSmartField("Contract due date");
        editTemplatePage.addSmartField("Contract category");
        editTemplatePage.addSmartField("Contract region");

        Field field = editTemplatePage.addField();
        field.setFieldName("AT Custom_field");
        field.markRequiredFieldCheckbox();
        field.clickOk();

        editTemplatePage.clickPublishButton();

        logger.info("Making sure that status was changed to Published...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.template__title:contains(\"Template_AT48\")').next().text()"), "Published");


    }
}
