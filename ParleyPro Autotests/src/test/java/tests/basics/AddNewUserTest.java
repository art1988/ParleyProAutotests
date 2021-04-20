package tests.basics;

import com.codeborne.selenide.Selenide;
import forms.add.AddNewUser;
import io.qameta.allure.Description;
import model.User;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.ManageUsers;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

@Listeners({ScreenShotOnFailListener.class})
public class AddNewUserTest
{
    private String host = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    // User that will be added
    private User newUser = new User("AT User first_name", "last_name AT", "arthur.khasanov+at_user@parleypro.com", "");

    private static Logger logger = Logger.getLogger(AddNewUserTest.class);

    @Test(priority = 1)
    @Description("This test goes to administration, creates new user and verifies that is was saved")
    public void addNewUser()
    {
        DashboardPage dashboardPage = new DashboardPage();

        ManageUsers manageUsersTab = dashboardPage.getSideBar().clickAdministration().clickManageUsersTab();

        AddNewUser addNewUserForm = manageUsersTab.clickAddNewUser();

        addNewUserForm.setFirstName(newUser.getFirstName());
        addNewUserForm.setLastName(newUser.getLastName());
        addNewUserForm.setEmail(newUser.getEmail());
        addNewUserForm.clickAddRole();
        addNewUserForm.setRole("Requester");
        addNewUserForm.clickAddUser();

        // Re-click tabs to refresh page
        dashboardPage.getSideBar().clickAdministration().clickTeamsTab();
        dashboardPage.getSideBar().clickAdministration().clickManageUsersTab();

        logger.info("Assert that user is in the list");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.usermanagement__userlist_content_row_fullname:contains(\"" + newUser.getFirstName() + "\")').length === 1"),
                "Just added user is not in the list !!!");

        logger.info("Edit of just created user and assert that all fields were saved correctly...");
        addNewUserForm = manageUsersTab.clickActionMenu(newUser.getFirstName()).clickEdit();

        Assert.assertEquals(addNewUserForm.getFirstName(), newUser.getFirstName());
        Assert.assertEquals(addNewUserForm.getLastName(), newUser.getLastName());
        Assert.assertEquals(addNewUserForm.getEmail(), newUser.getEmail());
        Assert.assertTrue(addNewUserForm.getRoles().contains("Requester"));

        Screenshoter.makeScreenshot();

        addNewUserForm.clickCancel();
    }

    @Test(priority = 2)
    @Description("This test checks that invitation email was sent")
    public void checkInvitationEmail()
    {
        logger.info("Waiting for 30 seconds to make sure that email has been delivered...");
        try
        {
            Thread.sleep(30_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Role assignment: Requester"),
                "Email with subject: Role assignment: Requester was not found !!!");
    }

    @Test(priority = 3)
    @Description("This test adds CN role to existing user")
    public void addNewRole()
    {
        ManageUsers manageUsersTab = new DashboardPage().getSideBar().clickAdministration().clickManageUsersTab();

        AddNewUser editUser = manageUsersTab.clickActionMenu(newUser.getFirstName()).clickEdit();

        editUser.clickAddRole();
        editUser.setRole("Chief Negotiator");
        editUser.clickUpdateUser();

        logger.info("Waiting for 30 seconds to make sure that email has been delivered...");
        try
        {
            Thread.sleep(30_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Role assignment: Chief Negotiator"),
                "Email with subject: Role assignment: Chief Negotiator was not found !!!");
    }

    @Test(priority = 4)
    @Description("This test delete all previous roles, add new role Contract Manager and checks that email was delivered")
    public void deleteAllRolesAndAddContractManagerRole()
    {
        ManageUsers manageUsersTab = new DashboardPage().getSideBar().clickAdministration().clickManageUsersTab();

        AddNewUser editUser = manageUsersTab.clickActionMenu(newUser.getFirstName()).clickEdit();

        editUser.deleteRole("Requester");
        editUser.deleteRole("Chief Negotiator");

        editUser.clickAddRole();
        editUser.setRole("Contract Manager");
        editUser.clickUpdateUser();

        logger.info("Waiting for 30 seconds to make sure that email has been delivered...");
        try
        {
            Thread.sleep(30_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Role assignment: Contract Manager"),
                "Email with subject: Role assignment: Contract Manager was not found !!!");
    }

    @Test(priority = 5)
    @Description("This test delete user")
    public void cleanUp()
    {
        DashboardPage dashboardPage = new DashboardPage();

        ManageUsers manageUsersTab = dashboardPage.getSideBar().clickAdministration().clickManageUsersTab();

        manageUsersTab.clickActionMenu(newUser.getFirstName()).clickDelete(newUser).clickDelete();

        try
        {
            Thread.sleep(1_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        logger.info("Assert that user is no longer in the list...");
        Assert.assertFalse(Selenide.executeJavaScript("return $('.usermanagement__userlist_content_row_fullname:contains(\"AT User first_name\")').length === 1"));

        Screenshoter.makeScreenshot();
    }
}
