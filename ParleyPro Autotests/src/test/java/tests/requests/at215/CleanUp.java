package tests.requests.at215;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AdministrationPage;
import pages.LoginPage;
import pages.administration.Fields;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(CleanUp.class);


    @Test(priority = 1)
    public void loginAsCN()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();
    }

    @Test(priority = 2)
    public void cleanUp()
    {
        logger.info("Remove routing workflow...");
        AdministrationPage administrationPage = sideBar.clickAdministration();
        administrationPage.clickWorkflowsTab().clickActionMenu("AT-215_Routing_Workflow").clickDelete().clickDelete();

        $$(".workflows-list__row").shouldHave(CollectionCondition.size(1));

        logger.info("Remove request field...");
        Fields fieldsTab = administrationPage.clickFieldsTab();
        fieldsTab.clickContractFields().removeField("ReqField_AT215_Trigger").clickDelete();
        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.appear, 17_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
