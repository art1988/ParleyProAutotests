package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import pages.subelements.CKEditorActive;
import pages.subelements.ParagraphActionsPopup;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

public class ParagraphActions
{
    private static Logger logger = Logger.getLogger(ParagraphActions.class);


    @Test(priority = 1)
    public void deleteParagraph()
    {
        OpenedContract openedContract = new OpenedContract();

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("delete me");

        CKEditorActive ckEditorActive = paragraphActionsPopup.clickDelete();
        String comment = "Delete paragraph example";
        ckEditorActive.setComment(comment);
        ckEditorActive.clickPost();

        logger.info("Assert that internal discussion popup was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Internal discussion Paragraph 1: Hello, delete me please has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that deleted paragraph has redline...");
        Assert.assertTrue( $("del").getAttribute("style").equals("color: rgb(181, 8, 46);") );

        logger.info("Check that deleted paragraph has discussion...");
        String paragraphTitle = "Paragraph 1: Hello, delete me please";

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(paragraphTitle);

        Assert.assertEquals(Integer.parseInt(openedDiscussion.getCountOfPosts()), 1); // we should see only one post
        Assert.assertEquals($("div[class*='post__comment'] div").text(), comment);  // check that comment is present

        Screenshoter.makeScreenshot();

        openedDiscussion.close(); // Close right panel with opened discussions
        $(".intercom-launcher").waitUntil(Condition.appear, 6_000);
    }

    @Test(priority = 2)
    public void commentParagraph()
    {
        OpenedContract openedContract = new OpenedContract();

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("comment here");

        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddComment();
        String comment = "This is autotest comment";
        ckEditorActive.setComment(comment);
        ckEditorActive.clickPost();

        // wait until notification popup disappear
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that comment is present");

        String paragraphTitle = "Paragraph 2: Create comment here";

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(paragraphTitle);

        Assert.assertEquals(Integer.parseInt(openedDiscussion.getCountOfPosts()), 1); // we should see only one post
        Assert.assertEquals($("div[class*='post__comment'] div").text(), comment);  // check that comment is present

        Screenshoter.makeScreenshot();

        openedDiscussion.close(); // Close right panel with opened discussions
        $(".intercom-launcher").waitUntil(Condition.appear, 6_000);
    }
}
