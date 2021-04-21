package pages.administration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.add.AddNewUser;
import org.apache.log4j.Logger;
import pages.tooltips.UserActionMenu;

import static com.codeborne.selenide.Selenide.$;

public class ManageUsers
{
    private SelenideElement newUserButton = $("button[tooltip='Add new user']");


    private static Logger logger = Logger.getLogger(ManageUsers.class);

    public ManageUsers()
    {
        $(".spinner").waitUntil(Condition.disappear, 7_000);
        $(".usermanagement__userlist_header").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("User\nEmailRoleLast loginEnable"));
    }

    public AddNewUser clickAddNewUser()
    {
        newUserButton.click();

        logger.info("+ NEW USER was clicked...");

        return new AddNewUser(false);
    }

    /**
     * Invoke action menu for user by userName. Click by 3 dots button
     * @param userName name of the user for which action menu should be invoked
     * @return
     */
    public UserActionMenu clickActionMenu(String userName)
    {
        Selenide.executeJavaScript("$('.usermanagement__userlist_content_row_fullname:contains(\"" + userName + "\")').parent().find(\".usermanagement__userlist_content_row_menu button\").click()");

        return new UserActionMenu();
    }

    /**
     * Sometimes username was not set, except email. Use this method to delete user only by email
     * @param userEmail
     * @return
     */
    public UserActionMenu clickActionMenuByEmail(String userEmail)
    {
        Selenide.executeJavaScript("$('.usermanagement__userlist_content_row_email:contains(\"" + userEmail + "\")').parent().find(\".usermanagement__userlist_content_row_menu button[id='dropdown-icon']\").click()");

        return new UserActionMenu();
    }

    /**
     * Enable or disable user by clicking blue toggle.
     * @param user name of the user that will be enabled/disabled
     * @param state set true to enable, false to disable
     */
    public void switchEnableToggle(String user, boolean state)
    {
        boolean currentStateOfUser = Selenide.executeJavaScript("return $('.usermanagement__userlist_content_row_fullname:contains(\"" + user + "\")').parent().find(\".tumbler\").hasClass(\"checked\")");

        if( state == true )
        {
            if( currentStateOfUser == true )
            {
                logger.info("User " + user + " is already enabled...");
                return;
            }
            else
            {
                Selenide.executeJavaScript("$('.usermanagement__userlist_content_row_fullname:contains(\"" + user + "\")').parent().find(\".tumbler\").click()");
                logger.info("User " + user + " was set as enabled...");
                $(".spinner").waitUntil(Condition.disappear, 7_000);
            }
        }
        else
        {
            if( currentStateOfUser == true )
            {
                Selenide.executeJavaScript("$('.usermanagement__userlist_content_row_fullname:contains(\"" + user + "\")').parent().find(\".tumbler\").click()");
                logger.info("User " + user + " was disabled...");
                $(".spinner").waitUntil(Condition.disappear, 7_000);
            }
            else
            {
                logger.info("User " + user + " is already disabled...");
                return;
            }
        }
    }
}
