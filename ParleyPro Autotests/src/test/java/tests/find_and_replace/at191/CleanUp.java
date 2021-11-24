package tests.find_and_replace.at191;

import com.codeborne.selenide.Condition;
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
    public void deleteContractFromInProgress()
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false)
                                        .selectContract(Cache.getInstance().getCachedContractTitle());

        new OpenedContract().clickContractActionsMenu()
                            .clickDeleteContract()
                            .clickDelete();

        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text(" has been deleted."));
        $(".notification-stack").should(Condition.disappear);
    }
}
