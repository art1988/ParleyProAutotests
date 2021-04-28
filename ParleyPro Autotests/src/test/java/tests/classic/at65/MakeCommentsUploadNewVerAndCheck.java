package tests.classic.at65;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class MakeCommentsUploadNewVerAndCheck
{
    private static Logger logger = Logger.getLogger(MakeCommentsUploadNewVerAndCheck.class);

    @Test(priority = 1)
    public void makeComments() throws InterruptedException
    {
        new OpenedContract().clickByParagraph("allowing Provider")
                            .selectInternal()
                            .setComment("simple comment")
                            .clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        new OpenedContract().clickByParagraph("allowing Provider")
                            .deleteAllText()
                            .setComment("del comment")
                            .selectQueued()
                            .clickPost();

        $(".notification-stack").waitUntil(Condition.appear, 7_000).shouldHave(Condition.text("Queued post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test uploads doc v2 and checks that reply from MS Word post appears.")
    public void uploadNewVersionAndCheck()
    {
        new OpenedContract().clickUploadNewVersionButton("Dynatrace_Vendor_Addendum")
                            .clickUploadCounterpartyDocument(Const.DOCUMENT_AT65_GLUE_V2, "Dynatrace_Vendor_Addendum", "at-65: comment glues")
                            .clickUpload(true)
                            .clickDocumentsTab();

        new OpenedContract().clickByDiscussionIcon("allowing Provider");

        logger.info("Assert that posts were added...");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(2))
                                         .shouldHave(CollectionCondition.textsInAnyOrder("del comment", "reply from MS Word"));

        Screenshoter.makeScreenshot();
    }
}
