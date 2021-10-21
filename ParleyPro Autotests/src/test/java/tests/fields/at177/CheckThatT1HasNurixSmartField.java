package tests.fields.at177;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.EditTemplatePage;
import pages.TemplatesPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

public class CheckThatT1HasNurixSmartField
{
    private static Logger logger = Logger.getLogger(CheckThatT1HasNurixSmartField.class);

    @Test
    @Description("Test adds new template and checks that smart field 'Nurix Contract No.' is in the list for T1 tenant.")
    public void addTemplateAndCheck() throws InterruptedException
    {
        TemplatesPage templatesPage =  new DashboardPage().getSideBar().clickTemplates(true);
        templatesPage.clickNewTemplate().clickUploadTemplatesButton( Const.TEMPLATE_AT77 );

        $(".notification-stack").waitUntil(Condition.appear, 60_000).shouldHave(Condition.text(" was added."));

        EditTemplatePage editTemplatePage = templatesPage.selectTemplate("Template_AT-77_dummy");

        logger.info("Expand smart field dropdown...");
        $("#editor-toolbar a[title='Add a smart field']").shouldBe(Condition.visible).click();
        Thread.sleep(1_000);

        logger.info("Assert that smart field 'Nurix Contract No.' is in the list...");
        Assert.assertTrue(Selenide.executeJavaScript("return $($('.cke_combopanel > iframe')[0].contentDocument).find(\".cke_panel_list a\").text().includes(\"Nurix\")"),
                "Smart field 'Nurix Contract No.' is not in the list !!!");

        Screenshoter.makeScreenshot();

        editTemplatePage.clickCancelButton();
    }
}
