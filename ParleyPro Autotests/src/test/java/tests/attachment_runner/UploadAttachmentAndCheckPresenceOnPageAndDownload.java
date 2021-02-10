package tests.attachment_runner;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AddDocuments;
import utils.Screenshoter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class UploadAttachmentAndCheckPresenceOnPageAndDownload
{
    private String attachmentFileName;
    private long fileSize;
    private static Logger logger = Logger.getLogger(UploadAttachmentAndCheckPresenceOnPageAndDownload.class);

    @Test(priority = 1)
    @Description("This test uploads attachment and checks that it was added on page")
    @Parameters("attachmentFileName")
    public void uploadAttachment(String attachmentFileName)
    {
        this.attachmentFileName = attachmentFileName;

        File fileToAttach = new File(Const.BAD_ATTACHMENTS_DOCS_DIR.getAbsolutePath() + "/" + attachmentFileName);
        fileSize = fileToAttach.length();

        logger.info("Uploading the following attachment: " + attachmentFileName);
        logger.info("File size in bytes: " + fileSize);
        new AddDocuments().clickUploadAttachmentTab().clickUploadFromYourComputer( new File[]{fileToAttach} );

        $(".notification-stack").waitUntil(Condition.visible, 20_000)
                .shouldHave(Condition.exactText("Attachment " + attachmentFileName + " has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 30_000);
    }

    @Test(priority = 2)
    @Description("This test checks that attachment was added on page")
    public void checkPresenceOnPage()
    {
        logger.info("Assert that 'Attachments' label is visible...");
        $(".supporting-documents__title").shouldBe(Condition.visible).shouldHave(Condition.exactText("attach_fileAttachments"));

        Assert.assertEquals($(".supporting-documents__document-name").getText(), attachmentFileName);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test downloads just uploaded attachment and checks file size")
    public void downloadAttachmentAndCheckSize()
    {
        try
        {
            $$(".supporting-documents__document-ico").shouldHave(CollectionCondition.size(1)).get(0).download();
            Thread.sleep(7_000);

            Assert.assertTrue(new WebDriverWait(WebDriverRunner.getWebDriver(), 20).
                    until(d -> Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), attachmentFileName).toFile().exists()));

            logger.info("Assert file size... Should be (in bytes) = " + fileSize);
            Assert.assertEquals(Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), attachmentFileName).toFile().length(), fileSize);
        }
        catch (FileNotFoundException | InterruptedException e)
        {
            logger.error("FileNotFoundException", e);
        }
    }

    @Test(priority = 4)
    @Description("This test clean up download dir")
    public void cleanUpDownloadDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
