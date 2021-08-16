package tests.regression.at162;

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

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsCCNAndCheckDiscussion
{
    private DashboardPage dashboardPage;

    private static Logger logger = Logger.getLogger(LoginAsCCNAndCheckDiscussion.class);

    @Test
    public void loginAsCCNAndCheckDiscussion()
    {
        LoginPage loginPage = new LoginPage();
        loginPage.setEmail( Const.PREDEFINED_CCN.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_CCN.getPassword() );
        dashboardPage = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS});

        OpenedContract openedContract = new OpenedContract();
        logger.info("Making sure that internal discussion is still open...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "1");

    }
}
