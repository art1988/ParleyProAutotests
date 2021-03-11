package forms.add;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represent form that appears after clicking on + NEW USER or Edit user
 */
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

    /**
     * Click by + Add role blue link
     */
    public void clickAddRole()
    {
        addRoleButton.click();

        logger.info("+ Add role was clicked");
    }

    /**
     * Set role.
     * @param roleName may be 'Chief Negotiator', 'Contract Manager', 'Viewer Plus', 'Viewer', 'Admin' or 'Requester'
     */
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

    /**
     * Delete role by role name
     * @param roleName
     */
    public void deleteRole(String roleName)
    {
        Selenide.executeJavaScript("$('.ui-tr input[value=\"" + roleName + "\"]').parent().parent().parent().parent().parent().parent().find('.contract-execute-form__remove').click()");

        logger.info("Role " + roleName + " was removed...");
    }

    public void setRegion(String regionName)
    {
        if( getRegion().equals("All regions") )
        {
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__region input:visible')[0]")).click(); // Expand Region dropdown
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('All')\")[0]")).click(); // Uncheck all by clicking All regions
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('" + regionName + "')\")[0]")).click(); // Check Region
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__region input:visible')[0]")).click(); // Collapse Region dropdown
        }
        else
        {
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__region input:visible')[0]")).click(); // Expand Region dropdown
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('All')\")[0]")).click(); // Click all regions first time to select all
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('All')\")[0]")).click(); // Click all regions again to unselect all
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('" + regionName + "')\")[0]")).click(); // Check Region
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__region input:visible')[0]")).click(); // Collapse Region dropdown
        }

        logger.info("Region: " + regionName + " was selected...");
    }

    public String getRegion()
    {
        return Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__region input:visible').val()");
    }

    public void setContractCategory(String categoryName)
    {
        if( getContractCategory().equals("All categories") )
        {
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__category input:visible')[0]")).click(); // Expand Contract category dropdown
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('All')\")[0]")).click(); // Uncheck all by clicking All categories
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('" + categoryName + "')\")[0]")).click(); // Check category
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__category input:visible')[0]")).click(); // Collapse Contract category dropdown
        }
        else
        {
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__category input:visible')[0]")).click(); // Expand Contract category dropdown
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('All')\")[0]")).click(); // Click all categories first time to select all
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('All')\")[0]")).click(); // Click all categories again to unselect all
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('" + categoryName + "')\")[0]")).click(); // Check category
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__category input:visible')[0]")).click(); // Collapse Contract category dropdown
        }

        logger.info("Contract category: " + categoryName + " was selected...");
    }

    public String getContractCategory()
    {
        return Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__category input:visible').val()");
    }

    public void setDepartment(String department)
    {
        if( getDepartment().equals("All departments") )
        {
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__department input:visible')[0]")).click(); // Expand Department dropdown
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('All')\")[0]")).click(); // Uncheck all by clicking All departments
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('" + department + "')\")[0]")).click(); // Check Department
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__department input:visible')[0]")).click(); // Collapse Department dropdown
        }
        else
        {
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__department input:visible')[0]")).click(); // Expand Department dropdown
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('All')\")[0]")).click(); // Click all departments first time to select all
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('All')\")[0]")).click(); // Click all departments again to unselect all
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .dropdown-menu:visible').find(\".checkbox__label:contains('" + department + "')\")[0]")).click(); // Check Department
            $((WebElement) Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__department input:visible')[0]")).click(); // Collapse Department dropdown
        }

        logger.info("Department: " + department + " was selected...");
    }

    public String getDepartment()
    {
        return Selenide.executeJavaScript("return $('.modal-body .user-edit-roles__department input:visible').val()");
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

    public void clickUpdateUser()
    {
        addUserButton.click(); // button UPDATE USER has the same classname as ADD USER

        logger.info("UPDATE USER button was clicked");

        $(".modal-body").waitUntil(Condition.disappear, 7_000);
    }
}
