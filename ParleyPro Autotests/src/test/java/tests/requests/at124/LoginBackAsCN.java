package tests.requests.at124;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginBackAsCN
{
    private static Logger logger = Logger.getLogger(LoginBackAsCN.class);

    @Test(priority = 1)
    public void loginBackAsCN()
    {
        LoginPage loginPage = new LoginPage();

        // Login as my team CN
        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        logger.info("Making sure that contract with name 'Contract request' was added in In-progress contracts list...");
        $$(".contracts-list__contract-name").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("Contract request"));


    }
}
