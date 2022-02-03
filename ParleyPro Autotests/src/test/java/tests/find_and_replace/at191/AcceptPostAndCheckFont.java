package tests.find_and_replace.at191;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.AcceptTypes;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import pages.tooltips.ParagraphActionsPopup;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AcceptPostAndCheckFont
{
    private OpenedContract openedContract;
    private static Logger logger = Logger.getLogger(AcceptPostAndCheckFont.class);


    @Test(priority = 1)
    public void acceptFirstParagraphAndCheckFont()
    {
        openedContract = new OpenedContract();

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("Production of Products");

        paragraphActionsPopup.clickAcceptChangesOnParagraph(AcceptTypes.ACCEPT).clickAcceptText();

        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text(" post has been successfully created."));
        $(".notification-stack").should(Condition.disappear);

        logger.info("After accepting, amount of discussions should be 9...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "9", "Amount of discussions should be 9 !!!");

        logger.info("Checking font of accepted discussions of first paragraph...");
        ArrayList<WebElement> spans = Selenide.executeJavaScript("return $('.discussion-indicator.closed').closest('div[class*=\"document-paragraph__content\"]').find(\"p\").find(\"span:not([style='-aw-import:ignore']\")");
        spans.forEach(span -> Assert.assertTrue(span.getAttribute("style").contains("\"Times New Roman\""), "Looks like that at least one span doesn't have correct font !!!"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void acceptSecondParagraphAndCheckFont()
    {
        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIconSoft("PRICE");

        openedDiscussion.clickAccept(AcceptTypes.ACCEPT, "PRICE").clickAcceptText();

        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text(" post has been successfully created."));
        $(".notification-stack").should(Condition.disappear);

        logger.info("After accepting, amount of discussions should be 8...");
        Assert.assertEquals($(".document__header-info .discussion-indicator__count").getText(), "8", "Amount of discussions should be 8 !!!");

        openedDiscussion.close();

        logger.info("Checking font of accepted discussions of second paragraph...");
        ArrayList<WebElement> spans = Selenide.executeJavaScript("return $('.discussion-indicator.closed').eq(1).closest('div[class*=\"document-paragraph__content\"]').find(\"p\").find(\"span:not([style='-aw-import:ignore']\")");
        spans.forEach(span -> Assert.assertTrue(span.getAttribute("style").contains("\"Times New Roman\""), "Looks like that at least one span doesn't have correct font !!!"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void acceptAllViaManagedDiscussionAndCheckFont() throws InterruptedException
    {
        // Scroll to top
        Selenide.executeJavaScript("document.querySelector('.documents__list').scrollTo(0,0)");

        openedContract.clickManageDiscussions().acceptInternalDiscussions().confirmAccept().clickDone();

        Thread.sleep(1_000);

        logger.info("Assert that amount of discussions is empty...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "none", "Amount of discussions should be empty !!!");

        logger.info("Checking font of all remaining closed discussions...");
        for( int i = 2; i < 10; i++ )
        {
            ArrayList<WebElement> spans = Selenide.executeJavaScript("return $('.discussion-indicator.closed').eq(" + i + ").closest('div[class*=\"document-paragraph__content\"]').find(\"p\").find(\"span:not([style='-aw-import:ignore']\")");
            spans.forEach(span -> Assert.assertTrue(span.getAttribute("style").contains("\"Times New Roman\""), "Looks like that at least one span doesn't have correct font !!!"));
        }

        logger.info("Making sure that there are no del/ins tags left...");
        $$("del").shouldHave(CollectionCondition.size(0));
        $$("ins").shouldHave(CollectionCondition.size(0));

        Screenshoter.makeScreenshot();
    }
}
