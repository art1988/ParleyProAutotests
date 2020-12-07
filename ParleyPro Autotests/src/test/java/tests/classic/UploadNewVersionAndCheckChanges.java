package tests.classic;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DiscussionsOfSingleContract;
import pages.DocumentComparePreview;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ ScreenShotOnFailListener.class})
public class UploadNewVersionAndCheckChanges
{
    private String documentName = "AT-40";
    private String contractName = "AT40 Classic contract";

    private static Logger logger = Logger.getLogger(UploadNewVersionAndCheckChanges.class);

    @Test(priority = 1)
    @Description("This test uploads new version and checks compare preview")
    public void uploadNewVersionAndCheckChanges()
    {
        OpenedContract openedContract = new OpenedContract();

        DocumentComparePreview comparePreview = openedContract.clickUploadNewVersionButton(documentName).
                clickUploadCounterpartyDocument(Const.CONTRACT_CLASSIC_AT40_2, documentName, contractName);

        // TODO: add check of added comments after fixing of PAR-12858
        logger.info("Assert that counters are correct...");
        Assert.assertEquals(comparePreview.getCounterAdded(), "2");
        Assert.assertEquals(comparePreview.getCounterEdited(), "2");
        Assert.assertEquals(comparePreview.getCounterDeleted(), "1");

        logger.info("Assert that icons are correct for corresponded paragraphs");
        StringBuffer jsCode = new StringBuffer("var ar = $('.update-document__changes.ui-td .icon');");
        jsCode.append("var string = \"\";");
        jsCode.append("for( var i = 0; i < ar.length; i++ ) { string += ar[i].getAttribute(\"value\"); } ");
        jsCode.append("return string;");
        Assert.assertEquals(Selenide.executeJavaScript(jsCode.toString()), "deletedaddedaddededitededited");

        logger.info("Assert that there are only 4 opened discussions");
        DiscussionsOfSingleContract discussionsInContract = comparePreview.clickUpload(true);
        Assert.assertEquals(discussionsInContract.getDiscussionCount(), "4");
        $$(".discussion-list .discussion2.discussion2_collapsed_yes").shouldHave(CollectionCondition.size(4));

        logger.info("Assert that all discussions and indicator have new icon...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.discussion-indicator.has-new.negotiating').length"), Long.valueOf(5));

        Screenshoter.makeScreenshot();

        openedContract = discussionsInContract.clickDocumentsTab();
    }

    @Test(priority = 2)
    @Description("This test verify changes in document view")
    public void checkChangesInDocumentView() throws FileNotFoundException
    {
        OpenedContract openedContract = new OpenedContract();

        logger.info("Assert that first paragraph was deleted");
        Assert.assertEquals(Selenide.executeJavaScript("return $('del').eq(0).text()"), "Paragraph 1: Hello, delete me please");
        Assert.assertEquals(Selenide.executeJavaScript("return $('del')[0].style.color"), "rgb(181, 8, 46)");

        // TODO: Second paragraph was commented (after fixing). Add after fixing of PAR-12858

        logger.info("Assert that third paragraph was inserted");
        Assert.assertEquals(Selenide.executeJavaScript("return $('ins').eq(0).text()"), "Insert paragraph");
        Assert.assertEquals(Selenide.executeJavaScript("return $('ins').eq(0).css(\"color\")"), "rgb(68, 120, 208)");

        logger.info("Assert that 4th and 5th paragraphs were changed");
        Assert.assertEquals(Selenide.executeJavaScript("return $('del').eq(1).text()"), "above me");
        Assert.assertEquals(Selenide.executeJavaScript("return $('del')[1].style.color"), "rgb(181, 8, 46)");
        Assert.assertEquals(Selenide.executeJavaScript("return $('del').eq(2).text()"), "Multiple");
        Assert.assertEquals(Selenide.executeJavaScript("return $('del')[2].style.color"), "rgb(181, 8, 46)");

        Assert.assertEquals(Selenide.executeJavaScript("return $('ins').eq(1).text()"), "in this paragraph");
        Assert.assertEquals(Selenide.executeJavaScript("return $('ins').eq(1).css(\"color\")"), "rgb(68, 120, 208)");
        Assert.assertEquals(Selenide.executeJavaScript("return $('ins').eq(2).text()"), "Single");
        Assert.assertEquals(Selenide.executeJavaScript("return $('ins').eq(2).css(\"color\")"), "rgb(68, 120, 208)");

        Screenshoter.makeScreenshot();

        try
        {
            openedContract.clickDocumentActionsMenu(documentName).clickDownload(true).clickDownloadForMyTeam();

            logger.info("Assert that file was downloaded [ my team ]...");
            Thread.sleep(10_000);

            FilenameFilter filter = (f, name) -> name.endsWith(".docx");
            String[] pathnames = Const.DOWNLOAD_DIR.list(filter);

            Assert.assertTrue(pathnames.length == 1);
            Assert.assertTrue( pathnames[0].startsWith("AT-40_draft_") );
            Assert.assertTrue( pathnames[0].endsWith(".docx") );
            Assert.assertTrue( new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + pathnames[0]).exists() );
        }
        catch (FileNotFoundException | InterruptedException e)
        {
            logger.error("FileNotFoundException", e);
        }

        try
        {
            openedContract.clickDocumentActionsMenu(documentName).clickDownload(true).clickDownloadForCounterparty();

            logger.info("Assert that file was downloaded [ counterparty ]...");

            new WebDriverWait(WebDriverRunner.getWebDriver(), 20).
                    until(d -> Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), documentName + ".docx").toFile().exists());
        }
        catch (FileNotFoundException e)
        {
            logger.error("FileNotFoundException", e);
        }
    }

    @Test(priority = 3)
    @Description("This test clean up download dir")
    public void cleanUpDownloadDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
