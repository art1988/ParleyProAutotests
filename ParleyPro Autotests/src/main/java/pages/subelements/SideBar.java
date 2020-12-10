package pages.subelements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.testng.Assert;
import pages.*;

import static com.codeborne.selenide.Selenide.$;

/**
 * Left sidebar. Each page has it.
 */
public class SideBar
{
    private SelenideElement priorityDashboard   = $("a[href*='#/priority-dashboard']");
    private SelenideElement inProgressContracts = $("a[href*='#/contracts?filter=active']");
    private SelenideElement executedContracts   = $("a[href*='contracts-executed?filter=executed']");
    private SelenideElement dashboard           = $("a[href*='#/dashboard']");
    private SelenideElement templates           = $("a[href*='#/templates']");
    private SelenideElement administration      = $("a[href*='#/admin/usermanagement']");
    private SelenideElement userGuide           = $("a[href='http://help.parleypro.com/articles']");
    private SelenideElement userIcon            = $("#page-menu-account");


    private static Logger logger = Logger.getLogger(LoginPage .class);

    public SideBar()
    {
        Assert.assertTrue(isInit());
    }

    private boolean isInit()
    {
        boolean hasLogo = $(".page-menu__item.page-menu__item_logo.state_active.state_active").isDisplayed();

        return (  hasLogo && priorityDashboard.isDisplayed() && inProgressContracts.isDisplayed() &&
                  executedContracts.isDisplayed() && dashboard.isDisplayed() && userGuide.isDisplayed() );
    }

    public void clickPriorityDashboard()
    {
        priorityDashboard.click();
    }

    public InProgressContractsPage clickInProgressContracts(boolean isBlank)
    {
        inProgressContracts.click();

        logger.info("In-progress contracts button was clicked");

        return new InProgressContractsPage(isBlank);
    }

    public ExecutedContractsPage clickExecutedContracts()
    {
        executedContracts.click();

        logger.info("Executed contracts button was clicked");

        return new ExecutedContractsPage();
    }

    public TemplatesPage clickTemplates(boolean isBlank)
    {
        templates.click();

        logger.info("Templates button was clicked");

        return new TemplatesPage(isBlank);
    }

    public AdministrationPage clickAdministration()
    {
        administration.click();

        logger.info("Administration button was clicked");

        return new AdministrationPage();
    }

    public LoginPage logout()
    {
        userIcon.click();
        $(".page-menu .dropdown-menu a").waitUntil(Condition.visible, 7_000).click();

        logger.info("Logout was clicked");

        return new LoginPage();
    }
}
