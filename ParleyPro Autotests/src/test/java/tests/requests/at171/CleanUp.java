package tests.requests.at171;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import pages.administration.Fields;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    private static Logger logger = Logger.getLogger(CleanUp.class);
    private DashboardPage dashboardPage;

    @Test(priority = 1)
    public void loginAsMyTeamCN()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );
        dashboardPage = loginPage.clickSignIn();
    }

    @Test(priority = 2)
    public void cleanUp()
    {
        logger.info("Remove routing workflow...");
        AdministrationPage administrationPage = dashboardPage.getSideBar().clickAdministration();
        administrationPage.clickWorkflowsTab().clickActionMenu("Routing workflow for AT-171").clickDelete().clickDelete();

        $$(".workflows-list__row").shouldHave(CollectionCondition.size(1));

        logger.info("Remove request field...");
        Fields fieldsTab = administrationPage.clickFieldsTab();
        fieldsTab.clickContractFields().removeField("reqField").clickDelete();
        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.appear, 17_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        logger.info("Remove 2 Contract request's...");
        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(false);

        for(int i = 0; i < 2; i++)
        {
            inProgressContractsPage.selectContract("Contract request");
            new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();
            $(".notification-stack").waitUntil(Condition.appear, 17_000).shouldHave(Condition.text("Contract Contract request has been deleted."));
        }

        Screenshoter.makeScreenshot();
    }
}
