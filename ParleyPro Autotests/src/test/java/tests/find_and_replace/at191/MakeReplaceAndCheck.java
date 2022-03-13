package tests.find_and_replace.at191;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.subelements.FindAndReplacePopup;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class MakeReplaceAndCheck
{
    private static Logger logger = Logger.getLogger(MakeReplaceAndCheck.class);

    @Test(priority = 1)
    public void makeReplace() throws InterruptedException
    {
        FindAndReplacePopup findAndReplacePopup = new OpenedContract().clickFindAndReplaceButton("AT-166_Manufacturing Agreement_1")
                                                                      .clickReplaceTab()
                                                                      .findInDocument("Exhibit")
                                                                      .replaceWith("Attachment");

        logger.info("Assert that Matches count is correct...");
        $("div[class^=\"styles__footer\"] div").shouldHave(Condition.text("Matches: 13"));

        Screenshoter.makeScreenshot();

        Thread.sleep(2_000);
        findAndReplacePopup.clickReviewAndReplaceButton()
                           .clickSave();

        $(".notification-stack").should(Condition.appear).shouldHave(Condition.exactText("10 discussions has been created"));
    }

    @Test(priority = 2)
    public void checkRedlines()
    {
        logger.info("Check redlines on the document view...");

        // 10 discussion icons on document view from the right side
        $$(".document__body-content .discussion-indicator").shouldHave(CollectionCondition.size(10));

        $$("del").shouldHave(CollectionCondition.size(13))
                .forEach(del -> Assert.assertEquals(del.getCssValue("color"), "rgba(213, 48, 87, 1)", "Looks like that word 'Exhibit' wasn't deleted !!!"));
        $$("ins").shouldHave(CollectionCondition.size(13))
                .forEach(ins -> Assert.assertEquals(ins.getCssValue("color"), "rgba(68, 120, 208, 1)", "Looks like that word 'Attachment' wasn't added !!!"));
    }
}
