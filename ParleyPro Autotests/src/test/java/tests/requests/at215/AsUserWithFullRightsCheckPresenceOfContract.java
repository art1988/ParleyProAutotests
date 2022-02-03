package tests.requests.at215;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.subelements.SideBar;
import utils.Screenshoter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AsUserWithFullRightsCheckPresenceOfContract
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(AsUserWithFullRightsCheckPresenceOfContract.class);


    @Test(priority = 1)
    @Description("Test verifies that user with full rights can still see contract and role is present on user's icon.")
    public void loginAsFelixAndCheck()
    {
        LoginPage loginPage = new LoginPage();

        logger.info("Login as user with full rights(Felix)...");
        loginPage.setEmail( Const.USER_FELIX.getEmail() );
        loginPage.setPassword( Const.USER_FELIX.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();
        sideBar.clickInProgressContracts(false);

        $(byText("AT-215 Request")).shouldBe(Condition.visible);

        sideBar.clickInProgressContracts(false).selectContract("AT-215 Request");

        logger.info("Hover over user icon FW and check role...");
        $$(".contract-header-users .user").filterBy(Condition.exactText("FW")).first().hover();
        $(".rc-tooltip-content").waitUntil(Condition.appear, 30_000);
        $(".rc-tooltip-inner .spinner").waitUntil(Condition.disappear, 30_000);
        $(".rc-tooltip-inner .contract-user__status").shouldHave(Condition.exactText("Requester"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logoutAsFelix()
    {
        logger.info("Logout as user with full rights(Felix)...");
        sideBar.logout();
    }
}
