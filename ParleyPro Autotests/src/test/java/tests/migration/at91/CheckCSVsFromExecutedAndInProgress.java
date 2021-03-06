package tests.migration.at91;

import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.Cache;
import utils.LoginBase;
import utils.ScreenShotOnFailListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Listeners({ScreenShotOnFailListener.class})
public class CheckCSVsFromExecutedAndInProgress
{
    private static Logger logger = Logger.getLogger(CheckCSVsFromExecutedAndInProgress.class);

    @BeforeMethod
    public void skipMigrationOnRCAndProd()
    {
        if(LoginBase.isRc() || LoginBase.isProd())
        {
            logger.warn("Do not run Migration suite on RC/PROD ! Skipping !");
            throw new SkipException("Do not run Migration suite on RC/PROD ! Skipping !");
        }
    }

    @Test(priority = 1)
    public void goToExecutedContractsAndDownloadCSV() throws FileNotFoundException
    {
        if(Cache.getInstance().getCachedLoginBase().isProd()) return;

        new DashboardPage().getSideBar().clickExecutedContracts(false).clickDownloadContractData();

        File executedMetadataCSV = new File(Const.DOWNLOAD_DIR.getAbsolutePath(), "executed-metadata.csv");

        Assert.assertTrue(new WebDriverWait(WebDriverRunner.getWebDriver(), 20).until(d -> executedMetadataCSV.exists()),
                "executed-metadata.csv wasn't downloaded !");
        Assert.assertTrue(executedMetadataCSV.getName().endsWith(".csv"), "Looks like that extension is not csv !");

        try
        {
            logger.info("Comparing CVS's from Executed contracts page...");
            Assert.assertTrue(isCSVTheSame(executedMetadataCSV.toPath(), Const.CONTRACT_DATA_CSV_ORIGINAL_EXECUTED.toPath()),
                    "Contract data CSV from Executed contracts page is not the same as before !");
        }
        catch (IOException e)
        {
            logger.error("IOException: unable to read CSV file", e);
        }
    }

    @Test(priority = 2)
    public void goToInProgressContractsAndDownloadCSV() throws FileNotFoundException
    {
        if(Cache.getInstance().getCachedLoginBase().isProd()) return;

        new DashboardPage().getSideBar().clickInProgressContracts(false).clickDownloadContractData();

        File activeMetadataCSV = new File(Const.DOWNLOAD_DIR.getAbsolutePath(), "active-metadata.csv");

        Assert.assertTrue(new WebDriverWait(WebDriverRunner.getWebDriver(), 20).until(d -> activeMetadataCSV.exists()),
                "active-metadata.csv wasn't downloaded !");
        Assert.assertTrue(activeMetadataCSV.getName().endsWith(".csv"), "Looks like that extension is not csv !");

        try
        {
            logger.info("Comparing CVS's from In-progress contracts page...");
            Assert.assertTrue(isCSVTheSame(activeMetadataCSV.toPath(), Const.CONTRACT_DATA_CSV_ORIGINAL_ACTIVE.toPath()),
                    "Contract data CSV from In-progress contracts page is not the same as before !");
        }
        catch (IOException e)
        {
            logger.error("IOException: unable to read CSV file", e);
        }
    }

    private boolean isCSVTheSame(Path downloaded, Path original) throws IOException
    {
        try (InputStream downloadedCSV = Files.newInputStream(downloaded);
             InputStream originalCSV   = Files.newInputStream(original))
        {
            int data;

            // byte-to-byte comparing
            while ( (data = downloadedCSV.read()) != -1 )
            {
                if ( data != originalCSV.read() )
                {
                    return false;
                }
            }
        }

        return true;
    }
}
