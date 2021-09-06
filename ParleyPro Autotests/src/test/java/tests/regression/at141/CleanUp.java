package tests.regression.at141;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    @Test
    public void cleanUp()
    {
        Selenide.refresh();

        new DashboardPage().getSideBar().clickInProgressContracts(false)
                                        .selectContract( Cache.getInstance().getCachedContractTitle() );

        new OpenedContract().clickContractActionsMenu()
                            .clickDeleteContract()
                            .clickDelete();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text(" has been deleted."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);
    }
}
