package tests.formatting;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import tests.DeleteContractFromExecuted;
import utils.ScreenShotOnFailListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ ScreenShotOnFailListener.class})
public class OnlineFormat
{
    private static Logger logger = Logger.getLogger(DeleteContractFromExecuted.class);

    @Test
    @Description("This test choose MS Word formatting, cancels formatting")
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
    }
}
