package tests.regression.at182;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import pages.subelements.CKEditorActive;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddPostsAndCheck
{
    private OpenedContract openedContract;
    private static Logger logger = Logger.getLogger(AddPostsAndCheck.class);

    @Test
    @Description("This test creates new paragraph with text change, clicks by the last post, adds comment to the same paragraph and checks" +
            " that comment was added and no grey screen happened. ")
    public void addPostsAndCheck() throws InterruptedException
    {
        openedContract = new OpenedContract();

        logger.info("Adding ' - test' at the end of paragraph...");
        openedContract.clickByParagraph("June 23")
                      .sendSpecificKeys(new CharSequence[]{Keys.END, " - test"})
                      .clickPost();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" has been successfully created."));

        logger.info("Clicking by just created discussion icon...");
        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIconSoft("June 23");
        Assert.assertEquals(openedDiscussion.getCountOfPosts(), "1", "Count of posts is wrong !!!");

        logger.info("Clicking by last post...");
        $(".discussion2__body .discussion2-post .text-reset").click();

        logger.info("Adding comment...");
        String commentToAdd = "COMMENT_ABC";
        new CKEditorActive().setComment(commentToAdd).clickPost();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" has been successfully created."));

        logger.info("Checking that comment was added to post...");
        $(".discussion2__body .discussion2-post .discussion2-post__comment div").shouldHave(Condition.exactText(commentToAdd));

        Screenshoter.makeScreenshot();
    }
}
