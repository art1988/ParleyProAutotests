package pages;

import com.codeborne.selenide.Condition;
import org.testng.Assert;
import pages.subelements.SideBar;

import static com.codeborne.selenide.Selenide.$;

public class DashboardPage
{
    private SideBar sideBar;


    public DashboardPage()
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        Assert.assertTrue(isInit());

        sideBar = new SideBar();
    }

    private boolean isInit()
    {
        boolean hasSidebar = $(".page-menu").waitUntil(Condition.visible, 10_000).isDisplayed();

        return hasSidebar;
    }

    public SideBar getSideBar()
    {
        return sideBar;
    }
}
