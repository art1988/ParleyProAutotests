package tests.classic.at43;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateQueuedDiscussionsAndVerify
{
    @Test
    public void createQueuedDiscussionsAndVerify() throws InterruptedException
    {
        new OpenedContract().clickByParagraph("Hello, delete me please")
                            .setText(" Some added text for this parahraph")
                            .setComment("Some comment Q1")
                            .selectQueued()
                            .clickPost();

        System.out.println($(".notification-stack").getText());
        $(".notification-stack").waitUntil(Condition.appear, 7_000).shouldHave(Condition.text("Queued discussion Paragraph 1"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);
    }
}
