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

        logger.info("Checking notification. It should be only one notification without warning.");
        $$(".notification-stack .notification-stack__item").shouldHave(CollectionCondition.size(1));
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Document " + listOfDocs[index] + " has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        $(".document__body .spinner").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that image was displayed in PP...");
        Assert.assertTrue($(".document-paragraph__content-text img").isImage(), "Looks like this is not an <img> !!!");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test uploads document with text and image.")
    public void uploadDocWithTextAndImage()
    {
        // Click UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.REGRESSION_TEXT_AND_IMG_DOC );

        logger.info("Checking notification. It should be only one notification without warning.");
        $$(".notification-stack .notification-stack__item").shouldHave(CollectionCondition.size(1));
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Document " + listOfDocs[index] + " has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        $(".document__body .spinner").waitUntil(Condition.disappear, 15_000);

        $$(".document-paragraph__content-text").shouldHave(CollectionCondition.size(6));
        $$(".document-paragraph__content-text").get(0).shouldHave(Condition.exactText(" Test contract document "));
        $$(".document-paragraph__content-text").get(1).shouldHave(Condition.exactText(" Chapter 1. "));
        $$(".document-paragraph__content-text").get(2).shouldHave(Condition.text("Left Munich at 8:35 P. M., on 1st May, arriving"));
        $$(".document-paragraph__content-text").get(3).shouldHave(Condition.exactText(" Chapter 2. Image. "));
        Assert.assertTrue($$(".document-paragraph__content-text").get(4).find("img").isImage(), "Looks like this is not an <img> !!!");
        $$(".document-paragraph__content-text").get(5).shouldHave(Condition.text("We left in pretty good time, and came after nightfall to Klausenburgh."));

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
}
