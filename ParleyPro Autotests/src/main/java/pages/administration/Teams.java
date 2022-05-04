package pages.administration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.add.AddNewTeam;
import org.apache.log4j.Logger;
import pages.tooltips.TeamActionMenu;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents selected team tab
 */
public class Teams
{
    private SelenideElement addNewTeamButton = $("._button.scheme_gray.size_md");


    private static Logger logger = Logger.getLogger(Teams.class);

    public Teams()
    {
        $(".spinner").waitUntil(Condition.disappear, 30_000);
        $(".teams__label").waitUntil(Condition.visible, 30_000).shouldHave(Condition.exactText("Teams"));
        $(".teams__description").waitUntil(Condition.visible, 30_000).shouldHave(Condition.exactText("Form your team by inviting internal stakeholders."));
        $(".teams-list tbody").waitUntil(Condition.visible, 30_000);
    }

    public AddNewTeam clickAddNewTeam()
    {
        addNewTeamButton.click();

        logger.info("+ New Team button was clicked");

        return new AddNewTeam();
    }

    /**
     * Invoke action menu for team by teamName. Click by 3 dots button
     * @param teamName name of the team for which action menu should be invoked
     */
    public TeamActionMenu clickActionMenu(String teamName)
    {
        $$(".teams-list__name").filterBy(Condition.text(teamName)).first().parent().find("button").shouldBe(Condition.visible).click();

        logger.info("Action menu was clicked for " + teamName);

        return new TeamActionMenu();
    }
}
