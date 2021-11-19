package tests.basics.at188;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.Discussions;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class MentionOnDiscussionBoard
{
    private String host = "pop.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private static Logger logger = Logger.getLogger(MentionOnDiscussionBoard.class);


    @Test
    @Description("This test mention user (Greg) from discussion board.")
    public void mentionOnDiscussionBoard() throws InterruptedException
    {
        Discussions discussions = new OpenedContract().clickByDiscussions();

        discussions.expandDiscussion("Paragraph 3");

        // Click by 'Propose text change or a comment…' field to activate CKE
        $(".discussion2:not(.discussion2_collapsed_yes)").find(".js-discussion-continue").click();
        CKEditorActive ckEditorInstance = new CKEditorActive();

        ckEditorInstance.getCommentInstance().sendKeys("@");

        SelenideElement popup = $$(".atwho-view").filterBy(Condition.visible).shouldHaveSize(1).first();
        popup.findAll("ul li").shouldHave(CollectionCondition.sizeGreaterThan(3));
        Screenshoter.makeScreenshot();

        popup.findAll("ul li").filterBy(Condition.text("Greg")).first().click();
        Thread.sleep(1_000);

        ckEditorInstance.setComment(" Greg was mentioned too !").clickPost();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText(" post has been successfully created."));

        logger.info("Assert that post has been created...");
        String textFromPost = $(".discussion2:not(.discussion2_collapsed_yes)").findAll(".discussion2-post").last().find(".discussion2-post__comment p").getText();
        Assert.assertTrue(textFromPost.contains("@arthur.khasanov+greg") && textFromPost.contains("Greg was mentioned too !"));
        Screenshoter.makeScreenshot();

        logger.info("Waiting for 60 seconds... Going to delete email with subject 'You are mentioned in discussions'...");
        Thread.sleep(60_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "You are mentioned in discussions"),
                "Email with subject: You are mentioned in discussions was not found !!!");
    }
}
