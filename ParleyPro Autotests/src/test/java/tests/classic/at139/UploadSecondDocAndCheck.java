package tests.classic.at139;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadSecondDocAndCheck
{
    private static Logger logger = Logger.getLogger(UploadSecondDocAndCheck.class);

    @Test
    public void uploadSecondDocAndCheck()
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("AT-139 Microvention doc");

        new OpenedContract().clickNewDocument().clickUploadMyTeamDocumentsWithDetectedChanges( Const.DOC_MICROVENTION_AT139_TWO ).clickOk();

        $(".notification-stack").waitUntil(Condition.appear, 25_000).shouldHave(Condition.text(" has been successfully uploaded."));

        logger.info("Checking Negotiate state for second doc...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(2)", "NEGOTIATE", "NEGOTIATE"));

        // Click by empty paragraph that has discussion icon
        $$(".documents__list-content .document__body .discussion-indicator.negotiating").shouldHave(CollectionCondition.size(2)).first().click();
        OpenedDiscussion openedDiscussion = new OpenedDiscussion("added and deleted");

        logger.info("Checking that discussion has 2 posts...");
        Assert.assertEquals(openedDiscussion.getCountOfPosts(), "2", "Amount of posts is wrong !!! Should be 2.");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("added and deleted", "need response"));

        Screenshoter.makeScreenshot();
    }
}
