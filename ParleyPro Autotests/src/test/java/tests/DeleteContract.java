package tests;

import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.ExecutedContractsPage;
import utils.Screenshoter;

import java.io.IOException;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;

public class DeleteContract
{
    private static Logger logger = Logger.getLogger(DeleteContract.class);

    @Test(priority = 0)
    public void deleteContract()
    {
        DashboardPage dashboardPage = new DashboardPage();

        ExecutedContractsPage executedContractsPage = dashboardPage.getSideBar().clickExecutedContracts();

        ContractInfo contractInfoPage = executedContractsPage.selectContract("Contract lifecycle autotest");

        forms.DeleteContract deleteContractPopup = contractInfoPage.deleteContract("Contract lifecycle autotest");

        deleteContractPopup.clickDelete();

        logger.info("Assert delete notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract Contract lifecycle autotest has been deleted."));

        $(".contracts__create").waitUntil(Condition.visible, 15_000).shouldHave(Condition.text("There are no executed contracts.\nYou can start a new executed  contract by clicking the button below"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 1)
    public void clearDownloadFolder() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
