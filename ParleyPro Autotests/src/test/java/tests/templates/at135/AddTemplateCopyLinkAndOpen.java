package tests.templates.at135;

import com.codeborne.selenide.Condition;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.EditTemplatePage;
import pages.TemplatesPage;
import utils.ScreenShotOnFailListener;

import java.util.Set;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddTemplateCopyLinkAndOpen
{
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

        Thread.sleep(5_000);
    }
}
