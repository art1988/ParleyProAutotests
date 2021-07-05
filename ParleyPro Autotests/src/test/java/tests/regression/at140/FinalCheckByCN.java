package tests.regression.at140;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class FinalCheckByCN
{
    private static Logger logger = Logger.getLogger(FinalCheckByCN.class);

    @Test
    public void loginAsCNAndCheck() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        loginPage.clickSignIn();

        new DashboardPage().getSideBar()
                           .clickInProgressContracts(false)
                           .selectContract("CTR AT-140");

        OpenedContract openedContract = new OpenedContract();

        logger.info("Checking all added changes by CN and CCN...");
        $$("ins").shouldHave(CollectionCondition.size(4))
                           .shouldHave(CollectionCondition.exactTexts("CN was here №1", "CN was here №2", "", "Added by CCN #3"));

        logger.info("Checking that there are no duplicate paragraphs were added on the document board...");
        for( int pNum = 1; pNum <= 7; pNum++ )
        {
            Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Paragraph " + pNum + "\")').length === 1"),
                    "Looks like that some of the paragraphs have duplicates !!!");
        }

        Screenshoter.makeScreenshot();

        logger.info("Checking that QUEUED label has only 1 item...");
        $(".label.label_theme_pink").waitUntil(Condition.visible, 10_000).shouldHave(Condition.text("1 QUEUED"));

        logger.info("Checking that queued discussion is closed, external discussion is open...");
        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon("Paragraph 3");

        $$(".discussion2-post .discussion2-label__status").shouldHave(CollectionCondition.size(1))
                                                                    .shouldHave(CollectionCondition.exactTexts("EXTERNAL"));
        // Collapse EXTERNAL...
        Selenide.executeJavaScript("$('.discussion-header__info_priority_normal .expand-collapse i').click()");
        Thread.sleep(1_000);
        // Expand INTERNAL, because closed post is closed by default...
        Selenide.executeJavaScript("$('.discussion-header__title-name span[title*=\"CN was here\"]').parent().parent().prev().click()");
        Thread.sleep(1_000);

        Assert.assertEquals(Selenide.executeJavaScript("return $('.discussion2-foot:last span').text()"), "This discussion is closed, no changes to the text",
                "Looks like that QUEUED is not closed !!!");
        $$(".discussion2-post .queued .discussion2-label__status").shouldHave(CollectionCondition.size(1))
                                                                            .shouldHave(CollectionCondition.exactTexts("QUEUED"));
        $$(".internal_closed span").shouldHave(CollectionCondition.size(1))
                                             .shouldHave(CollectionCondition.exactTexts("INTERNAL CLOSED"));

        Screenshoter.makeScreenshot();
    }
}
