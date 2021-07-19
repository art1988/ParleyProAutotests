package tests.regression.at142;

import com.codeborne.selenide.Condition;
import constants.Const;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import pages.*;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    private static Logger logger = Logger.getLogger(CleanUp.class);

    @Test
    public void cleanUp()
    {
        // Login under my team CN
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        ///
        logger.info("Removing contracts...");
        ExecutedContractsPage executedContractsPage = dashboardPage.getSideBar().clickExecutedContracts(false);

        for( int i = 0; i <= 2; i++ )
        {
            String contractName = "Contract " + i;

            AddDocuments addDocuments = executedContractsPage.selectContractWithoutUploadedDoc(contractName);
            new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();

            $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.text(" has been deleted."));
            $(".notification-stack").waitUntil(Condition.disappear, 25_000);
        }

        ///
        logger.info("Restoring user Greg roles...");
        dashboardPage.getSideBar()
                     .clickAdministration()
                     .clickManageUsersTab()
                     .clickActionMenu( Const.USER_GREG.getFirstName() )
                     .clickEdit()
                     .deleteRole("Viewer Plus")
                     .clickUpdateUser();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("updated successfully"));

        ///
        logger.info("Restoring department values...");
        Fields fieldsPage = dashboardPage.getSideBar().clickAdministration().clickFieldsTab();
        ContractFields contractFieldsPage = fieldsPage.clickContractFields();

        contractFieldsPage.clickEditValues("Contracting department");

        contractFieldsPage.setNewValue("Contracting department", "department1", 1);
        contractFieldsPage.setNewValue("Contracting department", "department2", 2);

        fieldsPage.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
