package tests.regression.at140;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
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
public class LoginAsCNAndMakeInternalDiscussions
{
    private static Logger logger = Logger.getLogger( LoginAsCNAndMakeInternalDiscussions.class );

    @Test(priority = 1)
    public void loginAsCNAndMakeInternalDiscussions() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        loginPage.clickSignIn();

        new DashboardPage().getSideBar()
                           .clickInProgressContracts(false)
                           .selectContract("CTR AT-140");

        logger.info("Creating internal discussion for the first paragraph...");
        new OpenedContract().clickByParagraph("Paragraph 1")
                            .sendSpecificKeys(new CharSequence[]{Keys.END, " CN was here №1"})
                            .selectInternal()
                            .clickPost();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.text("Internal discussion"));

        logger.info("Creating internal discussion for the second paragraph...");
        new OpenedContract().clickByParagraph("Paragraph 2")
                            .sendSpecificKeys(new CharSequence[]{Keys.END, " CN was here №2"})
                            .selectInternal()
                            .clickPost();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.text("Internal discussion"));

        logger.info("Making sure that discussions were created...");
        $$(".document__body-content .discussion-indicator").shouldHave(CollectionCondition.size(2));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void makeQueuedSecondParagraph()
    {
        OpenedDiscussion openedDiscussion = new OpenedContract().clickByDiscussionIcon("Paragraph 2");
        openedDiscussion.clickMakeQueued("CN was here №2");

        logger.info("Assert that Queued post was created...");
        $(".active_queued").shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();

        openedDiscussion.close();
    }

    @Test(priority = 3)
    public void createQueuedDiscussionForThirdParagraph() throws InterruptedException
    {
        new OpenedContract().clickByParagraph("Paragraph 3")
                            .sendSpecificKeys(new CharSequence[]{Keys.END, " CN was here №3"})
                            .selectQueued()
                            .clickPost();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.text("Queued discussion"));

        Screenshoter.makeScreenshot();

        logger.info("Check that QUEUED label has 2 items...");
        $(".label.label_theme_pink").waitUntil(Condition.visible, 10_000).shouldHave(Condition.text("2 QUEUED"));

        // Logout
        new DashboardPage().getSideBar().logout();
    }
}
