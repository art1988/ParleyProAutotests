package tests.classic.upload_clients_docs;

import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DiscussionsOfSingleContract;
import pages.OpenedContract;

public class CheckTableAfterUploading
{
    private static Logger logger = Logger.getLogger(CheckTableAfterUploading.class);

    @Test
    @Parameters({"contractName", "numberOfDiscussions", "textInFirstRow", "textInLastRow"})
    public void checkTableAfterUploading(String contractName,
                                         String numberOfDiscussions,
                                         String textInFirstRow,
                                         String textInLastRow) throws InterruptedException
    {
        DiscussionsOfSingleContract discussionsOfSingleContract = new DiscussionsOfSingleContract(contractName);

        logger.info("Checking number of discussions...");
        Assert.assertEquals(discussionsOfSingleContract.getDiscussionCount(), numberOfDiscussions);

        OpenedContract openedContract = discussionsOfSingleContract.clickDocumentsTab();

        logger.info("Checking text in first table row...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text tbody td span:contains(\"" + textInFirstRow + "\")').length > 1"));

        logger.info("Scroll to bottom of page...");
        Selenide.executeJavaScript("document.querySelector('.documents__list').scrollTo(0,document.querySelector('.documents__list').scrollHeight)");
        Thread.sleep(1_000);

        logger.info("Checking text in last table row...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text tbody td span:contains(\"" + textInLastRow + "\")').length >= 1"));
    }
}