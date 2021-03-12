package tests.approval_workflow;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AdministrationPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.administration.Teams;
import pages.administration.Workflows;
import utils.ScreenShotOnFailListener;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ ScreenShotOnFailListener.class})
public class CleanUp
{
    private static Logger logger = Logger.getLogger(CleanUp.class);

    @Test(priority = 1)
    @Description("This test clean up download dir")
    public void cleanUpDownloadDir() throws IOException
    {
        // Clean download dir
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }

    @Test(priority = 2)
    @Description("This test delete contract: Approval workflow positive")
    public void deleteContract()
    {
        new DashboardPage().getSideBar().clickExecutedContracts(false).selectContract("Approval workflow positive");

        OpenedContract openedContract = new OpenedContract();

        openedContract.clickContractActionsMenu().clickDeleteContract().clickDelete();

        logger.info("Assert delete contract notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract Approval workflow positive has been deleted."));
    }
}
