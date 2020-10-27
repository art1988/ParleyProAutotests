package pages.administration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import forms.AddNewTeam;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class Teams
{
    private SelenideElement addNewTeamButton = $("._button.scheme_gray.size_md");
    private SelenideElement saveButton       = $("");


    private static Logger logger = Logger.getLogger(Teams.class);

    public Teams()
    {
        $(".teams__label").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Teams"));
        $(".teams__description").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Form your team by inviting internal stakeholders."));
    }

    public AddNewTeam clickAddNewTeam()
    {
        addNewTeamButton.click();

        logger.info("+ New Team button was clicked");

        return new AddNewTeam();
    }
}
