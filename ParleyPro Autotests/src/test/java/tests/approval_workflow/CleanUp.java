package tests.approval_workflow;

import com.codeborne.selenide.Condition;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    private static Logger logger = Logger.getLogger(CleanUp.class);

    @Test(priority = 1)
    @Description("This test clean up download dir")
    public void cleanUpDownloadDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }

    @Test(priority = 2)
    @Description("Delete the following contracts: Approval workflow negative case [wrong category], Approval workflow negative case [wrong value range], Approval workflow positive")
    public void deleteContracts()
    {
        SideBar sideBar = new DashboardPage().getSideBar();

        logger.info("Delete 'Approval workflow positive' contract from executed...");
        sideBar.clickExecutedContracts(false).selectContract("Approval workflow positive");

        OpenedContract openedContract = new OpenedContract();
        openedContract.clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Contract Approval workflow positive has been deleted."));

        logger.info("Delete 'Approval workflow negative case [wrong value range]' contract from in-progress...");
        sideBar.clickInProgressContracts(false).selectContract("[wrong value range]");
        openedContract = new OpenedContract();
        openedContract.clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Contract Approval workflow negative case [wrong value range] has been deleted."));

        logger.info("Delete 'Approval workflow negative case [wrong category]' contract from in-progress...");
        sideBar.clickInProgressContracts(false).selectContract("[wrong category]");
        openedContract = new OpenedContract();
        openedContract.clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Contract Approval workflow negative case [wrong category] has been deleted."));
    }
}
