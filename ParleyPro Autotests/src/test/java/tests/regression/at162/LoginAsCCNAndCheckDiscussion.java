package tests.regression.at162;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import constants.SideBarItems;
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
    private DashboardPage dashboardPage;
    private String contractName = "AT-162 Contract";

    private static Logger logger = Logger.getLogger(LoginAsCCNAndCheckDiscussion.class);

    @Test(priority = 1)
    public void loginAsCCNAndCheckDiscussion()
    {
        LoginPage loginPage = new LoginPage();
        loginPage.setEmail( Const.PREDEFINED_CCN.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_CCN.getPassword() );
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS});

        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract(contractName);

        OpenedContract openedContract = new OpenedContract();
        logger.info("Making sure that internal discussion is still open...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "1");
        $$(".discussion-indicator").shouldHave(CollectionCondition.size(3)); // check total amount of discussion icons

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logoutAsCCN()
    {
        logger.info("Logout as CCN...");
        dashboardPage.getSideBar().logout();
    }

    @Test(priority = 3)
    public void loginBackAsMyTeamCN()
    {
        logger.info("Login back as my team CN...");
        LoginPage loginPage = new LoginPage();
        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        loginPage.clickSignIn();
    }
}
