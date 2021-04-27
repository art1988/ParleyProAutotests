package tests.classic.at65;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class MakeComments
{
    private static Logger logger = Logger.getLogger(MakeComments.class);

    @Test
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
    }
}
