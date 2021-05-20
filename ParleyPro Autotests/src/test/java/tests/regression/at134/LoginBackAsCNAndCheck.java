package tests.regression.at134;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import pages.subelements.CKEditorActive;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginBackAsCNAndCheck
{
    private static Logger logger = Logger.getLogger(LoginBackAsCNAndCheck.class);

    @Test
    @Description("This test logins back as my team CN and adds comment and checks that page not crashes.")
    public void loginBackAsMyTeamCNAndCheck() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        dashboardPage.getSideBar()
                     .clickInProgressContracts(false)
                     .selectContract("Share me");

        new OpenedContract().clickByDiscussionIcon("comment here");
        $(".documents-discussion-panel .discussion2-post").waitUntil(Condition.visible, 10_000);

        logger.info("Hover over added post and click pencil icon to activate editor...");
        $(".documents-discussion-panel .discussion2-post").hover();
        Thread.sleep(1_000);
        Selenide.executeJavaScript("$('.documents-discussion-panel .discussion2-post .material-icons:contains(\"edit\")').click()");

        new CKEditorActive().setComment("A B C").clickPost();

        // Notification check...
        $(".notification-stack").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Internal post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 20_000);

        logger.info("Assert that post has been created...");
        $$(".documents-discussion-panel .discussion2-post").shouldHave(CollectionCondition.size(2)); // 2 posts in one discussion
        Assert.assertEquals($$(".documents-discussion-panel .discussion2-post").last().find(".discussion2-post__comment p").getText(),
                "A B C", "Looks like that post wasn't created !!!"); // and the last post was added

        Screenshoter.makeScreenshot();
    }
}
