package tests.regression.at110;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    @Test
    public void deleteContractFromInProgress()
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false)
                                        .selectContract("Track changes AT-110");

        new OpenedContract().clickContractActionsMenu()
                            .clickDeleteContract()
                            .clickDelete();

        $(".notification-stack").waitUntil(Condition.appear, 6_000).shouldHave(Condition.exactText("Contract Track changes AT-110 has been deleted."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);
    }
}
