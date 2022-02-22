package tests.regression.at142;

import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.ExecutedContractsPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(CleanUp.class);


    @Test(priority = 1)
    public void loginAsMyTeamCN()
    {
        // Login under my team CN
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());

        sideBar = loginPage.clickSignIn().getSideBar();
    }

    @Test(priority = 2)
    public void restoringUserGregRoles()
    {
        ///
        logger.info("Restoring user Greg roles...");
              sideBar.clickAdministration()
                     .clickManageUsersTab()
                     .clickActionMenu(Const.USER_GREG.getFirstName())
                     .clickEdit()
                     .deleteRole("Viewer Plus")
                     .clickUpdateUser();

        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("updated successfully"));
    }

    @Test(priority = 3)
    public void removeExecutedContracts()
    {
        ///
        logger.info("Removing contracts...");
        ExecutedContractsPage executedContractsPage = sideBar.clickExecutedContracts(false);

        for( int i = 0; i <= 2; i++ )
        {
            String contractName = "Contract " + i;

            AddDocuments addDocuments = executedContractsPage.selectContractWithoutUploadedDoc(contractName);
            new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();

            $(".notification-stack").should(Condition.appear).shouldHave(Condition.text(" has been deleted."));
            $(".notification-stack").should(Condition.disappear);
        }
    }
}
