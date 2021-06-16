package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import constants.Const;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.open;

@Listeners({ScreenShotOnFailListener.class})
public class LoginToTrackChangesTenant
{
    private static Logger logger = Logger.getLogger(LoginToTrackChangesTenant.class);

    @BeforeTest
    private void setup()
    {
        Configuration.proxyEnabled   = true;
        Configuration.fileDownload   = FileDownloadMode.PROXY;
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10_000;

        Const.DOWNLOAD_DIR.mkdirs();
        Const.SCREENSHOTS_DIR.mkdirs();

        Configuration.downloadsFolder = Const.DOWNLOAD_DIR.getAbsolutePath();
        Configuration.reportsFolder   = Const.SCREENSHOTS_DIR.getAbsolutePath();

        open( Const.TRACK_CHANGES_TENANT_URL );
    }

    @Test
    public void loginToTrackChangesTenant()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.EVHEN_TRACKCHANGES_USER.getEmail() );
        loginPage.setPassword( Const.EVHEN_TRACKCHANGES_USER.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");

        DashboardPage dashboardPage = loginPage.clickSignIn();
        dashboardPage.getSideBar().clickInProgressContracts(false);

        Screenshoter.makeScreenshot();
    }
}
