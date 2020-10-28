package pages.administration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.AddNewTeam;
import org.apache.log4j.Logger;
import pages.tooltips.TeamActionMenu;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents selected team tab
 */
public class Teams
{
    private SelenideElement addNewTeamButton = $("._button.scheme_gray.size_md");


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

    /**
     * Invoke action menu for team by teamName. Click by 3 dots button
     * @param teamName name of the team for which action menu should be invoked
     */
    public TeamActionMenu clickActionMenu(String teamName)
    {
        Selenide.executeJavaScript("$('.teams-list__name:contains(\"" + teamName + "\")').parent().find(\".actions-menu button\").click()");

        logger.info("Action menu was clicked for " + teamName);

        return new TeamActionMenu();
    }
}
