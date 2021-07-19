package tests.regression.at142;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import constants.SideBarItems;
import io.qameta.allure.Description;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;
import static constants.SideBarItems.*;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsUser2AndCheck
{
    private static Logger logger = Logger.getLogger(LoginAsUser2AndCheck.class);

    @Test
    @Description("This test logins as User2 (USER_GREG) and checks that only Contract1 is in the list.")
    public void loginAsGregAndCheck()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.USER_GREG.getEmail() );
        loginPage.setPassword( Const.USER_GREG.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn(new SideBarItems[]{ PRIORITY_DASHBOARD,
                                                                                IN_PROGRESS_CONTRACTS,
                                                                                EXECUTED_CONTRACTS,
                                                                                DASHBOARD });

        dashboardPage.getSideBar().clickExecutedContracts(false);

        logger.info("Assert that only Contract 1 is in the list...");
        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(1))
                                                      .get(0)
                                                      .shouldHave(Condition.exactText("Contract 1"));

        Screenshoter.makeScreenshot();

        // Logout
        dashboardPage.getSideBar().logout();
    }
}
