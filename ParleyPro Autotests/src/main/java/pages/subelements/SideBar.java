package pages.subelements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import constants.SideBarItems;
import org.apache.log4j.Logger;
import org.testng.Assert;
import pages.*;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;

/**
 * Left sidebar. Each page has it.
 */
public class SideBar
{
    private SelenideElement logo     = $(".page-menu__item.page-menu__item_logo.state_active.state_active");
    private SelenideElement userIcon = $("#page-menu-account");
    private Map<SideBarItems, String> sideBarItems = new EnumMap<>(SideBarItems.class); // map each item to it's locator

    private static Logger logger = Logger.getLogger(SideBar.class);

    /**
     * Constructs default sidebar with PRIORITY_DASHBOARD, IN_PROGRESS_CONTRACTS, EXECUTED_CONTRACTS,
     * DASHBOARD and USER_GUIDE items
     */
    public SideBar()
    {
        initSideBarItems();
        Assert.assertTrue(isInit());
    }

    /**
     * Use this constructor to set custom sidebar items. Different users
     * with different roles may have different set of icons.
     * @param items
     */
    public SideBar(SideBarItems[] items)
    {
        initSideBarItems();
        Assert.assertTrue( logo.isDisplayed() );
        Arrays.stream(items).forEach( item -> Assert.assertTrue( $(sideBarItems.get(item)).isDisplayed() ) );
    }

    private boolean isInit()
    {
        return (  logo.isDisplayed() &&
                  $(sideBarItems.get(SideBarItems.PRIORITY_DASHBOARD)).isDisplayed() &&
                  $(sideBarItems.get(SideBarItems.IN_PROGRESS_CONTRACTS)).isDisplayed() &&
                  $(sideBarItems.get(SideBarItems.EXECUTED_CONTRACTS)).isDisplayed() &&
                  $(sideBarItems.get(SideBarItems.DASHBOARD)).isDisplayed() &&
                  $(sideBarItems.get(SideBarItems.USER_GUIDE)).isDisplayed()
        );
    }

    private void initSideBarItems()
    {
        sideBarItems.put(SideBarItems.PRIORITY_DASHBOARD,    "a[href*='#/priority-dashboard']");
        sideBarItems.put(SideBarItems.IN_PROGRESS_CONTRACTS, "a[href*='#/contracts?filter=active']");
        sideBarItems.put(SideBarItems.EXECUTED_CONTRACTS,    "a[href*='contracts-executed?filter=executed']");
        sideBarItems.put(SideBarItems.DASHBOARD,             "a[href*='#/dashboard']");
        sideBarItems.put(SideBarItems.TEMPLATES,             "a[href*='#/templates']");
        sideBarItems.put(SideBarItems.ADMINISTRATION,        "a[href*='#/admin/usermanagement']");
        sideBarItems.put(SideBarItems.USER_GUIDE,            "a[href^='http://help.parleypro.com/']");
    }

    public void clickPriorityDashboard()
    {
        $(sideBarItems.get(SideBarItems.PRIORITY_DASHBOARD)).click();
    }

    public InProgressContractsPage clickInProgressContracts(boolean isBlank)
    {
        $(sideBarItems.get(SideBarItems.IN_PROGRESS_CONTRACTS)).click();

        logger.info("In-progress contracts button was clicked");

        return new InProgressContractsPage(isBlank);
    }

    public ExecutedContractsPage clickExecutedContracts()
    {
        $(sideBarItems.get(SideBarItems.EXECUTED_CONTRACTS)).click();

        logger.info("Executed contracts button was clicked");

        return new ExecutedContractsPage();
    }

    public TemplatesPage clickTemplates(boolean isBlank)
    {
        $(sideBarItems.get(SideBarItems.TEMPLATES)).click();

        logger.info("Templates button was clicked");

        return new TemplatesPage(isBlank);
    }

    public AdministrationPage clickAdministration()
    {
        $(sideBarItems.get(SideBarItems.ADMINISTRATION)).click();

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
