package tests.requests.at185;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ex.ElementNotFound;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class ApproveRequestByApprover
{
    private static Logger logger = Logger.getLogger(ApproveRequestByApprover.class);
    private DashboardPage dashboardPage;

    @Test(priority = 1)
    public void approve()
    {
        // Login as APPROVER
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_APPROVER_USER_1.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_APPROVER_USER_1.getPassword() );

        dashboardPage = loginPage.clickSignIn();

        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract("Request for at-185");

        new OpenedContract();
        try
        {
            $(".contract-create__form .spinner").waitUntil(Condition.appear, 10_000);
        } catch( ElementNotFound e ) { }
        $(".contract-create__form .spinner").waitUntil(Condition.disappear, 25_000);

        logger.info("Assert that 2 My team documents are in the list...");
        $$(".documents__list-content .document__header-rename > span").shouldHave(CollectionCondition.size(2))
                .shouldHave(CollectionCondition.exactTexts("AT185-Lists-D2", "AT185-Manufacturing Agreement-redlines-D1"));

        logger.info("Assert that there is no ‘3rd party’ label...");
        $(".label.label_theme_dgray").shouldBe(Condition.hidden);

        logger.info("Assert that 2 buttons REJECT REQUEST and APPROVE are present...");
        $$(".modal-footer button").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REJECT REQUEST", "APPROVE"));

        new ContractInfo(true).clickApproveRequest().clickApprove();
        $(".notification-stack").shouldHave(Condition.text("Contract Request was successfully approved"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logoutApprover()
    {
        dashboardPage.getSideBar().logout();
    }
}
