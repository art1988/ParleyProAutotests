package com.parley.testing.pages.impl.admin;

import com.parley.testing.model.roles.User;
import com.parley.testing.pages.impl.dashboard.AdministrationPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;

public class UserManagementPage extends AdministrationPage {

    private static final By MANAGE_USERS_TAB = By.xpath("//a[contains(text(), 'Manage users')]");

    //Buttons
    private static final By NEW_USER_BUTTON =  By.xpath("//button[contains(text(), 'NEW USER')]");
    private static final By ADD_USER =  By.xpath("//button[contains(text(), 'Add user')]");
    private static final By ADD_ROLE = By.xpath("//div[contains(@class, 'contract-execute-form__add')]");
    private static final By EDIT_USER =  By.xpath("//a[contains(text(), 'Edit')]");
    private static final By DELETE_USER =  By.xpath("//a[contains(text(), 'Delete')]");
    private static final By DELETE_USER_CONFIRM =  By.xpath("//button[contains(text(), 'Delete user')]");

    //Inputs
    private static final By FIRST_NAME = By.cssSelector("input[label='First name']");
    private static final By LAST_NAME = By.cssSelector("input[label='Last name']");
    private static final By EMAIL = By.cssSelector("input[label='Email']");
    private static final By USER_ROLE = By.xpath("//input[contains(@class, 'select__input') and contains(@class ,'input__input')]");

    //Errors
    private static final By ERROR_MESSAGE_ELEMENT = By.xpath("//div[contains(@class,'input__error')]");
    private static final String EMPTY_EMAIL_ERROR_MESSAGE_TEXT = "\"email\" is not allowed to be empty";

    //Users List

    //contract form
    public static final By USER_ROW = By.xpath("//div[contains(@class, 'usermanagement__userlist_content_row')]");
    public static final By USER_EMAIL = By.xpath(".//span[contains(@class, 'usermanagement__userlist_content_row_email')]");
    public static final By USER_ROLES = By.xpath(".//span[contains(@class, 'usermanagement__userlist_content_row_role')]");
    public static final By USER_ENABLE = By.xpath(".//span[contains(@class, 'usermanagement__userlist_content_row_enable')]");
    public static final By USER_MENU = By.xpath(".//span[contains(@class, 'usermanagement__userlist_content_row_menu')]");
    public static final By USER_NAME = By.xpath(".//span[contains(@class, 'usermanagement__userlist_content_row_fullname')]");


    public UserManagementPage(WebDriver driverProvider) {
        super(driverProvider);
    }

    public void checkManageUsersTabExists(){
        waitUntilElementIsDisplayed(MANAGE_USERS_TAB);
    }

    public void checkNewUserButtonExists(){
        waitUntilElementIsEnabled(MANAGE_USERS_TAB);
        move(MANAGE_USERS_TAB);
        waitUntilElementIsDisplayed(NEW_USER_BUTTON);
    }

    public void createNewUser(String firstName, String lastName, String email, List<String> roles) {
        findElement(NEW_USER_BUTTON).click();
        clearAndEnterValue(FIRST_NAME, firstName);
        clearAndEnterValue(LAST_NAME, lastName);
        clearAndEnterValue(EMAIL, email);

        addUserRoles(roles);
        findElement(ADD_USER).click();

    }

    public void addUserRoles(List<String> roles){
        for(String role: roles)
        {
            if(!role.isEmpty()){
                findElement(ADD_ROLE).click();
                clearAndEnterValue(USER_ROLE, role);
            }
        }
    }

    public void checkCreateUserErrorMessage(){
        waitUntilElementIsDisplayed(ERROR_MESSAGE_ELEMENT);
        assertEquals(EMPTY_EMAIL_ERROR_MESSAGE_TEXT, findElement(ERROR_MESSAGE_ELEMENT).getText());
    }

    public void checkUserExists(Map<String, User> users, String email){
        assertNotNull(users.get(email));
    }

    public void checkUserDoesntExist(Map<String, User> users, String email){
        assertNull(users.get(email));
    }

    public void deleteUser(Map<String, User> users, String email){
        User user = users.get(email);
        if(user != null){
            user.getUserMenu().click();
            WebElement delete = findElement(DELETE_USER);
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript("arguments[0].click();",delete);

            waitUntilElementIsDisplayed(DELETE_USER_CONFIRM);
            findElement(DELETE_USER_CONFIRM).click();
        }
    }


    public Map<String, User> getUsers() throws InterruptedException {
        Thread.sleep(3000);
        Map<String, User> users = new HashMap<String, User>();
        List<WebElement> elements = findElements(USER_ROW);
        int i=0;
        for (WebElement element: elements){
            User user = new User();
            user.setName(element.findElement(USER_NAME).getText());
            user.setEnableWebElement(element.findElement(USER_ENABLE));
            user.setUserMenu(element.findElement(USER_MENU));

            String rolesAsString = element.findElement(USER_ROLES).getText();
            if(!rolesAsString.isEmpty()){
                user.setRoles(Arrays.asList(rolesAsString.split(",")));
            }
            String email = element.findElement(USER_EMAIL).getText();
            user.setEmail(element.findElement(USER_EMAIL).getText());
            user.setNumber(i);
            users.put(email, user);
            ++i;
        }
        return users;
    }


}
