package tests.ccn_tests.invite_ccn;

import constants.Const;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.InProgressContractsPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

@Listeners({ScreenShotOnFailListener.class})
public class LoginBackAsCN
{
    @Test
    public void loginBackAsCN()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        InProgressContractsPage inProgressContractsPage = loginPage.clickSignIn().getSideBar().clickInProgressContracts(false);

        Screenshoter.makeScreenshot();
    }
}
