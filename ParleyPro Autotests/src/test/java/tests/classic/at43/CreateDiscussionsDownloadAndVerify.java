package tests.classic.at43;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateDiscussionsDownloadAndVerify
{
    private static Logger logger = Logger.getLogger(CreateDiscussionsDownloadAndVerify.class);

    @Test
    public void changeParagraph_DownloadAsCounterparty_VerifyDownloadedFiles() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        openedContract.clickByParagraph("Hello, delete me please")
                      .setText(" Some added text for this parahraph")
                      .setComment("Some comment Q1")
                      .selectQueued()
                      .clickPost();

        $(".notification-stack").waitUntil(Condition.appear, 7_000).shouldHave(Condition.text("Queued discussion Paragraph 1"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        downloadForCounterparty(openedContract);

        logger.info("Assert that EXTERNAL post was added...");
        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon("Paragraph 1");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(2)); // Amount of total posts
        $$(".discussion2-post").last().find(".discussion2-label__status").shouldHave(Condition.exactText("EXTERNAL"));
        openedDiscussion.close();

        openedContract.clickByParagraph("Some added text")
                      .setText(" New TEXT #2")
                      .setComment("Some comment Internal2")
                      .selectInternal()
                      .clickPost();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Internal post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        downloadForCounterparty(openedContract);

        logger.info("Assert that last post was added as INTERNAL...");
        openedDiscussion = openedContract.clickByDiscussionIcon("Paragraph 1");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(3)); // Amount of total posts
        $$(".discussion2-post").last().find(".discussion2-label__status").shouldHave(Condition.exactText("INTERNAL"));

        logger.info("Make last internal post queued...");
        openedDiscussion.clickMakeQueued("New TEXT #2");

        logger.info("Assert that last post become queued...");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(4)); // Amount of total posts
        $$(".discussion2-post").last().find(".discussion2-label__status").shouldHave(Condition.exactText("queued"));
        Screenshoter.makeScreenshot();
        openedDiscussion.close();

        downloadForCounterparty(openedContract);
    }

    private void downloadForCounterparty(OpenedContract openedContract)
    {
        try
        {
            openedContract.clickDocumentActionsMenu("AT-14")
                          .clickDownload(true)
                          .clickDownloadForCounterparty();

            Thread.sleep(1_000);
            logger.info("Assert that file was downloaded...");

            Assert.assertTrue(new WebDriverWait(WebDriverRunner.getWebDriver(), 20).
                    until(d -> Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), "AT-14.docx").toFile().exists()),
                    "Looks like that it is unable to download file as counterparty !!!");
        }
        catch (FileNotFoundException | InterruptedException e)
        {
            logger.error("FileNotFoundException", e);
        }

        logger.info("Clean up download folder...");
        try
        {
            FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
        }
        catch (IOException e)
        {
            logger.error("IOException", e);
        }
    }
}
