package tests.regression.at110;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class MakeExternalAndUploadDocV2
{
    private String contractName = "Track changes AT-110";

    private static Logger logger = Logger.getLogger(MakeExternalAndUploadDocV2.class);

    @Test(priority = 1)
    public void makeDiscussionExternal()
    {
        new OpenedContract().clickManageDiscussions()
                            .makeExternalAllInternalDiscussions()
                            .confirmMakeExternal(contractName)
                            .clickStart();

        $(".notification-stack").waitUntil(Condition.appear, 6_000)
                .shouldHave(Condition.exactText("Contract " + contractName + " is now in negotiation. No notification was sent to the Counterparty."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        // Scroll to 4. Limitations on Damages and Indemnification.
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"Confidentiality.\")')[0].scrollIntoView({});");

        new OpenedContract(true).clickByDiscussionIcon("Limitations on Damages and Indemnification");

        logger.info("Assert that discussion become external...");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(2));
        $$(".discussion2-post").last().find(".discussion2-label__status").shouldHave(Condition.exactText("EXTERNAL"));
    }
}
