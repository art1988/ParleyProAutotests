package tests.regression.at150;

import com.codeborne.selenide.Condition;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;


public class CleanUp
{
    @Test
    public void deleteTwoExecutedContract()
    {
        new DashboardPage().getSideBar().clickExecutedContracts(false).selectContractWithoutUploadedDoc("contract1");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Contract contract1 has been deleted"));

        new DashboardPage().getSideBar().clickExecutedContracts(false).selectContractWithoutUploadedDoc("contract2");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Contract contract2 has been deleted"));

        Screenshoter.makeScreenshot();
    }
}
