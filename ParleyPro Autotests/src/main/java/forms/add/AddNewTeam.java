package forms.add;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.testng.Assert;
import pages.administration.Teams;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents page that visible after clicking of + NEW TEAM button or by clicking Edit of existing team
 */
public class AddNewTeam
{
    private SelenideElement teamNameFiled        = $("input[data-label='Team name']");
    private SelenideElement teamDescriptionField = $(".textarea.input__input");
    private SelenideElement addMembersButton     = $("._button.scheme_gray.size_md");
    private SelenideElement saveButton           = $("#editTeamSave");
    private SelenideElement cancelButton         = $("#editTeamCancel");


    private static Logger logger = Logger.getLogger(AddNewTeam.class);

    public AddNewTeam()
    {
        // check that it has gear image
        boolean hasImage = $(".team-icon").getCssValue("background-image").contains("images/d8f66bdcf20bfedb3a09672478d1516b.svg");

        Assert.assertTrue(hasImage);
    }

    public void setTeamName(String teamName)
    {
        teamNameFiled.setValue(teamName);
    }

    public void setTeamDescription(String teamDescription)
    {
        teamDescriptionField.setValue(teamDescription);
    }

    public AddMembers clickAddMembersButton()
    {
        addMembersButton.click();

        logger.info("+ Add Members button was clicked");

        return new AddMembers();
    }

    /**
     * Delete user from team
     * @param userName name of user to be deleted
     */
    public void deleteUser(String userName)
    {
        // click actions menu
        Selenide.executeJavaScript("$('.teams-users-list .teams-users-list__name:contains(\"" + userName + "\")').next().next().next().find(\"#actions-menu\").click()");
        // click Delete
        Selenide.executeJavaScript("$('.teams-users-list .teams-users-list__name:contains(\"" + userName + "\")').next().next().next().find(\"#actions-menu\").next().find(\"a\")[0].click()");

        logger.info("User " + userName + " was deleted...");
    }

    public Teams clickSave()
    {
        saveButton.click();

        logger.info("Save button was clicked");

        return new Teams();
    }
}
