package tests.templates;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.TemplateInformation;
import forms.editor_toolbar.Field;
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
public class CreateTemplateAT48
{
    private String templateName = "Template_AT48";
    private static Logger logger = Logger.getLogger(CreateTemplateAT48.class);

    @Test(priority = 1)
    @Description("This test creates template and publish")
    public void createTemplateAndPublish() throws InterruptedException
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(true);
        templatesPage.clickUploadTemplatesButton( Const.TEMPLATE_AT48 );

        $(".spinner").waitUntil(Condition.disappear, 7_000);

        EditTemplatePage editTemplatePage = templatesPage.selectTemplate(templateName);
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
        Assert.assertEquals(Selenide.executeJavaScript("return $('.template__title:contains(\"" +templateName + "\")').next().text()"), "Published");

        String description = "Some description of this template from autotest.";

        TemplateInformation templateInformation = templatesPage.clickActionMenu(templateName).clickTemplateInfo();
        templateInformation.setTemplateName( templateName += "[ EDITED ]" );
        templateInformation.setRegion("region1");
        templateInformation.setCategory("category1");
        templateInformation.setType("type1");
        templateInformation.clickIntegrationsCheckmark();
        templateInformation.setDescription(description);
        templateInformation.clickSave();

        logger.info("Assert that template name was changes and description was added in table view...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.template__title:contains(\"AT48\")').text()"), templateName + description);

        Screenshoter.makeScreenshot();

        logger.info("Edit just added template and check that all fields are saved...");
        templateInformation = templatesPage.clickActionMenu(templateName).clickTemplateInfo();

        Assert.assertEquals(templateInformation.getTemplateName(), templateName);
        Assert.assertEquals(templateInformation.getRegion(), "region1");
        Assert.assertEquals(templateInformation.getCategory(), "category1");
        Assert.assertEquals(templateInformation.getType(), "type1");

        templateInformation.clickCancel();
    }
}