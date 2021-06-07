package tests.templates.at135;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.DashboardPage;
import pages.EditTemplatePage;
import pages.TemplatesPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.util.Set;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddTemplateCopyLinkAndOpen
{
    private SoftAssert softAssert = new SoftAssert();
    private static Logger logger = Logger.getLogger(AddTemplateCopyLinkAndOpen.class);

    @Test(priority = 1)
    public void addTemplate() throws InterruptedException
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar()
                                                         .clickTemplates(true)
                                                         .clickTemplatesTab();

        templatesPage.clickNewTemplate()
                     .clickUploadTemplatesButton(Const.TEMPLATE_AT135);

        $(".notification-stack").waitUntil(Condition.appear, 25_000).shouldHave(Condition.exactText("Template AT-135_Template_identical was added."));
        $(".notification-stack .notification__close").click();

        EditTemplatePage editTemplatePage = templatesPage.selectTemplate("AT-135_Template_identical");

        WebDriver driver = WebDriverRunner.getWebDriver();
        String URLOfSelectedTemplate = driver.getCurrentUrl();

        logger.info("Open new tab...");
        String currentHandle = driver.getWindowHandle();
        Selenide.executeJavaScript("window.open()");

        Set<String> handles = driver.getWindowHandles();
        for ( String actual : handles )
        {
            if ( !actual.equalsIgnoreCase(currentHandle) )
            {
                //switching to the opened tab
                driver.switchTo().window(actual);

                logger.info("Open saved URL in that tab...");
                driver.get(URLOfSelectedTemplate);
            }
        }

        EditTemplatePage openedTemplate = new EditTemplatePage();
        logger.info("Assert that opened template is the same...");
        softAssert.assertEquals(openedTemplate.getTitle(), "AT-135_Template_identical", "Looks like that opened template is NOT the same !!!");
        softAssert.assertEquals(openedTemplate.getText(), "Text, and identical text.", "Looks like that opened template is NOT the same !!!");

        Screenshoter.makeScreenshotOfWholeScreen();
        openedTemplate.clickCancelButton();

        softAssert.assertAll();

        driver.close(); // close tab
        driver.switchTo().window(currentHandle); //switching to original tab

        editTemplatePage.clickCancelButton();
    }

    @AfterMethod
    public void cleanUp()
    {
        new DashboardPage().getSideBar()
                           .clickTemplates(false)
                           .clickActionMenuTemplate("AT-135_Template_identical")
                           .clickDelete()
                           .clickDelete();

        $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.text(" has been deleted."));
        $(".notification-stack").waitUntil(Condition.disappear, 30_000);
    }
}
