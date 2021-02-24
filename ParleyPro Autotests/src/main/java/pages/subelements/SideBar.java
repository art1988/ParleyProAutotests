package pages.subelements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import constants.SideBarItems;
import org.apache.log4j.Logger;
import org.testng.Assert;
import pages.*;

import java.util.Arrays;

import static com.codeborne.selenide.Selenide.$;

/**
 * Left sidebar. Each page has it.
 */
public class SideBar
{
    private SelenideElement logo     = $(".page-menu__item.page-menu__item_logo.state_active.state_active");
    private SelenideElement userIcon = $("#page-menu-account");


    private static Logger logger = Logger.getLogger(LoginPage .class);

    public SideBar()
    {
        Assert.assertTrue(isInit());
    }

    /**
     * Use this constructor to set which sidebar items should be present on page because different users
     * with different roles may have different set of icons.
     * @param items
     */
    public SideBar(SideBarItems[] items)
    {
        Assert.assertTrue( logo.isDisplayed() );
        Arrays.stream(items).forEach( item -> Assert.assertTrue( $(item.getLocator()).isDisplayed() ) );
    }

    private boolean isInit()
    {
        return (  logo.isDisplayed() &&
                  $(SideBarItems.PRIORITY_DASHBOARD.getLocator()).isDisplayed() &&
                  $(SideBarItems.IN_PROGRESS_CONTRACTS.getLocator()).isDisplayed() &&
                  $(SideBarItems.EXECUTED_CONTRACTS.getLocator()).isDisplayed() &&
                  $(SideBarItems.DASHBOARD.getLocator()).isDisplayed() &&
                  $(SideBarItems.USER_GUIDE.getLocator()).isDisplayed()
        );
    }

    public void clickPriorityDashboard()
    {
        $(SideBarItems.PRIORITY_DASHBOARD.getLocator()).click();
    }

    public InProgressContractsPage clickInProgressContracts(boolean isBlank)
    {
        $(SideBarItems.IN_PROGRESS_CONTRACTS.getLocator()).click();

        logger.info("In-progress contracts button was clicked");

        return new InProgressContractsPage(isBlank);
    }

    public ExecutedContractsPage clickExecutedContracts()
    {
        $(SideBarItems.EXECUTED_CONTRACTS.getLocator()).click();

        logger.info("Executed contracts button was clicked");

        return new ExecutedContractsPage();
    }

    public TemplatesPage clickTemplates(boolean isBlank)
    {
        $(SideBarItems.TEMPLATES.getLocator()).click();

        logger.info("Templates button was clicked");

        return new TemplatesPage(isBlank);
    }

    public AdministrationPage clickAdministration()
    {
        $(SideBarItems.ADMINISTRATION.getLocator()).click();

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
