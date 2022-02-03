package tests.basics.at188;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import pages.subelements.CKEditorActive;
import utils.EmailChecker;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


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
        logger.info("Mention user Felix...");
        ckEditorInstance = openedContract.clickByParagraph("Paragraph 2");

        ckEditorInstance.getCommentInstance().sendKeys("@F");
        SelenideElement popup = $$(".atwho-view").filterBy(Condition.visible).shouldHaveSize(1).first();
        popup.findAll("ul li").filterBy(Condition.text("Felix")).first().click();
        Thread.sleep(1_000);

        ckEditorInstance.setComment(" user was mentioned...").clickPost();
        $(".notification-stack").shouldHave(Condition.text(" has been successfully created."));

        $$(".contract-header__right .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "FW")); // users were added in contract header
        $$(".header-users .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "FW")); // users were added in document header

        $$(".document__body .discussion-indicator").shouldHave(CollectionCondition.size(1)).first().shouldBe(Condition.visible); // discussion icon is visible

        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "You are mentioned in discussions"),
                "Email with subject: You are mentioned in discussions was not found !!!");

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIconSoft("Paragraph 2");
        $$(".discussion2-post").last().find(".discussion2-post__comment").click(); // click by last post to activate editor
        ckEditorInstance = new CKEditorActive();
        Assert.assertEquals(ckEditorInstance.getCommentInstance().getText(), "@arthur.khasanov+felix  user was mentioned...", "Comment field content is wrong !!!");
        Screenshoter.makeScreenshot();

        logger.info("Mention one more user (Mary)...");
        ckEditorInstance.getCommentInstance().sendKeys(" @");
        popup = $$(".atwho-view").filterBy(Condition.visible).shouldHaveSize(1).first();
        popup.findAll("ul li").filterBy(Condition.text("Mary")).first().click();
        Thread.sleep(1_000);

        ckEditorInstance.clickPost();
        $(".notification-stack").shouldHave(Condition.text("Internal post has been successfully created."));

        $$(".contract-header__right .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "FW", "MJ")); // users were added in contract header
        $$(".header-users .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "FW", "MJ")); // users were added in document header

        logger.info("Assert that post has been updated...");
        String textFromPost = $$(".discussion2-post").last().find(".discussion2-post__comment p").getText();
        Assert.assertTrue(textFromPost.contains("@arthur.khasanov+felix") && textFromPost.contains("user was mentioned...") && textFromPost.contains("@arthur.khasanov+mary"));

        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);
        logger.info("Delete those 2 emails with subject 'You are mentioned in discussions'...");
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "You are mentioned in discussions"),
                "Email with subject: You are mentioned in discussions was not found !!!"); // first email with subject 'You are mentioned in discussions'
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "You are mentioned in discussions"),
                "Email with subject: You are mentioned in discussions was not found !!!"); // second email with subject 'You are mentioned in discussions'

        openedDiscussion.close();
    }

    @Test(priority = 3)
    public void addOneMoreDiscussion() throws InterruptedException
    {
        ckEditorInstance = openedContract.clickByParagraph("Paragraph 3");

        ckEditorInstance.setText(". Some added text").clickPost();
        $(".notification-stack").shouldHave(Condition.text(" has been successfully created."));
        $$(".document__body-content .discussion-indicator").shouldHave(CollectionCondition.size(2)); // 2 discussion icons
    }
}
