package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

/**
 * General test for deleting contracts from 'In-progress contracts' page that takes contract name from cache
 */
@Listeners({ScreenShotOnFailListener.class})
public class DeleteContractFromInProgressViaCache
{
    @Test
    public void deleteContractFromInProgressViaCache() throws InterruptedException
    {
        // Before deletion - refresh page, because previous opened modal forms may still be active preventing clicking by sidebar icons
        Selenide.refresh();

        Thread.sleep(3_000);

        new DashboardPage().getSideBar()
                           .clickInProgressContracts(false)
                           .selectContract( Cache.getInstance().getCachedContractTitle() );

        new OpenedContract().clickContractActionsMenu()
                            .clickDeleteContract()
                            .clickDelete();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text(" has been deleted."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);
    }
}
