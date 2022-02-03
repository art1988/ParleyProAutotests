package tests.regression.at123;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class RemoveContracts
{
    @Test
    public void removeContracts()
    {
        InProgressContractsPage inProgressContractsPage = new DashboardPage().getSideBar().clickInProgressContracts(false);

        inProgressContractsPage.selectContract("CTR under Bartholomew Bronson");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").waitUntil(Condition.visible, 5_000);
        $(".notification-stack .notification__close").click();


        inProgressContractsPage.selectContract("CTR under Bartholomew Aaronson");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").waitUntil(Condition.visible, 5_000);
        $(".notification-stack .notification__close").click();


        inProgressContractsPage.selectContract("CTR under Aaron Aaronson");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").waitUntil(Condition.visible, 5_000);
        $(".notification-stack .notification__close").click();
    }
}
