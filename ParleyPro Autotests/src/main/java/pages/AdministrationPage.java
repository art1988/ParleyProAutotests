package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import pages.administration.*;

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
        title.waitUntil(Condition.visible, 16_000).shouldHave(Condition.exactText("Administration"));

        // check that tabs are exists
        $(".landing-administration-subheaders").waitUntil(Condition.visible, 16_000).shouldHave(Condition.exactText("Manage usersIntegrationsWorkflowsFieldsTeams"));
    }

    public ManageUsers clickManageUsersTab()
    {
        manageUsersTab.click();

        logger.info("Manage users tab was clicked");

        return new ManageUsers();
    }

    public Integrations clickIntegrationsTab()
    {
        integrationsTab.click();

        logger.info("Integrations tab was clicked");

        return new Integrations();
    }

    public Workflows clickWorkflowsTab()
    {
        workflowsTab.click();

        logger.info("Workflows tab was clicked");

        return new Workflows();
    }

    public Fields clickFieldsTab()
    {
        fieldsTab.click();

        logger.info("Fields tab was clicked");

        return new Fields();
    }

    public Teams clickTeamsTab()
    {
        teamsTab.click();

        logger.info("Teams tab was clicked");

        return new Teams();
    }
}
