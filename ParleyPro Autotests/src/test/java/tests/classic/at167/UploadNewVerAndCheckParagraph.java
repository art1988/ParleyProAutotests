package tests.classic.at167;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import utils.Cache;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadNewVerAndCheckParagraph
{
    private String documentName = "Quotient_DCSA_QuotientEdits_14703_AT_167";
    private static Logger logger = Logger.getLogger(UploadNewVerAndCheckParagraph.class);

    @Test
    @Description("This test uploads second doc as CP and checks that only 2 opened discussions are present,  " +
            "there is no any (b) on document view for all paragraphs, checks last post.")
    public void uploadNewVerAndCheckParagraph()
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract(Cache.getInstance().getCachedContractTitle());

        OpenedContract openedContract = new OpenedContract().clickUploadNewVersionButton(documentName)
                                                            .clickUploadCounterpartyDocument(Const.CLASSIC_AT_167_V2, documentName, Cache.getInstance().getCachedContractTitle())
                                                            .clickUpload(true)
                                                            .clickDocumentsTab();

        logger.info("Assert that only 2 opened discussions are present...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "2");
        $$(".document__body .discussion-indicator.has-new").shouldHave(CollectionCondition.size(2))
                .forEach(icon -> Assert.assertEquals(icon.getCssValue("color"), "rgba(127, 111, 207, 1)"));
        $$(".document__body .discussion-indicator.closed").shouldHave(CollectionCondition.size(2))
                .forEach(icon -> Assert.assertEquals(icon.getCssValue("color"), "rgba(192, 193, 194, 1)"));

        logger.info("Assert that there is no any (b) on document view for all paragraphs...");
        $$(".document-paragraph__content-text").forEach(paragraph -> Assert.assertFalse(paragraph.has(Condition.exactText("(b)"))));

        // since there is no paragraph to match (empty paragraph) -> click by last new discussion
        $$(".document__body .discussion-indicator.has-new").last().click();
        OpenedDiscussion openedDiscussion = new OpenedDiscussion(true);

        // Scroll discussion right panel to the bottom
        Selenide.executeJavaScript("document.querySelector('.discussion2__body__scrollable-body').scrollTo(0,document.querySelector('.discussion2__body__scrollable-body').scrollHeight)");

        logger.info("Checking the latest post...");
        // get the last post of this discussion and check
        SelenideElement lastPost = $$(".discussion2-post").last().find(".discussion2-post__text");
        Assert.assertFalse(lastPost.find("ins").exists(), "The last post has some inserts !!! But shouldn't !!!"); // no added text at all
        lastPost.findAll("del").shouldHave(CollectionCondition.size(3)); // 3 del's
        Assert.assertTrue(Selenide.executeJavaScript("return $('.discussion2-post').last().find(\".discussion2-post__text:contains('(b)')\").length === 1")); // (b) is only one

        Screenshoter.makeScreenshot();
    }
}
