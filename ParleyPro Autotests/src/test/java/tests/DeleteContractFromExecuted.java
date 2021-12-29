package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DashboardPage;
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
    public void deleteContractFromExecuted(String contractName) throws InterruptedException
    {
        // Before deletion - refresh page, because previous opened modal forms may still be active preventing clicking by sidebar icons
        Selenide.refresh();

        Thread.sleep(3_000);

        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract(contractName)
                           .deleteContract(contractName)
                           .clickDelete();

        logger.info("Assert delete notification...");
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.exactText("Contract " + contractName + " has been deleted."));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void clearDownloadFolder() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
