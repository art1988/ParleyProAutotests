package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

public class AddNewTeam
{
    private SelenideElement teamNameFiled        = $("input[label='Team name']");
    private SelenideElement teamDescriptionField = $(".textarea.input__input");
    private SelenideElement addMembersButton     = $("._button.scheme_gray.size_md");


    private static Logger logger = Logger.getLogger(AddNewTeam.class);

    public AddNewTeam()
    {
        $(".team-info__name").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Team name"));

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
}
