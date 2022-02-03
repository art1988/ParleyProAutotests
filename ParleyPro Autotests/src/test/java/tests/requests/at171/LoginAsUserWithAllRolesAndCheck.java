package tests.requests.at171;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.LoginPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;


public class LoginAsUserWithAllRolesAndCheck
{
    private DashboardPage dashboardPage;
    private static Logger logger = Logger.getLogger(LoginAsUserWithAllRolesAndCheck.class);

    @Test(priority = 1)
    @Description("This test logins as User FELIX that have all roles and checks that both request items are in the list.")
    public void loginAsUserWithAllRolesAndCheck()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.USER_FELIX.getEmail() );
        loginPage.setPassword( Const.USER_FELIX.getPassword() );
        dashboardPage = loginPage.clickSignIn();

        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(false);

        logger.info("Check that User with all roles can see both request in the list...");
        $$(".contract-item .contracts-list__contract-name").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("Contract request", "Contract request"));

        logger.info("Check purple label...");
        $$(".label_theme_purple").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REQUEST", "REQUEST"));

        logger.info("Check Stage column...");
        $$(".contract-status").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REQUEST", "REQUEST"));

        logger.info("Check Chief Negotiator column...");
        $$(".contracts-list__chief-negotiator-cell").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.exactTexts("Chief Negotiator", "autotest_cn fn ln", "autotest_cn fn ln"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logoutAsUserWithAllRoles()
    {
        logger.info("Logout as User with all roles (FELIX)...");
        dashboardPage.getSideBar().logout();
    }
}
