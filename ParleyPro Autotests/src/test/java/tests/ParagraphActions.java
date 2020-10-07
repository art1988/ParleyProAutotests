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

        String comment = "Delete paragraph example";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickDelete();
        ckEditorActive.setComment(comment);
        ckEditorActive.clickPost();

        String paragraphTitle = "Paragraph 1: Hello, delete me please";

        logger.info("Assert that internal discussion popup was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Internal discussion " + paragraphTitle + " has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that deleted paragraph has redline...");
        Assert.assertTrue( $("del").getAttribute("style").equals("color: rgb(181, 8, 46);") );

        logger.info("Check that deleted paragraph has discussion...");

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(paragraphTitle);

        $(".documents-discussion-panel__close").waitUntil(Condition.visible, 5_000);
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

        String comment = "Comment paragraph autotest";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddComment();
        ckEditorActive.setComment(comment);
        ckEditorActive.clickPost();

        String paragraphTitle = "Paragraph 2: Create comment here";

        logger.info("Assert that internal discussion popup was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Internal discussion " + paragraphTitle + " has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that comment is present");

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(paragraphTitle);

        Assert.assertEquals(Integer.parseInt(openedDiscussion.getCountOfPosts()), 1); // we should see only one post
        Assert.assertEquals($("div[class*='post__comment'] div").text(), comment);  // check that comment is present

        Screenshoter.makeScreenshot();

        openedDiscussion.close(); // Close right panel with opened discussions
        $(".intercom-launcher").waitUntil(Condition.appear, 6_000);
    }

    @Test(priority = 3)
    public void addParagraphAbove()
    {
        OpenedContract openedContract = new OpenedContract();

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("Insert something above");

        String addedText = "Paragraph that was added ABOVE from autotest",
               comment   = "Added above comment";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphAbove();
        ckEditorActive.setText(addedText);
        ckEditorActive.setComment(comment);
        ckEditorActive.clickPost();

        logger.info("Assert that internal discussion popup was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Internal discussion " + addedText + " has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(addedText);

        Assert.assertEquals(Integer.parseInt(openedDiscussion.getCountOfPosts()), 1); // we should see only one post
        Assert.assertEquals($("div[class*='post__comment'] div").text(), comment);  // check that comment is present

        Screenshoter.makeScreenshot();

        openedDiscussion.close(); // Close right panel with opened discussions
        $(".intercom-launcher").waitUntil(Condition.appear, 6_000);

        logger.info("Assert that added paragraph has blue color...");
        Assert.assertTrue( $("ins").getCssValue("color").equals("rgba(68, 120, 208, 1)") );

        logger.info("Assert that 'Paragraph 3' is below of just added paragraph...");
        String textFromParagraph3 = Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"" + addedText + "\")').parent().parent().parent().next().text()");
        Assert.assertEquals(textFromParagraph3, "arrow_forward Paragraph 3: Insert something above me ");
    }
}
