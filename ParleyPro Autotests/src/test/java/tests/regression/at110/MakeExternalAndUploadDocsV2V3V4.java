package tests.regression.at110;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class MakeExternalAndUploadDocsV2V3V4
{
    private String contractName = "Track changes AT-110";
    private String documentName = "Acorns Engagement Letter_party_changes_v1";

    private static Logger logger = Logger.getLogger(MakeExternalAndUploadDocsV2V3V4.class);

    @Test(priority = 1)
    public void makeDiscussionExternal()
    {
        new OpenedContract().clickManageDiscussions()
                            .makeExternalAllInternalDiscussions()
                            .confirmMakeExternalForTheFirstTime(contractName)
                            .clickStart();

        $(".notification-stack").waitUntil(Condition.appear, 6_000)
                .shouldHave(Condition.exactText("Contract " + contractName + " is now in negotiation. No notification was sent to the Counterparty."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        // Scroll to 4. Limitations on Damages and Indemnification.
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"Confidentiality.\")')[0].scrollIntoView({});");

        new OpenedContract(true).clickByDiscussionIcon("Limitations on Damages and Indemnification");

        logger.info("Assert that discussion become external...");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(2)); // Amount of total posts
        $$(".discussion2-post").last().find(".discussion2-label__status").shouldHave(Condition.exactText("EXTERNAL"));

        Screenshoter.makeScreenshot();

        Selenide.refresh();
        new OpenedContract();
    }

    @Test(priority = 2)
    public void uploadDocV2AsCounterparty()
    {
        new OpenedContract().clickUploadNewVersionButton(documentName)
                            .clickUploadCounterpartyDocument(Const.TRACK_CHANGES_AT110_V2, documentName, contractName)
                            .clickUpload(true)
                            .clickDocumentsTab();

        logger.info("Assert that contract has 15 discussions...");
        Assert.assertEquals(new OpenedContract().getAmountOfContractDiscussion(), "15", "Amount of total discussions should be equal 15 !!!");

        // Scroll to 4. Limitations on Damages and Indemnification.
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"Confidentiality.\")')[0].scrollIntoView({});");

        new OpenedContract(true).clickByDiscussionIcon("Limitations on Damages and Indemnification");
        // Scroll discussion right panel to the bottom
        Selenide.executeJavaScript("document.querySelector('.discussion2__body__scrollable-body').scrollTo(0,document.querySelector('.discussion2__body__scrollable-body').scrollHeight)");

        logger.info("Making sure that new post was created...");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(3)); // Amount of total posts
        $$(".discussion2-post").last().find(".discussion2-label__status").shouldHave(Condition.exactText("EXTERNAL"));
        $$(".discussion2-post").last().find("del").shouldHave(Condition.text("will not"));
        $$(".discussion2-post").last().$$("ins").shouldBe(CollectionCondition.sizeGreaterThanOrEqual(3)); // amount of ins 3 or more

        Screenshoter.makeScreenshot();

        Selenide.refresh();
        new OpenedContract();
    }

    @Test(priority = 3)
    public void uploadDocV3AsMyTeam()
    {
        new OpenedContract().clickUploadNewVersionButton(documentName)
                            .clickUploadMyTeamDocument(Const.TRACK_CHANGES_AT110_V3, documentName, contractName)
                            .clickUpload(true)
                            .clickDocumentsTab();

        logger.info("Assert that contract has 10 discussions...");
        Assert.assertEquals(new OpenedContract().getAmountOfContractDiscussion(), "10", "Amount of total discussions should be equal 10 !!!");

        // Scroll to 4. Limitations on Damages and Indemnification.
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"Confidentiality.\")')[0].scrollIntoView({});");

        new OpenedContract(true).clickByDiscussionIcon("Limitations on Damages and Indemnification");
        // Scroll discussion right panel to the bottom
        Selenide.executeJavaScript("document.querySelector('.discussion2__body__scrollable-body').scrollTo(0,document.querySelector('.discussion2__body__scrollable-body').scrollHeight)");

        logger.info("Making sure that new post was created...");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(4)); // Amount of total posts
        $$(".discussion2-post").last().find(".discussion2-label__status").shouldHave(Condition.exactText("INTERNAL"));
        $$(".discussion2-post").last().find("del").shouldHave(Condition.exactText("2x"));
        $$(".discussion2-post").last().find("ins").shouldHave(Condition.exactText("10x"));

        Screenshoter.makeScreenshot();

        Selenide.refresh();
        new OpenedContract();
    }

    @Test(priority = 4)
    public void makeLastPostAsQueued()
    {
        // Scroll to 4. Limitations on Damages and Indemnification.
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"Confidentiality.\")')[0].scrollIntoView({});");

        new OpenedContract(true).clickByDiscussionIcon("Limitations on Damages and Indemnification")
                                               .clickMakeQueued("10x");

        logger.info("Making sure that queued has been created...");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(5)); // Amount of total posts
        $$(".discussion2-post").last().find(".discussion2-label__status").shouldHave(Condition.exactText("queued"));

        Screenshoter.makeScreenshot();

        Selenide.refresh();
        new OpenedContract();
    }

    @Test(priority = 5)
    public void makeQueuedPostAsExternal()
    {
        new OpenedContract().clickManageDiscussions()
                            .makeExternalAllQueuedDiscussions()
                            .confirmMakeExternalRegularly()
                            .clickDone();

        // Scroll to 4. Limitations on Damages and Indemnification.
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"Confidentiality.\")')[0].scrollIntoView({});");

        new OpenedContract(true).clickByDiscussionIcon("Limitations on Damages and Indemnification");
        // Scroll discussion right panel to the bottom
        Selenide.executeJavaScript("document.querySelector('.discussion2__body__scrollable-body').scrollTo(0,document.querySelector('.discussion2__body__scrollable-body').scrollHeight)");

        logger.info("Confirm that EXTERNAL post has been created...");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(6)); // Amount of total posts
        $$(".discussion2-post").last().find(".discussion2-label__status").shouldHave(Condition.exactText("EXTERNAL"));

        Screenshoter.makeScreenshot();

        Selenide.refresh();
        new OpenedContract();
    }

    @Test(priority = 6)
    public void uploadFinalDocV4AndCheck()
    {
        new OpenedContract().clickUploadNewVersionButton(documentName)
                            .clickUploadCounterpartyDocument(Const.TRACK_CHANGES_AT110_V4, documentName, contractName)
                            .clickUpload(true)
                            .clickDocumentsTab();

        logger.info("Assert that contract has 10 discussions...");
        Assert.assertEquals(new OpenedContract().getAmountOfContractDiscussion(), "10", "Amount of total discussions should be equal 10 !!!");

        // Scroll to 4. Limitations on Damages and Indemnification.
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"Confidentiality.\")')[0].scrollIntoView({});");

        new OpenedContract(true).clickByDiscussionIcon("Limitations on Damages and Indemnification");

        // Scroll discussion right panel to the bottom
        Selenide.executeJavaScript("document.querySelector('.discussion2__body__scrollable-body').scrollTo(0,document.querySelector('.discussion2__body__scrollable-body').scrollHeight)");

        logger.info("Assert that total amount of posts is 7...");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(7)); // Amount of total posts

        logger.info("Assert that changes were applied...");
        $$(".discussion2-post").last()
                                         .$$("del")
                                         .shouldHave(CollectionCondition.size(2))
                                         .shouldHave(CollectionCondition.textsInAnyOrder("under", "on damages"));

        $$(".discussion2-post").last()
                                         .$$("ins")
                                         .shouldHave(CollectionCondition.sizeGreaterThanOrEqual(6)); // for sake of stability check only amount of added tags

        Screenshoter.makeScreenshot();
    }
}
