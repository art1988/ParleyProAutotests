package tests.requests.at149;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.administration.Fields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class RevertStateBack
{
    private static Logger logger = Logger.getLogger(RevertStateBack.class);

    @Test
    @Description("This test logins back as my team CN and removes CN role for PREDEFINED_REQUESTER user; removes contract routing workflow and removes one request field.")
    public void revertStateBack()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        logger.info("Restoring PREDEFINED_REQUESTER user...");
        dashboardPage.getSideBar()
                     .clickAdministration()
                     .clickManageUsersTab()
                     .clickActionMenu(Const.PREDEFINED_REQUESTER.getFirstName() + " " + Const.PREDEFINED_REQUESTER.getLastName())
                     .clickEdit()
                     .deleteRole("Chief Negotiator")
                     .clickUpdateUser();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("updated successfully"));

        //////
        logger.info("Removing contract routing workflow...");
        dashboardPage.getSideBar()
                     .clickAdministration()
                     .clickWorkflowsTab()
                     .clickActionMenu("Routing workflow at-149")
                     .clickDelete()
                     .clickDelete();

        $(".workflows-list__row:not(.type_header)").shouldBe(Condition.hidden);

        //////
        logger.info("Removing request field...");
        Fields fieldsPage = dashboardPage.getSideBar().clickAdministration().clickFieldsTab();

        fieldsPage.clickContractFields().removeField("requestField").clickDelete();
        fieldsPage.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
