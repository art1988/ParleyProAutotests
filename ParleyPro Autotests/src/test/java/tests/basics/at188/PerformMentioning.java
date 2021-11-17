package tests.basics.at188;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class PerformMentioning
{
    private String host = "pop.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private CKEditorActive ckEditorInstance;
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(PerformMentioning.class);


    @Test(priority = 1)
    @Description("This test verifies that after typing @ popup with users is visible.")
    public void mayInvokePopupByTypingAt()
    {
        openedContract = new OpenedContract();

        ckEditorInstance = openedContract.clickByParagraph("Paragraph 1");

        ckEditorInstance.getCommentInstance().sendKeys("@");

        SelenideElement popup = $$(".atwho-view").filterBy(Condition.visible).shouldHaveSize(1).first();
        popup.findAll("ul li").shouldHave(CollectionCondition.sizeGreaterThan(3));
        Screenshoter.makeScreenshot();

        ckEditorInstance.clickCancel();
    }

    @Test(priority = 2)
    @Description("This test mentions user Felix, checks email and creates discussion.")
    public void mentionUser() throws InterruptedException
    {
        ckEditorInstance = openedContract.clickByParagraph("Paragraph 2");

        ckEditorInstance.getCommentInstance().sendKeys("@");
        SelenideElement popup = $$(".atwho-view").filterBy(Condition.visible).shouldHaveSize(1).first();
        popup.findAll("ul li").filterBy(Condition.text("Felix")).first().click();
        Thread.sleep(1_000);

        ckEditorInstance.setComment(" user were mentioned...").clickPost();
        $(".notification-stack").shouldHave(Condition.text(" has been successfully created."));

        $$(".contract-header__right .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "FW")); // users were added in contract header
        $$(".header-users .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "FW")); // users were added in document header

        $$(".document__body .discussion-indicator").shouldHave(CollectionCondition.size(1)).first().shouldBe(Condition.visible); // discussion icon is visible

        logger.info("Waiting for 30 seconds to make sure that email has been delivered...");
        Thread.sleep(30_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "You are mentioned in discussions"),
                "Email with subject: You are mentioned in discussions was not found !!!");

        openedContract.clickByDiscussionIconSoft("Paragraph 2");
        $$(".discussion2-post").last().find(".discussion2-post__comment").click(); // click by last post
        ckEditorInstance = new CKEditorActive();
        Assert.assertEquals(ckEditorInstance.getCommentInstance().getText(), "@arthur.khasanov+felix  user were mentioned...", "Comment field content is wrong !!!");
        Screenshoter.makeScreenshot();

        logger.info("Mention one more user...");
        ckEditorInstance.getCommentInstance().sendKeys(" @");
        popup = $$(".atwho-view").filterBy(Condition.visible).shouldHaveSize(1).first();
        popup.findAll("ul li").filterBy(Condition.text("Mary")).first().click();
        Thread.sleep(1_000);

        ckEditorInstance.clickPost();
        $(".notification-stack").shouldHave(Condition.text("Internal post has been successfully created."));

        $$(".contract-header__right .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "FW", "MJ")); // users were added in contract header
        $$(".header-users .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "FW", "MJ")); // users were added in document header

        logger.info("Waiting for 30 seconds to make sure that email has been delivered...");
        Thread.sleep(30_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "You are mentioned in discussions"),
                "Email with subject: You are mentioned in discussions was not found !!!");
    }
}
