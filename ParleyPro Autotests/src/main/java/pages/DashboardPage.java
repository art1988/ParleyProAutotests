package pages;

import com.codeborne.selenide.Condition;
import constants.SideBarItems;
import org.testng.Assert;
import pages.subelements.SideBar;

import static com.codeborne.selenide.Selenide.$;

public class DashboardPage
{
    private SideBar sideBar;


    public DashboardPage()
    {
        $(".spinner").should(Condition.disappear);

        Assert.assertTrue(isInit());

        sideBar = new SideBar();
    }

    /**
     * Use this constructor to verify only certain icons of sidebar because different users
     * with different roles may have different set of icons.
     * @param sideBarItems
     */
    public DashboardPage(SideBarItems[] sideBarItems)
    {
        $(".spinner").waitUntil(Condition.disappear, 35_000);

        Assert.assertTrue(isInit());

        sideBar = new SideBar(sideBarItems);
    }

    private boolean isInit()
    {
        boolean hasSidebar = $(".page-menu").waitUntil(Condition.visible, 35_000).isDisplayed();

        return hasSidebar;
    }

    public SideBar getSideBar()
    {
        return sideBar;
    }
}
