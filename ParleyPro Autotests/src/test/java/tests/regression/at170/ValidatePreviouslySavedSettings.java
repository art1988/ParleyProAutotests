package tests.regression.at170;

import com.codeborne.selenide.Selenide;
import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Integrations;
import utils.Cache;
import utils.Screenshoter;


public class ValidatePreviouslySavedSettings
{
    private static Logger logger = Logger.getLogger(ValidatePreviouslySavedSettings.class);

    @Test
    public void validatePreviouslySavedSettings() throws InterruptedException
    {
        Integrations integrationsTab = new DashboardPage().getSideBar().clickAdministration().clickIntegrationsTab();

        logger.info("Scroll down to Google Drive...");
        Selenide.executeJavaScript("$('.integrations-google-drive')[0].scrollIntoView({});");
        Thread.sleep(1_000);

        logger.info("Checking saved settings for Google Drive...");
        Assert.assertEquals(integrationsTab.getGoogleDriveOwnerEmail(), Cache.getInstance().getCachedEmail(), "Drive Owner Email wasn't saved !!!");
        Assert.assertTrue(integrationsTab.getGoogleDrivePPUser().contains(Const.PREDEFINED_USER_CN_ROLE.getEmail()), "ParleyPro User wasn't saved !!!");
        Assert.assertEquals(integrationsTab.getGoogleDriveBackupFolder(), "/subdir/BackupFolderTEST", "Backup Folder wasn't saved !!!");
        Assert.assertEquals(integrationsTab.getGoogleDriveDDTemplate(), "/templates/DDTemplate/", "Document Directory Template wasn't saved !!!");

        Screenshoter.makeScreenshot();
    }
}
