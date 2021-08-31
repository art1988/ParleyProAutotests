package tests.regression.at93;

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

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsCCN
{
    private Logger logger = Logger.getLogger(LoginAsCCN.class);

    @Test(priority = 1)
    @Description("This test logins as CCN, clicks Ready For Signature button and making sure that no grey screen happens.")
    public void loginAsCCNAndClickReadyForSignature()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_CCN.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_CCN.getPassword() );
        DashboardPage dashboardPage = loginPage.clickSignIn(new SideBarItems[]{SideBarItems.IN_PROGRESS_CONTRACTS, SideBarItems.EXECUTED_CONTRACTS});

        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("Grey screen - CCN");

        new OpenedContract().clickReadyForSignature().clickOk();

        logger.info("Making sure that button Ready for signature disappeared and there is no grey screen...");
        $(".ready_to_sign").should(Condition.disappear);
        Assert.assertFalse($(".ready_to_sign").isDisplayed(), "Button READY FOR SIGNATURE is still on page, but shouldn't !!!");

        new OpenedContract(); // init new page to make sure that contractName is displayed

        Screenshoter.makeScreenshot();

        dashboardPage.getSideBar().logout();
    }

    @Test(priority = 2)
    public void loginBackAsMyTeamCN()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        loginPage.clickSignIn();
    }
}
