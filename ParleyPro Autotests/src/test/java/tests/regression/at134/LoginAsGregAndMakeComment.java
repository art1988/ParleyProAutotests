package tests.regression.at134;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsGregAndMakeComment
{
    @Test
    @Description("This test logins as User Greg, adds '1' to paragraph and leaves a comment.")
    public void loginAsUserGregAndMakeComment() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.USER_GREG.getEmail() );
        loginPage.setPassword( Const.USER_GREG.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        dashboardPage.getSideBar()
                     .clickInProgressContracts(false)
                     .selectContract("Share me");

        new OpenedContract().clickByParagraph("comment here")
                            .setText(" 1")
                            .setComment("just added 1 at the end of the paragraph")
                            .clickPost();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.text("has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        Screenshoter.makeScreenshot();

        // Logout
        dashboardPage.getSideBar().logout();
    }
}
