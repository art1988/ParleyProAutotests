package tests.formatting.at190;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.EditDocumentPage;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class FormatAndChangeTextColorAndDownload
{
    private static Logger logger = Logger.getLogger(FormatAndChangeTextColorAndDownload.class);


    @Test(priority = 1)
    @Description("This test selects all content from doc and sets black color.")
    public void formatOnline() throws InterruptedException
    {
        EditDocumentPage editDocumentPage = new OpenedContract().clickDocumentActionsMenu("AT_190-SoW_CASS_Hybrid-Platform_2022_from Patrick")
                                                                .clickFormat()
                                                                .clickOnline();

        logger.info("Selecting all text via (Ctrl+A)...");
        editDocumentPage.getEditorBodyInstance().sendKeys(Keys.chord(Keys.CONTROL, "a"));
        Thread.sleep(1_000);

        editDocumentPage.clickByTextColor().selectColor("000000");
        Thread.sleep(1_000);

        editDocumentPage.clickSave();
        Thread.sleep(5_000);

        logger.info("Making sure that color has been changed to black...");
        $$(".document-paragraph__content-text").filter(Condition.text("LASCAS 2022"))
                .forEach(div -> Assert.assertEquals(div.findAll("span").last().getCssValue("color"), "rgba(0, 0, 0, 1)", "Looks like that color has not changed !!!"));
    }

    @Test(priority = 2)
    @Description("This test downloads just edited doc.")
    public void download() throws IOException, InterruptedException
    {
        new OpenedContract().clickDocumentActionsMenu("AT_190-SoW_CASS_Hybrid-Platform_2022_from Patrick")
                            .clickDownload(false);

        logger.info("Assert that file was downloaded...");
        Thread.sleep(10_000);

        FilenameFilter filter = (f, name) -> name.endsWith(".docx");
        String[] pathnames = Const.DOWNLOAD_DIR.list(filter);

        File downloadedFile = null;
        for( int i = 0; i < pathnames.length; i++ )
        {
            if( pathnames[i].startsWith("AT_190-SoW_CASS") )
            {
                Assert.assertTrue( pathnames[i].endsWith(".docx") );
                downloadedFile = new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + pathnames[i]);
                Assert.assertTrue( downloadedFile.exists() );

                break;
            }

            Assert.fail("Looks like that file has not been downloaded !!!");
        }

        logger.info("Rename just downloaded file...");
        String fileName = "downloaded_AT_190-SoW_CASS.docx";
        Cache.getInstance().setFile(fileName);

        Path source = Optional.ofNullable(downloadedFile).get().toPath();
        Path target = Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + fileName);

        File renamed = Files.move(source, target).toFile();
        Assert.assertTrue(renamed.exists(), "Can't rename file !");
    }

    @Test(priority = 3)
    public void deletePreviousDoc()
    {
        new OpenedContract().clickDocumentActionsMenu("AT_190-SoW_CASS_Hybrid-Platform_2022_from Patrick")
                            .clickDelete().clickDelete();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" has been deleted."));
    }
}
