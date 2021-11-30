package tests.basics.at192;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import forms.StartExternalDiscussion;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.Discussions;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class VisitDiscussionsBoardOfContracts
{
    private static Logger logger = Logger.getLogger(VisitDiscussionsBoardOfContracts.class);


    @Test
    @Description("This test visits Discussions board of contracts page, checks total amount of contacts and performs other checks.")
    public void visitDiscussionsBoardOfContractsAndCheck() throws InterruptedException
    {
        Discussions discussions = new DashboardPage().getSideBar().clickInProgressContracts(false).clickDiscussionsTab();

        logger.info("Making sure that total amount of discussions is 84...");
        $$(".contracts-tabs__right .dropdown.btn-group span").first().shouldHave(Condition.exactText("Open (84)"));

        discussions.expandDiscussion("SCHEDULE 4");

        logger.info("Making it queued...");
        SelenideElement makeQueuedButton = $$(".discussion2-post__foot-action").filter(Condition.text("MAKE QUEUED")).first();
        makeQueuedButton.shouldBe(Condition.visible, Condition.enabled);
        Thread.sleep(1_000);
        makeQueuedButton.click();
        $(".discussion-indicator.queued").shouldBe(Condition.visible).find(".discussion-indicator__count").shouldHave(Condition.exactText("2"));

        logger.info("Making it external...");
        $$(".discussion2-post__foot-action").filter(Condition.text("MAKE EXTERNAL")).last().shouldBe(Condition.visible, Condition.enabled).click();
        new StartExternalDiscussion("SCHEDULE 4").clickNext()
                                                 .clickSend("SCHEDULE 4", "CounterpartyAT")
                                                 .clickPostExternally();

        $(".discussion-indicator.negotiating").shouldBe(Condition.visible).find(".discussion-indicator__count").shouldHave(Condition.exactText("3"));

        discussions.filter("With Counterparty");

        logger.info("Check that filter accepted and only one discussion was shown...");
        $$(".discussion2-header__row").shouldHave(CollectionCondition.size(1));
        $$(".rename > span").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("SCHEDULE 4"));

        Screenshoter.makeScreenshot();

        discussions.collapseDiscussion("SCHEDULE 4");

        discussions.filter("All");

        discussions.sortColumn("Posts");

        logger.info("Checking that sorting of posts works...");
        $$(".discussion2-header__row .discussion-indicator__count").shouldHave(CollectionCondition.size(20))
                .shouldHave(CollectionCondition.exactTexts("4", "4", "3", "3", "3", "3", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2", "2"));

        logger.info("Click by page #5...");
        $$(".pagination .pagination__item").filterBy(Condition.text("5")).first().click();

        $$(".discussion2-header__row").shouldHave(CollectionCondition.size(4));
        $$(".discussion2-header__row .discussion-indicator__count").shouldHave(CollectionCondition.exactTexts("1", "1", "1", "1"));

        Screenshoter.makeScreenshot();
    }
}
