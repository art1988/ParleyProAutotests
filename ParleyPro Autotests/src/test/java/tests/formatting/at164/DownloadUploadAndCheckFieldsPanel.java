package tests.formatting.at164;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.io.FileNotFoundException;
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
}
