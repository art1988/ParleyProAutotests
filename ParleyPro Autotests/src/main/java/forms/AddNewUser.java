package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.$;

public class AddNewUser
{
    private SelenideElement title          = $(".modal-body-title");
    private SelenideElement firstNameField = $("input[label='First name']");
    private SelenideElement lastNameField  = $("input[label='Last name']");
    private SelenideElement emailField     = $("input[label='Email']");
    private SelenideElement addRoleButton  = $(".contract-execute-form__add");

    private SelenideElement cancelButton  = $(".button.btn-common.btn-link-pseudo.btn.btn-link");
    private SelenideElement addUserButton = $(".button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(AddNewUser.class);

    /**
     * If edit is true then title of form is Update user. Add new user otherwise
     * @param edit
     */
    public AddNewUser(boolean edit)
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        if( edit == true )
        {
            title.waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Update user"));

            try
            {
                Thread.sleep(1_000);
            }
            catch (InterruptedException e)
            {
                logger.error("InterruptedException", e);
            }
        }
        else
        {
            title.waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Add new user"));
        }
    }

    public void setFirstName(String firstName)
    {
        firstNameField.setValue(firstName);
    }

    public String getFirstName()
    {
        return firstNameField.getValue();
    }

    public void setLastName(String lastName)
    {
        lastNameField.setValue(lastName);
    }

    public String getLastName()
    {
        return lastNameField.getValue();
    }

    public void setEmail(String email)
    {
        emailField.setValue(email);
    }

    public String getEmail()
    {
        return emailField.getValue();
    }

    public void clickAddRole()
    {
        addRoleButton.click();

        logger.info("+ Add role was clicked");
    }

    public void setRole(String roleName)
    {
        $(".select.is_empty input").setValue(roleName);
    }

    public ArrayList getRoles()
    {
        StringBuffer jsCode = new StringBuffer("var ar = $('.user-edit-roles__role.ui-td input'); ");
        jsCode.append("var vals = ar.map(function(){ return $(this).val(); }).get(); ");
        jsCode.append("return vals; ");

        return Selenide.executeJavaScript(jsCode.toString());
    }

    public void clickCancel()
    {
        cancelButton.click();

        logger.info("CANCEL button was clicked");
    }

    public void clickAddUser()
    {
        addUserButton.click();

        logger.info("ADD USER button was clicked");
    }
}
