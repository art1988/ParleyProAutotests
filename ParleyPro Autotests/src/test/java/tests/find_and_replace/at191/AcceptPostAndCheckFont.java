package tests.find_and_replace.at191;

import com.codeborne.selenide.*;
import constants.AcceptTypes;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import pages.tooltips.ParagraphActionsPopup;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

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
        ElementsCollection spans = $(".discussion-indicator.closed").parent().parent().find("p").findAll("span");
        for(SelenideElement span: spans)
        {
            String styleAttr = span.getAttribute("style");
            if( styleAttr.contains("-aw-import:ignore") || StringUtils.isBlank(styleAttr)) continue;
            Assert.assertTrue(styleAttr.contains("Times New Roman"), "Looks like that at least one span doesn't have correct font !!!");
        }

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
        ElementsCollection spans = $$(".discussion-indicator.closed").get(1).parent().parent().find("p").findAll("span");
        for(SelenideElement span: spans)
        {
            String styleAttr = span.getAttribute("style");
            if( styleAttr.contains("-aw-import:ignore") || StringUtils.isBlank(styleAttr)) continue;
            Assert.assertTrue(styleAttr.contains("Times New Roman"), "Looks like that at least one span doesn't have correct font !!!");
        }

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void acceptAllViaManagedDiscussionAndCheckFont() throws InterruptedException
    {
        // Scroll to top
        Selenide.executeJavaScript("document.querySelector('.documents__list').scrollTo(0,0)");

        openedContract.clickManageDiscussions().acceptInternalDiscussions().confirmAccept().clickDone();

        Thread.sleep(4_000);

        logger.info("Assert that amount of discussions is empty...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "none", "Amount of discussions should be empty !!!");

        logger.info("Checking font of all remaining closed discussions...");
        for( int i = 2; i < 10; i++ )
        {
            ElementsCollection spans = $$(".discussion-indicator.closed").get(i).parent().parent().find("p").findAll("span");
            for(SelenideElement span: spans)
            {
                String styleAttr = span.getAttribute("style");
                if( styleAttr.contains("-aw-import:ignore") || StringUtils.isBlank(styleAttr)) continue;
                Assert.assertTrue(styleAttr.contains("Times New Roman"), "Looks like that at least one span doesn't have correct font !!!");
            }
        }

        logger.info("Making sure that there are no del/ins tags left...");
        $$("del").shouldHave(CollectionCondition.size(0));
        $$("ins").shouldHave(CollectionCondition.size(0));

        Screenshoter.makeScreenshot();
    }
}
