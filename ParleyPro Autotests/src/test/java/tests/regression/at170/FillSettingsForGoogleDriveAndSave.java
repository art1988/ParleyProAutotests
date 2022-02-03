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

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class FillSettingsForGoogleDriveAndSave
{
    private static Logger logger = Logger.getLogger(FillSettingsForGoogleDriveAndSave.class);

    @Test(priority = 1)
    public void fillSettingsForGoogleDriveAndSave()
    {
        Integrations integrationsTab = new DashboardPage().getSideBar().clickAdministration().clickIntegrationsTab();

        logger.info("Scroll down to Google Drive...");
        Selenide.executeJavaScript("$('.integrations-google-drive')[0].scrollIntoView({});");

        String email = "junk_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime()) + "@junk.com";
        Cache.getInstance().setEmail(email);
        integrationsTab.setGoogleDriveOwnerEmail(email);
        integrationsTab.setGoogleDrivePPUser(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        integrationsTab.setGoogleDriveBackupFolder("/subdir/BackupFolderTEST");
        integrationsTab.setGoogleDriveDDTemplate("/templates/DDTemplate");
        integrationsTab.googleDriveSaveSettings();

        Assert.assertTrue(Selenide.executeJavaScript("return $('.integrations-google-drive button').is(':disabled')"),
                "Looks like that SAVE button is still enabled, but shouldn't !!!");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void refreshPage()
    {
        logger.info("Refreshing the page...");

        Selenide.refresh();
    }
}
