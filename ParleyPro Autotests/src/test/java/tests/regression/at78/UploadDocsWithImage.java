package tests.regression.at78;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadDocsWithImage
{
    // List of doc names in order of uploading
    private String[] listOfDocs = {"image", "text_and_image", "Floating_image", "Floating_text_box"};
    private int index = 0;

    private static Logger logger = Logger.getLogger(UploadDocsWithImage.class);

    @Test(priority = 1)
    @Description("This test uploads document with only one image in it.")
    public void uploadDocWithOnlyImage()
    {
        // Click UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.REGRESSION_IMG_DOC );

        assertNotificationAfterUploading();

        logger.info("Assert that image was displayed in PP...");
        $$(".document-paragraph__content-text img").shouldHave(CollectionCondition.size(1)); // Assert only one <img> tag on page
        Assert.assertTrue($(".document-paragraph__content-text img").isImage(), "Looks like this is not an <img> !!!");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test uploads document with text and image.")
    public void uploadDocWithTextAndImage()
    {
        // Click UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.REGRESSION_TEXT_AND_IMG_DOC );

        assertNotificationAfterUploading();

        logger.info("Assert that document was loaded correctly...");
        $$(".document-paragraph__content-text").shouldHave(CollectionCondition.size(6));
        $$(".document-paragraph__content-text").get(0).shouldHave(Condition.exactText(" Test contract document "));
        $$(".document-paragraph__content-text").get(1).shouldHave(Condition.exactText(" Chapter 1. "));
        $$(".document-paragraph__content-text").get(2).shouldHave(Condition.text("Left Munich at 8:35 P. M., on 1st May, arriving"));
        $$(".document-paragraph__content-text").get(3).shouldHave(Condition.exactText(" Chapter 2. Image. "));
        Assert.assertTrue($$(".document-paragraph__content-text").get(4).find("img").isImage(), "Looks like this is not an <img> !!!");
        $$(".document-paragraph__content-text img").shouldHave(CollectionCondition.size(1)); // Assert only one <img> tag on page
        $$(".document-paragraph__content-text").get(5).shouldHave(Condition.text("We left in pretty good time, and came after nightfall to Klausenburgh."));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test uploads document with floating image.")
    public void uploadDocWithFloatingImage()
    {
        // Click UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.REGRESSION_FLOATING_IMG_DOC );

        assertNotificationAfterUploading();

        logger.info("Assert that document was loaded correctly...");
        $$(".document-paragraph__content-text").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts(" Floading image "));
        $$(".document-paragraph__content-text img").shouldHave(CollectionCondition.size(1)); // Assert only one <img> tag on page
        Assert.assertTrue($(".document-paragraph__content-text img").isImage(), "Looks like this is not an <img> !!!");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    @Description("This test uploads document with floating text box.")
    public void uploadDocWithFloatingTextBox()
    {
        // Click UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.REGRESSION_FLOATING_TEXT_DOC );

        logger.info("Assert that document wasn't loaded correctly and we see one warning...");
        $(".notification-stack .notification-stack__item").waitUntil(Condition.visible, 7_000);
        $$(".notification-stack .notification-stack__item").shouldHave(CollectionCondition.size(2)); // two notification popups
        $$(".notification-stack .notification-stack__item").shouldHave(CollectionCondition.textsInAnyOrder("1 unsupported formatting attributes were found. Navigate to see all replacements\nkeyboard_arrow_left\n1/1\nkeyboard_arrow_right",
                                                                                                                     "Document Floating_text_box has been successfully uploaded."));

        $$(".document-paragraph__content-text img").shouldHave(CollectionCondition.size(0)); // There is no <img> tag on page at all

        Assert.assertEquals($(".document-paragraph__content-text").getText().trim(), "Floating text box");

        logger.info("Assert that only one warning was shown on page of PP...");
        $$(".document-paragraph-warning").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("1\nText boxes are not supported"));

        // Close warning notification popup
        $(".notification-stack .notification-stack__item").find(".notification__close").click();

        Screenshoter.makeScreenshot();
    }

    // Helper test that deletes document after each Test
    @AfterMethod
    public void deleteDoc()
    {
        new OpenedContract().clickDocumentActionsMenu(listOfDocs[index]).clickDelete().clickDelete();

        logger.info("Check document deletion notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Document " + listOfDocs[index++] + " has been deleted."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);
    }

    private void assertNotificationAfterUploading()
    {
        $(".spinner").waitUntil(Condition.disappear, 20_000);

        logger.info("Checking notification. It should be only one notification without warning.");
        $$(".notification-stack .notification-stack__item").shouldHave(CollectionCondition.size(1)); // only one notification popup
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Document " + listOfDocs[index] + " has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        $(".document__body .spinner").waitUntil(Condition.disappear, 15_000);
    }
}
