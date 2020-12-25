package tests.basics;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.AcceptTypes;
import forms.AcceptPost;
import forms.DiscardDiscussion;
import forms.RevertToOriginal;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import pages.subelements.CKEditorActive;
import pages.subelements.MultipleDeleteOverlay;
import pages.tooltips.ParagraphActionsPopup;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ ScreenShotOnFailListener.class})
public class ParagraphActions
{
    private static Logger logger = Logger.getLogger(ParagraphActions.class);


    @Test(priority = 1)
    @Description("This test deletes Paragraph 1 and check redlines")
    public void deleteParagraph() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("delete me");

        String comment = "Delete paragraph example";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickDelete();
        ckEditorActive.setComment(comment);
        ckEditorActive.clickPost();

        String paragraphTitle = "Paragraph 1: Hello, delete me please";

        logger.info("Assert that internal discussion notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Internal discussion " + paragraphTitle + " has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that deleted paragraph has redline...");
        Assert.assertTrue( $("del").getAttribute("style").equals("color: rgb(181, 8, 46);") );

        logger.info("Check that deleted paragraph has discussion...");

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(paragraphTitle);

        Waiter.smartWaitUntilVisible("$('.discussion2-original')");
        Assert.assertEquals(Integer.parseInt(openedDiscussion.getCountOfPosts()), 1); // we should see only one post
        $("div[class*='post__comment'] div").waitUntil(Condition.visible, 9_000).shouldHave(Condition.exactText(comment)); // check that comment is present

        Screenshoter.makeScreenshot();

        openedDiscussion.close(); // Close right panel with opened discussions
        $(".documents-pdf-discussion__container").waitUntil(Condition.disappear, 6_000);
    }

    @Test(priority = 2)
    @Description("This test leaves a comment for Paragraph 2")
    public void commentParagraph() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("comment here");

        String comment = "Comment paragraph autotest";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddComment();
        ckEditorActive.setComment(comment);
        ckEditorActive.clickPost();

        String paragraphTitle = "Paragraph 2: Create comment here";

        logger.info("Assert that internal discussion notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Internal discussion " + paragraphTitle + " has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that comment is present");

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(paragraphTitle);

        Assert.assertEquals(Integer.parseInt(openedDiscussion.getCountOfPosts()), 1); // we should see only one post
        $("div[class*='post__comment'] div").waitUntil(Condition.visible, 9_000).shouldHave(Condition.exactText(comment)); // check that comment is present

        Screenshoter.makeScreenshot();

        openedDiscussion.close(); // Close right panel with opened discussions
        $(".documents-pdf-discussion__container").waitUntil(Condition.disappear, 6_000);
    }

    @Test(priority = 3)
    @Description("This test adds paragraph ABOVE Paragraph 3")
    public void addParagraphAbove() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("Insert something above");

        String addedText = "Paragraph that was added ABOVE from autotest",
               comment   = "Added above comment";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphAbove();
        ckEditorActive.setText(addedText);
        ckEditorActive.setComment(comment);
        ckEditorActive.clickPost();

        logger.info("Assert that internal discussion notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Internal discussion " + addedText + " has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(addedText);

        Assert.assertEquals(Integer.parseInt(openedDiscussion.getCountOfPosts()), 1); // we should see only one post
        $("div[class*='post__comment'] div").waitUntil(Condition.visible, 9_000).shouldHave(Condition.exactText(comment)); // check that comment is present

        Screenshoter.makeScreenshot();

        openedDiscussion.close(); // Close right panel with opened discussions
        $(".documents-pdf-discussion__container").waitUntil(Condition.disappear, 6_000);

        logger.info("Assert that added paragraph has blue color...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph ins:contains(\"" + addedText + "\")').css(\"color\") === \"rgb(68, 120, 208)\""));

        logger.info("Assert that 'Paragraph 3' is below of just added paragraph...");
        String textFromParagraph3 = Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"" + addedText + "\")').parent().parent().parent().next().text()");
        Assert.assertEquals(textFromParagraph3, "arrow_forward Paragraph 3: Insert something above me ");
    }

    @Test(priority = 4)
    @Description("This test adds paragraph BELOW Paragraph 4")
    public void addParagraphBelow() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("below me");

        String addedText  = "Paragraph that was added BELOW from autotest",
               comment    = "Added below comment";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphBelow();
        ckEditorActive.setText(addedText);
        ckEditorActive.setComment(comment);
        ckEditorActive.clickPost();

        logger.info("Assert that internal discussion notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Internal discussion " + addedText + " has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(addedText);

        Assert.assertEquals(Integer.parseInt(openedDiscussion.getCountOfPosts()), 1); // we should see only one post
        $("div[class*='post__comment'] div").waitUntil(Condition.visible, 9_000).shouldHave(Condition.exactText(comment)); // check that comment is present

        Screenshoter.makeScreenshot();

        openedDiscussion.close(); // Close right panel with opened discussions
        $(".documents-pdf-discussion__container").waitUntil(Condition.disappear, 6_000);

        logger.info("Assert that added paragraph has blue color...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph ins:contains(\"" + addedText + "\")').css(\"color\") === \"rgb(68, 120, 208)\""));

        logger.info("Assert that 'Paragraph 4' is above of just added paragraph...");
        String textFromParagraph4 = Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"" + addedText + "\")').parent().parent().parent().prev().text()");
        Assert.assertEquals(textFromParagraph4, "arrow_forward Paragraph 4: Insert from clipboard below me ");
    }

    @Test(priority = 5)
    @Description("This test deletes multiple paragraphs: Paragraph 5 and Paragraph 6")
    public void multipleDeleteAction()
    {
        OpenedContract openedContract = new OpenedContract();

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("Multiple delete first");

        MultipleDeleteOverlay multipleDeleteOverlay = paragraphActionsPopup.clickDeleteMultipleParagraphs();

        multipleDeleteOverlay.markParagraph("Paragraph 6: Multiple delete second");

        Screenshoter.makeScreenshot();

        multipleDeleteOverlay.clickPost();

        logger.info("Assert that 'External discussion on deleted paragraph...' notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("External discussion on deleted paragraph has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that redlines were applied for both paragraphs...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Paragraph 5\")').find(\"del\").attr(\"style\") === \"color:#b5082e\""));
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Paragraph 6\")').find(\"del\").attr(\"style\") === \"color:#b5082e\""));
    }

    @Test(priority = 6)
    @Description("This test discards discussion for Paragraph 1")
    public void discardDiscussion()
    {
        OpenedContract openedContract = new OpenedContract();

        String paragraphTitle = "Paragraph 1: Hello, delete me please";

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(paragraphTitle);

        DiscardDiscussion discardDiscussionForm = openedDiscussion.clickDiscardDiscussion();

        discardDiscussionForm.clickDiscardDiscussion();

        logger.info("Assert that 'discussion closed' notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Discussion " + paragraphTitle + " has been closed."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        openedDiscussion.close();

        $(".paragraph-discussions").waitUntil(Condition.disappear, 5_000); // wait until right panel disappear

        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"" + paragraphTitle + "\")')");

        logger.info("Assert that there are no more redlines for Paragraph 1...");
        boolean isColorStillRed = Selenide.executeJavaScript("return ($('.document-paragraph__content-text:contains(\"" + paragraphTitle + "\")').find(\"del\").attr(\"style\") === \"color:#b5082e\")");
        Assert.assertFalse(isColorStillRed);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 7)
    @Description("This test accepts added above paragraph via opened discussion. As a result Paragraph ABOVE has black color")
    public void acceptAddedAbove()
    {
        OpenedContract openedContract = new OpenedContract();

        String addedText = "Paragraph that was added ABOVE from autotest";

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(addedText);

        AcceptPost acceptPostForm = openedDiscussion.clickAccept(AcceptTypes.INSERT, addedText);

        acceptPostForm.clickAcceptText();

        logger.info("Assert notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        openedDiscussion.close();

        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"" + addedText + "\")')");

        logger.info("Assert that paragraph's color become black...");
        // According to jQuery documentation if length == 0 it means that element doesn't exist
        // see: https://learn.jquery.com/using-jquery-core/faq/how-do-i-test-whether-an-element-exists/
        boolean colorTagDoestExist = Selenide.executeJavaScript("return ($('.document-paragraph__content-text:contains(\"" + addedText + "\")').find(\"ins\").length === 0)");
        Assert.assertTrue(colorTagDoestExist);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 8)
    @Description("This test accepts added below paragraph via hover tooltip. As a result Paragraph BELOW has black color")
    public void acceptAddedBelowViaTooltip()
    {
        OpenedContract openedContract = new OpenedContract();

        String addedText  = "Paragraph that was added BELOW from autotest";

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover(addedText);

        AcceptPost acceptPostForm = paragraphActionsPopup.clickAcceptChangesOnParagraph(AcceptTypes.INSERT);

        acceptPostForm.clickAcceptText();

        logger.info("Assert notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that paragraph's color become black...");
        boolean colorTagDoestExist = Selenide.executeJavaScript("return ($('.document-paragraph__content-text:contains(\"" + addedText + "\")').find(\"ins\").length === 0)");
        Assert.assertTrue(colorTagDoestExist);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 9)
    @Description("This test reverts to original the 5th paragraph")
    public void revertChanges()
    {
        OpenedContract openedContract = new OpenedContract();

        String paragraphTitle = "Paragraph 5: Multiple delete first";

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(paragraphTitle);

        RevertToOriginal revertToOriginalForm = openedDiscussion.clickRevertToOriginal();

        revertToOriginalForm.clickCloseDiscussion();

        logger.info("Assert notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that paragraph's color become black...");
        boolean colorTagDoestExist = Selenide.executeJavaScript("return ($('.document-paragraph__content-text:contains(\"" + paragraphTitle + "\")').find(\"del\").length === 0)");
        Assert.assertTrue(colorTagDoestExist);
    }

    @Test(priority = 10)
    @Description("This test adds high priority tag and non-standard term for Paragraph 2")
    public void addTagAndTerm()
    {
        OpenedContract openedContract = new OpenedContract();

        String paragraphTitle = "Paragraph 2: Create comment here";

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon(paragraphTitle);

        openedDiscussion.clickPriorityButton();

        logger.info("Assert that High priority post appeared...");
        $(".discussion2-post__priority").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("High priority"));

        logger.info("Check that paragraph has high priority mark from the left...");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"" + paragraphTitle + "\")').parent().parent().prev().find(\".label_priority\")"); // wait until icon appear
        boolean hasHighPriorityMark = Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"" + paragraphTitle + "\")').parent().parent().prev().find(\".label_priority\").length === 1");
        Assert.assertTrue(hasHighPriorityMark);

        String addedTag = "Autotest TAG";
        openedDiscussion.clickTermButton(addedTag);

        // Wait until post with Non-standard tag appear
        $(".discussion2-post__term-name").waitUntil(Condition.visible, 5_000);

        logger.info("Assert that Non-standard post appeared...");
        Waiter.smartWaitUntilVisible("$('.discussion2-post:contains(\"Non-standard: " + addedTag + "\")')");
        Long countOfNonStandardPosts = Selenide.executeJavaScript("return $('.discussion2-post:contains(\"Non-standard: " + addedTag + "\")').length");
        Assert.assertEquals(countOfNonStandardPosts.longValue(), 1);

        logger.info("Check that paragraph has tag from the left...");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"" + paragraphTitle + "\")').parent().parent().prev().find(\".label_term\")"); // wait until icon appear
        boolean hasNonStandardMark = Selenide.executeJavaScript("return ( $('.document-paragraph__content-text:contains(\"" + paragraphTitle + "\")').parent().parent().prev().find(\".label_term\").length === 1 )");
        Assert.assertTrue(hasNonStandardMark);

        Screenshoter.makeScreenshot();
    }
}
