package tests.regression.at165;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import constants.SideBarItems;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsCCNAndCheckDiscussion
{
    private static Logger logger = Logger.getLogger(LoginAsCCNAndCheckDiscussion.class);
    private DashboardPage dashboardPage;

    @Test(priority = 1)
    @Description("Test checks that internal discussion is closed under CCN user.")
    public void loginAsCCNAndCheckDiscussion()
    {
        LoginPage loginPage = new LoginPage();
        loginPage.setEmail( Const.PREDEFINED_CCN.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_CCN.getPassword() );
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS});

        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract("AT-165 SIGN CTR");

        OpenedContract openedContract = new OpenedContract();

        logger.info("Making sure that internal discussion is closed...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "none");
        $$(".discussion-indicator").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.cssClass("closed")); // check that it has closed discussion

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logoutAsCCN()
    {
        dashboardPage.getSideBar().logout();
    }
}
