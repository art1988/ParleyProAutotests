package tests.templates.at86;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.TemplatesPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadPublishDownloadTemplate
{
    private String templateName = "Template_AT-86_text_cut_off";
    private File   downloadedTemplate;

    private static Logger logger = Logger.getLogger(UploadPublishDownloadTemplate.class);

    @Test(priority = 1)
    @Description("This test uploads template Template_AT-86_text_cut_off.docx and publish it.")
    public void uploadDocAsTemplateAndPublish()
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar()
                                                         .clickTemplates(true);

        templatesPage.clickNewTemplate()
                     .clickUploadTemplatesButton( Const.TEMPLATE_AT86 );

        new DashboardPage().getSideBar()
                           .clickTemplates(false)
                           .selectTemplate(templateName)
                           .clickPublishButton();

        logger.info("Assert that template was published...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.template__title:contains(\"" + templateName + "\")').next().text()"), "Published");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test downloads just added template.")
    public void downloadJustPublishedTemplate() throws IOException
    {
        new DashboardPage().getSideBar()
                           .clickTemplates(false)
                           .clickActionMenuTemplate(templateName)
                           .clickDownload();

        downloadedTemplate = Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), templateName + ".docx").toFile();

        new WebDriverWait(WebDriverRunner.getWebDriver(), 20).until(d -> downloadedTemplate.exists());
        Assert.assertTrue( downloadedTemplate.exists() );

        logger.info("Rename file to downloadedModified...");
        Path source = downloadedTemplate.toPath();
        Path target = Paths.get(downloadedTemplate.getParent() + "/" + "downloaded_Modified.docx");
        downloadedTemplate = Files.move(source, target).toFile();
    }

    @Test(priority = 3)
    @Description("This test uploads downloaded template with name downloaded_Modified.docx and publish it.")
    public void addNewTemplateOutOfDownloaded()
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar()
                                                         .clickTemplates(false);

        templatesPage.clickNewTemplate()
                     .clickUploadTemplatesButton( downloadedTemplate );

        $(".notification-stack").waitUntil(Condition.appear, 25_000).shouldHave(Condition.exactText("Template downloaded_Modified was added."));
        $(".notification-stack .notification__close").click();

        $(".spinner").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that template was added...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.template__title:contains(\"" + FilenameUtils.removeExtension(downloadedTemplate.getName()) + "\")').next().text()"), "Not published");

        logger.info("Make it publish...");
        templatesPage.selectTemplate( FilenameUtils.removeExtension(downloadedTemplate.getName()) ).clickPublishButton();
    }
}
