package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.proxy.SelenideProxyServer;
import constants.Const;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getSelenideProxy;

@Listeners({ScreenShotOnFailListener.class})
public class LoginToTrackChangesTenant extends LoginBase
{
    private final static int MAX_RETRY_COUNT = 5;
    private static Logger logger = Logger.getLogger(LoginToTrackChangesTenant.class);

    @BeforeSuite
    public void setup()
    {
        if( isPROD )
        {
            logger.info("This is PROD -> Disabling proxy for TrackChangesTenant on PROD...");

            logger.info("Stopping the previous running...");
            SelenideProxyServer proxy = getSelenideProxy();
            proxy.shutdown();

            logger.info("Disabling proxyEnabled...");
            Configuration.proxyEnabled = false;
            Configuration.fileDownload = FileDownloadMode.HTTPGET;
        }

        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10_000;

        System.setProperty( "chromeoptions.args", "--no-sandbox --disable-dev-shm-usage" );

        Const.DOWNLOAD_DIR.mkdirs();
        Const.SCREENSHOTS_DIR.mkdirs();

        Configuration.downloadsFolder = Const.DOWNLOAD_DIR.getAbsolutePath();
        Configuration.reportsFolder   = Const.SCREENSHOTS_DIR.getAbsolutePath();

        int retryCount = 0;
        while(true)
        {
            try
            {
                open( getTrackChangesUrl() );
                break;
            }
            catch(WebDriverException e)
            {
                if( retryCount > MAX_RETRY_COUNT )
                {
                    throw new RuntimeException("Too many retries...", e);
                }

                logger.warn("encountered exception : ", e);
                logger.warn("Trying again...");

                retryCount++;
                try
                {
                    Thread.sleep(2_000);
                }
                catch (InterruptedException interruptedException)
                {
                    interruptedException.printStackTrace();
                }
                continue;
            }
        }
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
