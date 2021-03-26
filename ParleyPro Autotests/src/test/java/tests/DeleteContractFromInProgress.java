package tests;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

/**
 * General test for deleting contracts from 'In-progress contracts' page
 */
@Listeners({ScreenShotOnFailListener.class})
public class DeleteContractFromInProgress
{
    private static Logger logger = Logger.getLogger(DeleteContractFromExecuted.class);

    @Test
    @Parameters("contractName")
    public void deleteContractFromInProgress(String contractName)
    {
        InProgressContractsPage inProgressContractsPage = new DashboardPage().getSideBar().clickInProgressContracts(false);
        inProgressContractsPage.selectContract(contractName);

        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();

        logger.info("Assert delete notification...");
        logger.info("Waiting until notification will be visible [up to 1 minute]...");
        $(".notification-stack").waitUntil(Condition.visible, 60_000).shouldHave(Condition.exactText("Contract " + contractName + " has been deleted."));

        logger.info("Assert that In-progress contracts page has blank page with no contracts...");
        inProgressContractsPage = new InProgressContractsPage(true);

        Screenshoter.makeScreenshot();
    }
}
