package tests.classic.upload_clients_docs;

import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DiscussionsOfSingleContract;
import pages.OpenedContract;

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
        DiscussionsOfSingleContract discussionsOfSingleContract = new DiscussionsOfSingleContract(contractName);

        logger.info("Checking number of discussions...");
        Assert.assertEquals(discussionsOfSingleContract.getDiscussionCount(), numberOfDiscussions);

        OpenedContract openedContract = discussionsOfSingleContract.clickDocumentsTab();

        logger.info("Checking text on first page...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"" + textOnFirstPage + "\")').length >= 1"));

        logger.info("Scroll to bottom of page...");
        Selenide.executeJavaScript("document.querySelector('.documents__list').scrollTo(0,document.querySelector('.documents__list').scrollHeight)");
        Thread.sleep(1_000);

        logger.info("Checking text on last page...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"" + textOnLastPage + "\")').length >= 1"));
    }
}