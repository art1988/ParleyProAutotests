package tests.fields.at177;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.EditTemplatePage;
import pages.TemplatesPage;
import tests.LoginBase;
import utils.Cache;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckThatT2DoesntHaveNurixSmartField
{
    private static Logger logger = Logger.getLogger(CheckThatT2DoesntHaveNurixSmartField.class);

    @Test(priority = 1)
    @Description("Test adds new template and checks that there is no smart field 'Nurix Contract No.' for tenant T2.")
    public void addTemplateAndCheck() throws InterruptedException
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates();

        templatesPage.clickNewTemplate().clickUploadTemplatesButton( Const.TEMPLATE_AT77 );
        $(".notification-stack").waitUntil(Condition.appear, 60_000).shouldHave(Condition.text(" was added."));

        EditTemplatePage editTemplatePage = templatesPage.selectTemplate("Template_AT-77_dummy");

        logger.info("Expand smart field dropdown...");
        $("#editor-toolbar a[title='Add a smart field']").shouldBe(Condition.visible).click();
        Thread.sleep(1_000);

        logger.info("Assert that there is NO 'Nurix Contract No.' smart field...");
        Assert.assertFalse(Selenide.executeJavaScript("return $($('.cke_combopanel > iframe')[0].contentDocument).find(\".cke_panel_list a\").text().includes(\"Nurix\")"),
                "Smart field 'Nurix Contract No.' is in the list, but shouldn't !!!");

        Screenshoter.makeScreenshot();

        editTemplatePage.clickCancelButton();

        logger.info("Deleting template...");
        templatesPage.clickActionMenuTemplate("Template_AT-77_dummy").clickDelete().clickDelete();
        $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.text(" has been deleted."));
    }

    @Test(priority = 2)
    @Description("Logout from T2, close current tab, switch to original tab with T1.")
    public void logoutFromT2()
    {
        new DashboardPage().getSideBar().logout();

        if( !LoginBase.isProd() )
        {
            logger.info("Switching to the original tab with T1 tenant...");
            WebDriverRunner.getWebDriver().switchTo().window(Cache.getInstance().getCachedCurrentTabHandle());
        }
    }
}
