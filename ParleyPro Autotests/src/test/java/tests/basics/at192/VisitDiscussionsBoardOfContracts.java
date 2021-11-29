package tests.basics.at192;

import com.codeborne.selenide.Condition;
import forms.StartExternalDiscussion;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.Discussions;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class VisitDiscussionsBoardOfContracts
{
    private static Logger logger = Logger.getLogger(VisitDiscussionsBoardOfContracts.class);


    @Test
    @Description("This test visits Discussions board of contracts page, checks total amount of contacts.")
    public void visitDiscussionsBoardOfContracts()
    {
        Discussions discussions = new DashboardPage().getSideBar().clickInProgressContracts(false).clickDiscussionsTab();

        logger.info("Making sure that total amount of discussions is 84...");
        $$(".contracts-tabs__right .dropdown.btn-group span").first().shouldHave(Condition.exactText("Open (84)"));

        discussions.expandDiscussion("SCHEDULE 4");

        logger.info("Making it queued...");
        $$(".discussion2-post__foot-action").filter(Condition.text("MAKE QUEUED")).first().shouldBe(Condition.visible, Condition.enabled).click();
        $(".discussion-indicator.queued").shouldBe(Condition.visible).find(".discussion-indicator__count").shouldHave(Condition.exactText("2"));

        logger.info("Making it external...");
        $$(".discussion2-post__foot-action").filter(Condition.text("MAKE EXTERNAL")).last().shouldBe(Condition.visible, Condition.enabled).click();
        new StartExternalDiscussion("SCHEDULE 4").clickNext()
                                                 .clickSend("SCHEDULE 4", "CounterpartyAT")
                                                 .clickPostExternally();

        $(".discussion-indicator.negotiating").shouldBe(Condition.visible).find(".discussion-indicator__count").shouldHave(Condition.exactText("3"));
    }
}
