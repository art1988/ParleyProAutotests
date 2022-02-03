package tests.basics.attachments;

import com.codeborne.selenide.*;
import constants.Const;
import forms.delete.DeleteAttachment;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.AuditTrail;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.Screenshoter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class UploadAndDownloadAttachments
{
    private static Logger logger = Logger.getLogger(UploadAndDownloadAttachments.class);

    @Test(priority = 1)
    @Description("This test uploads 3 files from attachmentDocs folder: [file-sample_100kB.doc], [Software License Agreement.docx] and [Third Party License.pdf]")
    public void uploadAttachments()
    {
        new InProgressContractsPage(false).selectContract("Contract lifecycle autotest");

        AddDocuments addDocumentsPage = new AddDocuments();

        addDocumentsPage.clickUploadAttachmentTab();
        addDocumentsPage.clickUploadFromYourComputer( Const.ATTACHMENTS_DOCS_DIR.listFiles() );

        logger.info("Check that Attachments label appeared...");
        $(".supporting-documents__title").waitUntil(Condition.appear, 7_000).shouldHave(Condition.exactText("attach_fileAttachments"));

        logger.info("Checking that 3 notification pop-up's were displayed...");
        $(".notification-stack").waitUntil(Condition.appear, 7_000);
        $$(".notification-stack .notification-stack__item").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.textsInAnyOrder("Attachment file-sample_100kB.doc has been successfully uploaded.",
                                                                "Attachment Third Party License.pdf has been successfully uploaded.",
                                                                "Attachment Software License Agreement.docx has been successfully uploaded."));

        logger.info("Checking that 3 icons of uploaded docs were displayed...");
        $$(".supporting-documents__document.allow_download").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.textsInAnyOrder("Software License Agreement.docx\n36.34 KB",
                                                                "Third Party License.pdf\n40.02 KB",
                                                                "file-sample_100kB.doc\n98 KB"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test downloads files that were attached in previous step")
    public void downloadAttachments()
    {
        for( int i = 0; i < 3; i++ )
        {
            String fileName = $$(".supporting-documents__list .supporting-documents__document-name").get(i).getText();

            // click by icon to initiate download
            try
            {
                $$(".supporting-documents__list .supporting-documents__document-ico").get(i).download();
                Thread.sleep(7_000);

                Assert.assertTrue(new WebDriverWait(WebDriverRunner.getWebDriver(), 20).
                        until(d -> Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), fileName).toFile().exists()));
            }
            catch (FileNotFoundException | InterruptedException e)
            {
                logger.error("FileNotFoundException or InterruptedException", e);
            }
        }
    }

    @Test(priority = 3)
    @Description("This test deletes attachment that ends with .doc")
    public void deleteAttachment() throws InterruptedException
    {
        for( int i = 0; i < 3; i++ )
        {
            SelenideElement attachment = $$(".supporting-documents__list .supporting-documents__document .supporting-documents__document-name").get(i);

            if( attachment.getText().endsWith(".doc") )
            {
                attachment.parent().parent().hover().find(".supporting-documents__document-remove").click();
                new DeleteAttachment(attachment.getText()).clickDelete();

                Thread.sleep(3_000);

                logger.info("Checking that only 2 attachments are present in the list...");
                $$(".supporting-documents__document.allow_download").shouldHave(CollectionCondition.size(2));

                Screenshoter.makeScreenshot();

                return;
            }
        }

        Assert.fail("Attachment with .doc wasn't found !");
    }

    @Test(priority = 4)
    public void checkAuditTrail()
    {
        AuditTrail auditTrail = new OpenedContract().clickAuditTrail();

        Assert.assertEquals(Selenide.executeJavaScript("return $('.timeline-title').text()"),
                "Attachment DeletedAttachment UploadedAttachment UploadedAttachment UploadedContract taggedContract created");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.timeline-title:contains(\"Attachment Deleted\")').parent().parent().find(\".timeline-body\").text()"),
                "Document name: file-sample_100kB.doc");
        $$(".timeline-title").filter(Condition.exactText("Attachment Uploaded")).shouldHave(CollectionCondition.size(3));

        Screenshoter.makeScreenshot();

        auditTrail.clickOk();
    }

    @Test(priority = 5)
    @Description("This test clean up download dir")
    public void cleanUpDownloadDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
