package tests.basics.at225;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import constants.FieldType;
import forms.add.AddNewUser;
import io.qameta.allure.Description;
import model.User;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CreateParleyProAccountPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.administration.Fields;
import pages.administration.ManageUsers;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.subelements.SideBar;
import utils.EmailChecker;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AddNewUserTest
{
    private SideBar sideBar;

    // User that will be added
    private User newUser = new User("AT User first_name", "last_name AT", "arthur.khasanov+at_user@parleypro.com", "Parley650!");

    private static Logger logger = Logger.getLogger(AddNewUserTest.class);

    @Test(priority = 1)
    @Description("This test goes to administration, creates new user and verifies that is was saved")
    public void addNewUser()
    {
        sideBar = new DashboardPage().getSideBar();

        ManageUsers manageUsersTab = sideBar.clickAdministration().clickManageUsersTab();

        AddNewUser addNewUserForm = manageUsersTab.clickAddNewUser();

        addNewUserForm.setFirstName(newUser.getFirstName());
        addNewUserForm.setLastName(newUser.getLastName());
        addNewUserForm.setEmail(newUser.getEmail());
        addNewUserForm.clickAddRole();
        addNewUserForm.setRole("Requester");
        addNewUserForm.clickAddUser();

        // Re-click tabs to refresh page
        sideBar.clickAdministration().clickTeamsTab();
        sideBar.clickAdministration().clickManageUsersTab();

        logger.info("Assert that user is in the list");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.usermanagement__userlist_content_row_fullname:contains(\"" + newUser.getFirstName() + "\")').length === 1"),
                "Just added user is not in the list !!!");

        logger.info("Edit of just created user and assert that all fields were saved correctly...");
        addNewUserForm = manageUsersTab.clickActionMenu(newUser.getFirstName()).clickEdit();

        Assert.assertEquals(addNewUserForm.getFirstName(), newUser.getFirstName(), "First name of user wasn't saved !!!");
        Assert.assertEquals(addNewUserForm.getLastName(), newUser.getLastName(), "Last name of user wasn't saved !!!");
        Assert.assertEquals(addNewUserForm.getEmail(), newUser.getEmail(), "Email of user wasn't saved !!!");
        Assert.assertTrue(addNewUserForm.getRole().equals("Requester"), "Role of user wasn't saved !!!");

        Screenshoter.makeScreenshot();

        addNewUserForm.clickCancel();
    }

    @Test(priority = 2)
    public void addRequestFieldAndLogout()
    {
        Fields fieldsPage = sideBar.clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsPage.clickContractFields();

        contractFields.createNewFiled("Contract Request", "at225_requestField", FieldType.MULTI_SELECT, false);
        contractFields.addValues("at225_requestField", "v1");

        fieldsPage.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));
        $("input[value='at225_requestField']").shouldBe(Condition.visible);

        sideBar.logout();
    }

    @Test(priority = 3)
    @Description("This test checks that invitation email was sent, register new user, logins and check that button + NEW REQUEST present.")
    public void getEmailRegisterAndCheckButton() throws InterruptedException
    {
        logger.info("Waiting for 70 seconds to make sure that email has been delivered...");
        Thread.sleep(70_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Role assignment: Requester"),
                "Email with subject: Role assignment: Requester was not found !!!");

        String bodyText = EmailChecker.assertEmailBodyText("You have been designated as the Requester");
        int start = bodyText.indexOf("<"),
            end   = bodyText.indexOf(">");
        String URLFromSignInButton = bodyText.substring(start + 1, end);
        URLFromSignInButton = URLFromSignInButton.replaceAll("amp;", ""); // remove amp; symbol

        logger.info("Open URL from SIGN IN button...");
        Selenide.open(URLFromSignInButton);

        logger.info("Register new user...");
        CreateParleyProAccountPage createParleyProAccountPage = new CreateParleyProAccountPage();

        createParleyProAccountPage.setFirstName(newUser.getFirstName());
        createParleyProAccountPage.setLastName(newUser.getLastName());
        createParleyProAccountPage.setPassword(newUser.getPassword());
        createParleyProAccountPage.setConfirmPassword(newUser.getPassword());
        createParleyProAccountPage.clickCreateAndSignIn(true);

        logger.info("Making sure that only +NEW REQUEST button is visible...");
        $$("button").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText("NEW REQUEST"));
        Screenshoter.makeScreenshot();

        sideBar.logout();
    }

    @Test(priority = 4)
    @Description("This test adds CN role to existing user, logins as this user and checks that both buttons +NEW CONTRACT and +NEW REQUEST are visible.")
    public void addOneMoreRoleCN() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());
        loginPage.clickSignIn();

        ManageUsers manageUsersTab = sideBar.clickAdministration().clickManageUsersTab();

        AddNewUser editUser = manageUsersTab.clickActionMenu(newUser.getFirstName()).clickEdit();

        editUser.clickAddRole();
        editUser.setRole("Chief Negotiator");
        editUser.clickUpdateUser();

        logger.info("Waiting for 70 seconds to make sure that email has been delivered...");
        Thread.sleep(70_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Role assignment: Chief Negotiator"),
                "Email with subject: Role assignment: Chief Negotiator was not found !!!");

        String bodyText = EmailChecker.assertEmailBodyText("You have been designated as the Chief Negotiator");
        int start = bodyText.indexOf("<"),
            end   = bodyText.indexOf(">");
        String URLFromSignInButton = bodyText.substring(start + 1, end);
        URLFromSignInButton = URLFromSignInButton.replaceAll("amp;", ""); // remove amp; symbol

        logger.info("Open URL from SIGN IN button...");
        Selenide.open(URLFromSignInButton);

        logger.info("Signing in as just added new user...");
        loginPage = new LoginPage();

        loginPage.setEmail(newUser.getEmail());
        loginPage.setPassword(newUser.getPassword());
        loginPage.clickSignIn();

        logger.info("Making sure that only 2 buttons are visible: +NEW CONTRACT and +NEW REQUEST...");
        $$("button").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("NEW CONTRACT", "NEW REQUEST"));
        Screenshoter.makeScreenshot();

        sideBar.logout();
    }

    @Test(priority = 5)
    @Description("This test delete all previous roles, add new role Contract Manager and checks that email was delivered")
    public void deleteAllRolesAndAddContractManagerRole() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());
        loginPage.clickSignIn();

        ManageUsers manageUsersTab = sideBar.clickAdministration().clickManageUsersTab();

        AddNewUser editUser = manageUsersTab.clickActionMenu(newUser.getFirstName()).clickEdit();

        editUser.deleteRole("Requester");
        editUser.deleteRole("Chief Negotiator");

        editUser.clickAddRole();
        editUser.setRole("Contract Manager");
        editUser.clickUpdateUser();

        logger.info("Waiting for 70 seconds to make sure that email has been delivered...");
        Thread.sleep(70_000);

        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Role assignment: Contract Manager"),
                "Email with subject: Role assignment: Contract Manager was not found !!!");
    }

    @Test(priority = 6)
    @Description("This test delete user and request field.")
    public void cleanUp() throws InterruptedException
    {
        ManageUsers manageUsersTab = sideBar.clickAdministration().clickManageUsersTab();

        manageUsersTab.clickActionMenu(newUser.getFirstName()).clickDelete(newUser).clickDelete();
        Thread.sleep(1_000);

        logger.info("Assert that user is no longer in the list...");
        Assert.assertFalse(Selenide.executeJavaScript("return $('.usermanagement__userlist_content_row_fullname:contains(\"AT User first_name\")').length === 1"));


        Fields fieldsPage = sideBar.clickAdministration().clickFieldsTab();
        ContractFields contractFields = fieldsPage.clickContractFields();

        contractFields.removeField("at225_requestField", "Contract Request").clickDelete();
        fieldsPage.clickSave();

        logger.info("Assert that fields were completely removed...");
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields__title:contains(\"Contract Request\")').parent().find(\".admin-fields-staticfield.js-item\").length === 0"),
                "Looks like that not all fields were removed from Contract Request !!!");
    }
}
