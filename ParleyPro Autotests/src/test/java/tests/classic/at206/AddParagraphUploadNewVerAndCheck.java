package tests.classic.at206;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.Discussions;
import pages.OpenedContract;
import utils.Cache;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AddParagraphUploadNewVerAndCheck
{
    private static final String textToPaste = "Affiliate(s) means Liberty Mutual Holding Company Inc. (“LMHC”), the ultimate parent of Customer, and any entity that directly or indirectly through one or more intermediaries is majority owned, or is controlled by, or is under common control with LMHC all of which comprise the Liberty Mutual Group of Companies. Further, any entity that becomes affiliated with LMHC after the Effective Date of the Agreement shall also be considered an Affiliate.";
    private static Logger logger = Logger.getLogger(AddParagraphUploadNewVerAndCheck.class);


    @Test(priority = 1)
    public void addParagraph() throws InterruptedException
    {
        new OpenedContract().hover("Agreement Term")
                            .clickAddParagraphAbove()
                            .setText(textToPaste)
                            .selectQueued()
                            .clickPost();

        logger.info("Check recalculation after posting...");
        $$(".discussion-indicator.queued").shouldHave(CollectionCondition.size(1));
        Assert.assertEquals($(".discussion-indicator.queued").closest(".document-paragraph__content").find("span[list-item='true']").text(),
                "1.1", "Queued post has wrong number !!! Should be 1.1 !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Agreement Term\")').closest('.document-paragraph__content').find('span[list-item=\"true\"]').text() === '1.2'"), "Recalculation is wrong !!! Should be 1.2");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"eGain\")').closest('.document-paragraph__content').find('span[list-item=\"true\"]').text() === '1.3'"), "Recalculation is wrong !!! Should be 1.3");
    }

    @Test(priority = 2)
    public void emailToCounterparty()
    {
        new OpenedContract().clickEmailContractToCounterparty("Applebook")
                            .clickNext()
                            .clickEmailContract();

        logger.info("Making sure that discussion become external...");
        $$(".document__body .discussion-indicator.negotiating").shouldHave(CollectionCondition.size(1));
        $$(".discussion-indicator.negotiating").shouldHave(CollectionCondition.size(3)); // including contract header and breadcrumb
    }

    @Test(priority = 3)
    @Description("Final check of AT-206 test: only one discussion has been created for item 1.1. Item 1.1 is shown as removed. The next items have been recalculated." +
            "Comment is present for the post.")
    public void uploadNewVerAndCheck()
    {
        String docName = "AT_206_eGain Master Agreement LM edits July_9";

        Discussions discussionsTab = new OpenedContract().clickUploadNewVersionButton(docName)
                                                         .clickUploadCounterpartyDocument(Const.TRACK_CHANGES_CLASSIC_AT206_V2, docName, Cache.getInstance().getCachedContractTitle())
                                                         .clickUpload(true);

        OpenedContract openedContract = discussionsTab.clickDocumentsTab();

        logger.info("Check recalculation...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Affiliate\")').closest('.document-paragraph__content').find('del').first().text() === '1.1'"), "Recalculation is wrong !!! Should be 1.1");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Agreement Term\")').closest('.document-paragraph__content').find('span[list-item=\"true\"]').text() === '1.1'"), "Recalculation is wrong !!! Should be 1.1");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"eGain\")').closest('.document-paragraph__content').find('span[list-item=\"true\"]').text() === '1.2'"), "Recalculation is wrong !!! Should be 1.2");

        logger.info("Checking that post was added only once...");
        openedContract.clickByDiscussionIconSoft("Affiliate");

        $$(".discussion2__body .discussion2-post").shouldHave(CollectionCondition.size(3)); // total posts
        $$(".discussion2__body .discussion2-post").filter(Condition.cssClass(".diff del")).stream().forEach(del -> del.shouldBe(Condition.visible));
        $$(".discussion2__body .discussion2-post").last().findAll(".diff del").shouldHave(CollectionCondition.size(2)); // the last post should have 2 <del>'s
        $$(".discussion2__body .discussion2-post").last().find(".discussion2-post__comment").shouldHave(Condition.text("Ss Note: Moving this to Glossary")); // comment is present

        logger.info("Checking that first paragraph was deleted on document view...");
        String wholeDelText = Selenide.executeJavaScript("return $('.document__body-content del').text()");
        Assert.assertTrue(wholeDelText.startsWith("1.1"), "Looks like that first paragraph wasn't deleted !!!");
        Assert.assertTrue(wholeDelText.contains("Affiliate") && wholeDelText.contains("LMHC") && wholeDelText.contains("Agreement shall also be considered"), "Looks like that first paragraph wasn't deleted !!!");

        Screenshoter.makeScreenshot();
    }
}
