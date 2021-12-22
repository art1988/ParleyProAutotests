package tests.classic.at206;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Discussions;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
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
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Agreement Term\")').closest('.document-paragraph__content').find('span[list-item=\"true\"]').text() === '1.2'"));
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"eGain\")').closest('.document-paragraph__content').find('span[list-item=\"true\"]').text() === '1.3'"));
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
    public void uploadNewVerAndCheck()
    {
        String docName = "AT_206_eGain Master Agreement LM edits July_9";

        Discussions discussionsTab = new OpenedContract().clickUploadNewVersionButton(docName)
                                                         .clickUploadCounterpartyDocument(Const.TRACK_CHANGES_CLASSIC_AT206_V2, docName, Cache.getInstance().getCachedContractTitle())
                                                         .clickUpload(true);

        discussionsTab.clickDocumentsTab();
    }
}
