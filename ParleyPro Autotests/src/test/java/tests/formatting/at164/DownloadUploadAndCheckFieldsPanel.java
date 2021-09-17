package tests.formatting.at164;

import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class DownloadUploadAndCheckFieldsPanel
{
    private static Logger logger = Logger.getLogger(DownloadUploadAndCheckFieldsPanel.class);

    @Test(priority = 1)
    public void formatMSWordAndDownload() throws IOException, InterruptedException
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("AT-164 // CTR - Document moves from Review to Draft");

        Thread.sleep(2_000);

        OpenedContract openedContract = new OpenedContract();

        openedContract.clickDocumentActionsMenu("Template_AT-164-Manufacturing_Agreement").clickFormat().clickMsWord().clickDownload();

        logger.info("Assert that file was downloaded...");
        Thread.sleep(5_000);

        try (Stream<Path> stream = Files.list(Paths.get(Const.DOWNLOAD_DIR.getPath())))
        {
            Optional<String> matchingFile = stream.map(Path::getFileName)
                                                  .map(Path::toString)
                                                  .filter(file -> file.startsWith("Template_AT-164"))
                                                  .findFirst();

            String downloadedFile = matchingFile.get();
            Assert.assertTrue(new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/" + downloadedFile).exists(), "Looks like that " + downloadedFile + " has not been downloaded !!!");
        }

        logger.info("Assert that FORMATTING label was shown...");
        $(".document__header-row").find("span[class*='label_theme_lblue']").shouldBe(Condition.visible).shouldHave(Condition.exactText("FORMATTING"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void uploadNewDoc()
    {
        OpenedContract openedContract = new OpenedContract();

        openedContract.clickDocumentActionsMenu("Template_AT-164-Manufacturing_Agreement")
                      .clickUploadDocument()
                      .clickUploadDocumentButton( Const.TEMPLATE_TO_UPLOAD_OVER_AT164 )
                      .clickUpload(false);

        logger.info("Making sure that Filed panel has disappeared...");
        $(".documents__column .documents-placeholders__title").waitUntil(Condition.hidden, 30_000);
        $(".documents__column .documents-placeholders__title").shouldBe(Condition.hidden); // only Fields panel has '.documents-placeholders__title'

        logger.info("Making sure that there is no Formatting label anymore...");
        $(".document__header-row").find("span[class*='label_theme_lblue']").should(Condition.disappear);

        Screenshoter.makeScreenshot();
    }
}
