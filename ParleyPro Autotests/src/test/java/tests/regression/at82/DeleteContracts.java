package tests.regression.at82;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;

import static com.codeborne.selenide.Selenide.$;


public class DeleteContracts
{
    @Test
    @Description("This test deletes both contracts with title '3Qcategory1' from in-progress and executed.")
    public void deleteContracts()
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("3Q");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract(true).clickDelete();
        $(".notification-stack").waitUntil(Condition.appear, 20_000).shouldHave(Condition.text("has been deleted."));

        new DashboardPage().getSideBar().clickExecutedContracts(false).selectContract("3Qcategory1");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract(true).clickDelete();
        $(".notification-stack").waitUntil(Condition.appear, 20_000).shouldHave(Condition.text("has been deleted."));
    }
}
