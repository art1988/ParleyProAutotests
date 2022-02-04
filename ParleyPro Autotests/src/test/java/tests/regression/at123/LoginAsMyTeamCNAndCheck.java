package tests.regression.at123;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsMyTeamCNAndCheck
{
    private static Logger logger = Logger.getLogger(LoginAsMyTeamCNAndCheck.class);

    @Test
    public void loginAsMyTeamCNAndCheck()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();


        InProgressContractsPage inProgressContractsPage = new DashboardPage().getSideBar().clickInProgressContracts(false);

        // total number of rows should be 3...
        $$(".contracts-list__table a").shouldHave(CollectionCondition.size(3));

        logger.info("#1 Click by Chief Negotiator column...");
        inProgressContractsPage.clickByChiefNegotiatorColumn();
        logger.info("Assert that order of rows is: [ Bartholomew Bronson, Bartholomew Aaronson, Aaron Aaronson ]...");
        $$(".ui-tr.contract-item .contracts-list__chief-negotiator-cell").shouldHave(CollectionCondition.exactTexts("Aaron Aaronson", "Bartholomew Aaronson", "Bartholomew Bronson"));
        Screenshoter.makeScreenshot();

        logger.info("#2 Click by Chief Negotiator column...");
        inProgressContractsPage.clickByChiefNegotiatorColumn();
        logger.info("Assert that order of rows is: [ Aaron Aaronson, Bartholomew Aaronson, Bartholomew Bronson ]...");
        $$(".ui-tr.contract-item .contracts-list__chief-negotiator-cell").shouldHave(CollectionCondition.exactTexts("Bartholomew Bronson", "Bartholomew Aaronson", "Aaron Aaronson"));
        Screenshoter.makeScreenshot();

        logger.info("#3 Click by Chief Negotiator column to reset sorting...");
        inProgressContractsPage.clickByChiefNegotiatorColumn();
        logger.info("Assert that order was reset...");
        $$(".ui-tr.contract-item .contracts-list__chief-negotiator-cell").shouldHave(CollectionCondition.exactTexts("Bartholomew Bronson", "Bartholomew Aaronson", "Aaron Aaronson"));
        Screenshoter.makeScreenshot();
    }
}
