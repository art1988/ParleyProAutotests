package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.Screenshoter;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;

/**
 * General test for deleting contracts from 'Executed contracts' page
 */

public class DeleteContractFromExecuted
{
    private static Logger logger = Logger.getLogger(DeleteContractFromExecuted.class);

    @Test(priority = 1)
    @Parameters("contractNameExecuted")
    public void deleteContractFromExecuted(String contractNameExecuted) throws InterruptedException
    {
        // Before deletion - refresh page, because previous opened modal forms may still be active preventing clicking by sidebar icons
        Selenide.refresh();

        Thread.sleep(3_000);

        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract(contractNameExecuted)
                           .deleteContract(contractNameExecuted)
                           .clickDelete();

        logger.info("Assert delete notification...");
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.exactText("Contract " + contractNameExecuted + " has been deleted."));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void clearDownloadFolder() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
