package tests.migration.at91;

import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Listeners({ScreenShotOnFailListener.class})
public class GoToExecutedContractsAndDownloadCSV
{
    private File executedMetadataCSV;

    @Test
    public void goToExecutedContractsAndDownloadCSV() throws IOException
    {
        new DashboardPage().getSideBar().clickExecutedContracts(false).clickDownloadContractData();

        executedMetadataCSV = new File(Const.DOWNLOAD_DIR.getAbsolutePath(), "executed-metadata.csv");

        Assert.assertTrue(new WebDriverWait(WebDriverRunner.getWebDriver(), 20).until(d -> executedMetadataCSV.exists()),
                "executed-metadata.csv wasn't downloaded !");
        Assert.assertTrue(executedMetadataCSV.getName().endsWith(".csv"), "Looks like that extension is not csv !");

        Assert.assertTrue(csvIsTheSame());
    }

    private boolean csvIsTheSame() throws IOException
    {
        try (InputStream downloadedCSV = Files.newInputStream(executedMetadataCSV.toPath());
             InputStream originalCSV   = Files.newInputStream(Const.CONTRACT_DATA_CSV_ORIGINAL.toPath()))
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
