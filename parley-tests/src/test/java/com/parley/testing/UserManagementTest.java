package com.parley.testing;

import com.parley.testing.listener.UITestListener;
import com.parley.testing.model.roles.User;
import com.parley.testing.model.roles.UserRoles;
import com.parley.testing.pages.impl.LoginPage;
import com.parley.testing.pages.impl.admin.UserManagementPage;
import com.parley.testing.pages.impl.dashboard.AdministrationPage;
import com.parley.testing.runner.AbstractIT;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Map;

@Listeners(UITestListener.class)
public class UserManagementTest extends AbstractIT {

    @Test(enabled = false)
    public void testAddAndDeleteUser() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria+admin@parleypro.com","Parley650!");
        //Validate administration page is available

        AdministrationPage administrationPage = pageFactory.administrationPage();
        administrationPage.checkCurrentPage();
        administrationPage.moveToPage();

        UserManagementPage userManagementPage = pageFactory.userManagementPage();
        userManagementPage.checkManageUsersTabExists();
        userManagementPage.checkNewUserButtonExists();

        String newUserEmail="victoria+test2@parleypro.com";
        userManagementPage.createNewUser("1", "1", newUserEmail, Arrays.asList(UserRoles.CN.getRole()));
        Map<String, User> users = userManagementPage.getUsers();
        userManagementPage.checkUserExists(users, newUserEmail);
        userManagementPage.deleteUser(users, newUserEmail);

        userManagementPage.checkUserDoesntExist(userManagementPage.getUsers(), newUserEmail);
    }


    @Test
    public void emptyEmail() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria+admin@parleypro.com","Parley650!");
        //Validate administration page is available

        AdministrationPage administrationPage = pageFactory.administrationPage();
        administrationPage.checkCurrentPage();
        administrationPage.moveToPage();

        UserManagementPage userManagementPage = pageFactory.userManagementPage();
        userManagementPage.checkManageUsersTabExists();
        userManagementPage.checkNewUserButtonExists();
        userManagementPage.createNewUser("test","test","", Arrays.asList(""));
        userManagementPage.checkCreateUserErrorMessage();
    }


}
