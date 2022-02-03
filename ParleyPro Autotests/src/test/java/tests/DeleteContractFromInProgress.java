package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

/**
 * General test for deleting contracts from 'In-progress contracts' page that takes contract name from parameter
 */
@Listeners({ScreenShotOnFailListener.class})
public class DeleteContractFromInProgress
{
    private static Logger logger = Logger.getLogger(DeleteContractFromInProgress.class);

    @Test
    @Parameters("contractName")
    public void deleteContractFromInProgress(String contractName) throws InterruptedException
    {
        // Before deletion - refresh page, because previous opened modal forms may still be active preventing clicking by sidebar icons
        Selenide.refresh();

        Thread.sleep(3_000);

        new DashboardPage().getSideBar()
                           .clickInProgressContracts(false)
                           .selectContract(contractName);

        new OpenedContract().clickContractActionsMenu()
                            .clickDeleteContract()
                            .clickDelete();

        logger.info("Assert delete notification...");
        logger.info("Waiting until notification will be visible [up to 5 minutes]...");
        $(".notification-stack").waitUntil(Condition.visible, 60_000 * 5)
                                          .shouldHave(Condition.exactText("Contract " + contractName + " has been deleted."));

        logger.info("Assert that In-progress contracts page has blank page with no contracts...");
        new DashboardPage().getSideBar().clickInProgressContracts(true);

        Screenshoter.makeScreenshot();
    }
}
