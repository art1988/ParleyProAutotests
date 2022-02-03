package tests.classic.classic_runner;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.Discussions;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckContentAfterUploading
{
    private static Logger logger = Logger.getLogger(CheckContentAfterUploading.class);

    @Test
    @Parameters({"contractName", "numberOfDiscussions", "textOnFirstPage", "textOnLastPage"})
    public void checkContentAfterUploading(String contractName,
                                           String numberOfDiscussions,
                                           String textOnFirstPage,
                                           String textOnLastPage) throws InterruptedException
    {
        Discussions discussions = new Discussions(contractName);

        logger.info("Checking number of discussions...");
        Assert.assertTrue(Integer.parseInt(discussions.getDiscussionCount()) >= Integer.parseInt(numberOfDiscussions),
                "Looks like that discussions are empty or not greater or equal to " + numberOfDiscussions);
        //Assert.assertEquals(discussionsOfSingleContract.getDiscussionCount(), numberOfDiscussions);

        OpenedContract openedContract = discussions.clickDocumentsTab();
        logger.info("Waiting until spinner will disappear [up to 2 minute]...");
        $(".spinner").waitUntil(Condition.disappear, 60_000 * 2);
        $(".document__body .spinner").waitUntil(Condition.disappear, 60_000);

        logger.info("Checking text on first page...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"" + textOnFirstPage + "\")').length >= 1"), "Text on the first page is incorrect !!!");

        Screenshoter.makeScreenshot();

        logger.info("Scroll to bottom of page...");
        Selenide.executeJavaScript("document.querySelector('.documents__list').scrollTo(0,document.querySelector('.documents__list').scrollHeight)");
        Thread.sleep(2_000);

        logger.info("Checking text on last page...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"" + textOnLastPage + "\")').length >= 1"), "Text on the last page is incorrect !!!");

        Screenshoter.makeScreenshot();
    }
}
