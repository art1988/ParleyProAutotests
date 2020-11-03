package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import pages.administration.Teams;
import pages.administration.Workflows;

import static com.codeborne.selenide.Selenide.$;

public class AdministrationPage
{
    private SelenideElement title = $(".landing-administration__head");

    private SelenideElement manageUsersTab  = $("a[href*='#/admin/usermanagement']");
    private SelenideElement integrationsTab = $("a[href*='#/admin/integrations']");
    private SelenideElement workflowsTab    = $("a[href*='#/admin/workflows']");
    private SelenideElement fieldsTab       = $("a[href*='#/admin/fields']");
    private SelenideElement teamsTab        = $("a[href*='#/admin/teams']");


    private static Logger logger = Logger.getLogger(AdministrationPage.class);

    public AdministrationPage()
    {
        // check that title exists
        title.waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Administration"));

        // check that tabs are exists
        $(".landing-administration-subheaders").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Manage usersIntegrationsWorkflowsFieldsTeams"));
    }

    public Workflows clickWorkflowsTab()
    {
        workflowsTab.click();

        logger.info("Workflows tab was clicked");

        return new Workflows();
    }

    public Teams clickTeamsTab()
    {
        teamsTab.click();

        logger.info("Teams tab was clicked");

        return new Teams();
    }
}
