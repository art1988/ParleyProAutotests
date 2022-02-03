package tests.basics.at224;

import com.codeborne.selenide.Condition;
import constants.Const;
import model.User;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.Cache;

import static com.codeborne.selenide.Selenide.$;


public class CleanUp
{
    private static Logger logger = Logger.getLogger(CleanUp.class);


    @Test
    public void cleanUp()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        logger.info("Deleting contract 'AT-224: share me'...");

        SideBar sideBar = dashboardPage.getSideBar();
        sideBar.clickInProgressContracts(false).selectContract("AT-224: share me");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract AT-224: share me has been deleted."));

        User userToDelete = Cache.getInstance().getCachedUser();
        logger.info("Deleting user " + userToDelete);

        sideBar.clickAdministration().clickManageUsersTab().clickActionMenu(userToDelete.getFirstName()).clickDelete(userToDelete).clickDelete();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" deleted successfully"));
    }
}
