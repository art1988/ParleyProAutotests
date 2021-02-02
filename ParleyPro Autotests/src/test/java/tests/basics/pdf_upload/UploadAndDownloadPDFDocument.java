package tests.basics.pdf_upload;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInNegotiation;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadAndDownloadPDFDocument
{
    private String docName;
    private static Logger logger = Logger.getLogger(UploadAndDownloadPDFDocument.class);

    @Test(priority = 1)
    @Parameters("documentName")
    public void uploadPDFDocument(String documentName)
    {
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(false);
        inProgressContractsPage.selectContract("Contract lifecycle autotest");

        AddDocuments addDocuments = new AddDocuments();
        addDocuments.clickUploadCounterpartyDocuments( new File(Const.PDF_DOCS_DIR.getAbsolutePath() + "/" + documentName) );

        docName = documentName.substring(0, documentName.indexOf("."));
        logger.info("Assert upload notification...");
        $(".notification-stack").waitUntil(Condition.visible, 25_000).shouldHave(Condition.exactText("Document " + docName + " has been successfully uploaded."));

        new ContractInNegotiation("Contract lifecycle autotest").clickOk();

        logger.info("Assert that both contract and document icons have NEGOTIATE status");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        logger.info("Assert that both contract and document icons have purple color");
        $$(".lifecycle__item.active").first().getCssValue("background-color").equals("#7f6fcf;");
        $$(".lifecycle__item.active").last().getCssValue("background-color").equals("#7f6fcf;");

        logger.info("Waiting until document is fully loaded...");
        $(".document__body .spinner").waitUntil(Condition.disappear, 30_000);

        $$(".document__body canvas").first().waitUntil(Condition.visible, 10_000);
        $$(".document__body canvas").last().waitUntil(Condition.visible, 10_000);
        $$(".document__body canvas").shouldBe(CollectionCondition.sizeGreaterThan(2));

        logger.info("Check that PDF icon is displayed in document header...");
        WebElement icon = Selenide.executeJavaScript("return $('.document__header-row svg').find(\"path[fill-rule='evenodd']\")[0]");
        $(icon).should(Condition.exist);

        logger.info("Check that Start discussion icon is visible...");
        $(".btn-blue.document__pdf-discussion-button").shouldBe(Condition.visible);

        logger.info("Check that 3RD PARTY label is visible...");
        $(".label.label_theme_dgray").shouldBe(Condition.visible).shouldHave(Condition.exactText("3RD PARTY"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void downloadPDFDocument()
    {
        OpenedContract openedContract = new OpenedContract();

        try
        {
            openedContract.clickDocumentActionsMenu(docName).clickDownload(false);
            $(".spinner").waitUntil(Condition.disappear, 20_000);

            logger.info("Assert that file was downloaded...");
            Thread.sleep(1_000);

            FilenameFilter filter = (f, name) -> name.endsWith(".pdf");
            String[] pathnames = Const.DOWNLOAD_DIR.list(filter);

            Assert.assertTrue(pathnames.length == 1);
            Assert.assertTrue( pathnames[0].startsWith(docName) );
            Assert.assertTrue( pathnames[0].endsWith(".pdf") );
            Assert.assertTrue( new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + pathnames[0]).exists() );
        }
        catch (FileNotFoundException | InterruptedException e)
        {
            logger.error("FileNotFoundException or InterruptedException occurred", e);
        }
    }

    @Test(priority = 3)
    @Description("This test clean up download dir")
    public void cleanUpDownloadDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
