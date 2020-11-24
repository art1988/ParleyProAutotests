package pages.tooltips;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.AddNewUser;
import forms.DeleteUser;
import model.User;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Popup that appears after clicking on 3 dots button on Manage users tab page for given user
 */
public class UserActionMenu
{
    private String user;


    private static Logger logger = Logger.getLogger(UserActionMenu.class);

    public UserActionMenu(String user)
    {
        this.user = user;

        $(".dropdown.open.btn-group ul").waitUntil(Condition.visible, 6_000);
        $$(".dropdown.open.btn-group ul li").shouldHaveSize(2).shouldHave(CollectionCondition.exactTexts("Edit", "Delete"));
    }

    public AddNewUser clickEdit()
    {
        Selenide.executeJavaScript("$('.dropdown.open.btn-group ul').find('li:contains(\"Edit\")').find('a')[0].click()");

        logger.info("Edit user menu item was clicked...");

        return new AddNewUser(true);
    }

    public DeleteUser clickDelete(User user)
    {
        Selenide.executeJavaScript("$('.dropdown.open.btn-group ul').find('li:contains(\"Delete\")').find('a')[0].click()");

        logger.info("Delete user menu item was clicked...");

        return new DeleteUser(user);
    }
}
