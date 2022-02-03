package tests.dashboards.at151;

import com.codeborne.selenide.CollectionCondition;
import io.qameta.allure.Description;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.PriorityDashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class ClickByPriorityDashboard
{
    @Test
    @Description("This simple test clicks by Priority Dashboard icon and checks that no grey screen happens.")
    public void clickByPriorityDashboard()
    {
        PriorityDashboardPage priorityDashboardPage = new DashboardPage().getSideBar().clickPriorityDashboard();

        $$(".priority-item").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(4));

        $$("button").shouldHave(CollectionCondition.size(2));

        Screenshoter.makeScreenshot();
    }
}
