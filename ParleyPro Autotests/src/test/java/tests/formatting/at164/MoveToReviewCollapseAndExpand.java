package tests.formatting.at164;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class MoveToReviewCollapseAndExpand
{
    private static Logger logger = Logger.getLogger(MoveToReviewCollapseAndExpand.class);

    @Test(priority = 1)
    public void moveToReview()
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("AT-164 // CTR - Document moves from Review to Draft");

        OpenedContract openedContract = new OpenedContract();

        openedContract.switchDocumentToReview("Template_AT-164-Manufacturing_Agreement").clickStart();

        logger.info("Assert that status was changed to REVIEW...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        logger.info("Making sure that Fields panel is still hidden...");
        $(".document__header-row").find("span[class*='label_theme_lblue']").should(Condition.disappear);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void collapseAndExpand() throws InterruptedException
    {
        Thread.sleep(2_000);

        logger.info("Collapse doc...");
        $(".document__header-row .expand-collapse").click();
        Thread.sleep(1_000);
        logger.info("Making sure that no text is visible...");
        Assert.assertFalse(Selenide.executeJavaScript("return $('.document__body-content').is(\":visible\");"));
        $(".document__header-row").find("span[class*='label_theme_lblue']").should(Condition.disappear);
        Screenshoter.makeScreenshot();

        logger.info("Expand doc...");
        $(".document__header-row .expand-collapse").click();
        Thread.sleep(1_000);

        logger.info("Making sure that text is visible again...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"MANUFACTURING\")').text().trim()"), "MANUFACTURING AGREEMENT", "There is no 'MANUFACTURING AGREEMENT' text on the page !!!");
        Assert.assertEquals((long) Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"FDA\")').length"), 4l, "There is no 'FDA' abbreviation on the page !!!");
        $(".document__header-row").find("span[class*='label_theme_lblue']").should(Condition.disappear);
        Screenshoter.makeScreenshot();
    }
}
