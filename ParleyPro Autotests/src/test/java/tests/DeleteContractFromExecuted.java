package tests;

import com.codeborne.selenide.Condition;
import constants.Const;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.ExecutedContractsPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

/**
 * General test for deleting contracts from 'Executed contracts' page
 */
@Listeners({ScreenShotOnFailListener.class})
public class DeleteContractFromExecuted
{
    private static Logger logger = Logger.getLogger(DeleteContractFromExecuted.class);

    @Test(priority = 1)
    @Parameters("contractName")
    public void deleteContractFromExecuted(String contractName)
    {
        DashboardPage dashboardPage = new DashboardPage();

        ExecutedContractsPage executedContractsPage = dashboardPage.getSideBar().clickExecutedContracts();

        ContractInfo contractInfoPage = executedContractsPage.selectContract(contractName);

        forms.DeleteContract deleteContractPopup = contractInfoPage.deleteContract(contractName);

        deleteContractPopup.clickDelete();

        logger.info("Assert delete notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract " + contractName + " has been deleted."));

        $(".contracts__create").waitUntil(Condition.visible, 15_000).shouldHave(Condition.text("There are no executed contracts.\nYou can start a new executed  contract by clicking the button below"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void clearDownloadFolder() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
