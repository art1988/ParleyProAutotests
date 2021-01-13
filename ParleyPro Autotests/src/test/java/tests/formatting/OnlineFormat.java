package tests.formatting;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DocumentComparePreview;
import pages.OpenedContract;
import tests.DeleteContractFromExecuted;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class OnlineFormat
{
    private static Logger logger = Logger.getLogger(DeleteContractFromExecuted.class);

    @Test(priority = 1)
    @Description("This test choose MS Word formatting, downloads document, cancels formatting, downloads again and Uploads new version")
    public void onlineFormat()
    {
        OpenedContract openedContract = new OpenedContract();

        try
        {
            openedContract.clickDocumentActionsMenu("AT-40").clickFormat().clickMsWord().clickDownload();

            logger.info("Assert that file was downloaded...");
            Thread.sleep(10_000);

            FilenameFilter filter = (f, name) -> name.endsWith(".docx");
            String[] pathnames = Const.DOWNLOAD_DIR.list(filter);

            Assert.assertTrue(pathnames.length == 1); // only one file
            Assert.assertTrue( pathnames[0].startsWith("AT-40_draft_") );
            Assert.assertTrue( pathnames[0].endsWith(".docx") );
            Assert.assertTrue( new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + pathnames[0]).exists() );
        }
        catch (FileNotFoundException | InterruptedException e)
        {
            logger.error("FileNotFoundException", e);
        }

        logger.info("Making sure that FORMATTING label is visible...");
        $(".label.label_theme_lblue").waitUntil(Condition.visible, 16_000).shouldHave(Condition.exactText("FORMATTING"));

        Screenshoter.makeScreenshot();

        // cancel formatting...
        openedContract.clickDocumentActionsMenu("AT-40").clickCancelFormatting();
        logger.info("Making sure that FORMATTING label is gone...");
        $(".label.label_theme_lblue").waitUntil(Condition.disappear, 16_000).shouldNotBe(Condition.visible);

        logger.info("Download again via MS Word option...");
        try
        {
            openedContract.clickDocumentActionsMenu("AT-40").clickFormat().clickMsWord().clickDownload();

            logger.info("Assert that file was downloaded...");
            Thread.sleep(10_000);

            FilenameFilter filter = (f, name) -> name.endsWith(".docx");
            String[] pathnames = Const.DOWNLOAD_DIR.list(filter);

            Assert.assertTrue(pathnames.length == 2); // should be two files
            for( int i = 0; i < pathnames.length; i++ )
            {
                Assert.assertTrue( pathnames[i].startsWith("AT-40_draft_") );
                Assert.assertTrue( pathnames[i].endsWith(".docx") );
                Assert.assertTrue( new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + pathnames[i]).exists() );
            }
        }
        catch (FileNotFoundException | InterruptedException e)
        {
            logger.error("FileNotFoundException", e);
        }

        logger.info("Making sure that FORMATTING label is visible...");
        $(".label.label_theme_lblue").waitUntil(Condition.visible, 16_000).shouldHave(Condition.exactText("FORMATTING"));

        DocumentComparePreview documentComparePreview = openedContract.clickDocumentActionsMenu("AT-40")
                .clickUploadDocument().clickUploadDocumentButton( Const.CONTRACT_CLASSIC_AT40_2 );

        documentComparePreview.clickUpload(false);
    }

    @Test(priority = 2)
    @Description("This test verifies that changes were applied in document after uploading of new version of document")
    public void checkChangesInDocumentView() throws InterruptedException
    {
        logger.info("Verify changes in document view...");

        Thread.sleep(5_000);

        // JS code that removes paragraphs that contains only spaces and returns res array with text
        StringBuffer jsCode = new StringBuffer("var p = $('.document-paragraph__content-text'); ");
        jsCode.append("var res = []; ");
        jsCode.append("for( var i=0; i<p.length; i++ ) {");
        jsCode.append("if( /^(&nbsp;|\\s)*.$/.test(p[i].innerText) ) continue;");
        jsCode.append("res.push(p[i]); }");
        jsCode.append("return res;");

        ArrayList<RemoteWebElement> matchedParagraphs = Selenide.executeJavaScript(jsCode.toString());

        Assert.assertEquals(matchedParagraphs.get(0).getText(), "Paragraph 2: Create comment here");
        Assert.assertEquals(matchedParagraphs.get(1).getText(), "Insert paragraph");
        Assert.assertEquals(matchedParagraphs.get(2).getText(), "Paragraph 3: Insert something in this paragraph");
        Assert.assertEquals(matchedParagraphs.get(3).getText(), "Paragraph 4: Insert from clipboard below me");
        Assert.assertEquals(matchedParagraphs.get(4).getText(), "Paragraph 5: Single delete first");
        Assert.assertEquals(matchedParagraphs.get(5).getText(), "Paragraph 6: Multiple delete second");
        Assert.assertEquals(matchedParagraphs.get(6).getText(), "Paragraph 7: Unused extra paragraph");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test downloads document via document context menu")
    public void downloadDocument()
    {
        try
        {
            new OpenedContract().clickDocumentActionsMenu("AT-40").clickDownload(false);

            logger.info("Assert that file was downloaded...");
            Thread.sleep(10_000);

            FilenameFilter filter = (f, name) -> name.endsWith(".docx");
            String[] pathnames = Const.DOWNLOAD_DIR.list(filter);

            Assert.assertTrue(pathnames.length == 3); // should be three files
            for( int i = 0; i < pathnames.length; i++ )
            {
                Assert.assertTrue( pathnames[i].startsWith("AT-40_draft_") );
                Assert.assertTrue( pathnames[i].endsWith(".docx") );
                Assert.assertTrue( new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + pathnames[i]).exists() );
            }
        }
        catch (FileNotFoundException | InterruptedException e)
        {
            logger.error("FileNotFoundException", e);
        }
    }

    @Test(priority = 4)
    public void cleanUpDownloadDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
