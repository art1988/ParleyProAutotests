package tests.regression.at140;

import com.codeborne.selenide.Condition;
import constants.Const;
import constants.SideBarItems;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsCCNAndMakeInternalDiscussions
{
    private Logger logger = Logger.getLogger( LoginAsCCNAndMakeInternalDiscussions.class );
    private DashboardPage dashboardPage;

    @Test
    public void loginAsCCNAndMakeInternalDiscussions() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_CCN.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_CCN.getPassword() );
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS});

        logger.info("Creating internal discussion for the first paragraph...");
        new OpenedContract().clickByParagraph("Paragraph 1")
                            .sendSpecificKeys(new CharSequence[]{Keys.END, " Added by CCN #1"})
                            .selectInternal()
                            .clickPost();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.text("Internal discussion"));


        logger.info("Creating internal discussion for the second paragraph...");
        new OpenedContract().clickByParagraph("Paragraph 2")
                            .sendSpecificKeys(new CharSequence[]{Keys.END, " Added by CCN #2"})
                            .selectInternal()
                            .clickPost();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.text("Internal discussion"));

        Screenshoter.makeScreenshot();

        // Logout
        dashboardPage.getSideBar().logout();
    }
}
