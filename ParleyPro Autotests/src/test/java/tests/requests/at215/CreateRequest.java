package tests.requests.at215;

import constants.Const;
import forms.ContractRequest;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class CreateRequest
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(CreateRequest.class);


    @Test(priority = 1)
    public void loginAsFelix()
    {
        LoginPage loginPage = new LoginPage();

        logger.info("Login as user with full rights(Felix)...");
        loginPage.setEmail( Const.USER_FELIX.getEmail() );
        loginPage.setPassword( Const.USER_FELIX.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        ContractRequest contractRequest = sideBar.clickInProgressContracts(true).clickNewRequestButton();

    }
}
