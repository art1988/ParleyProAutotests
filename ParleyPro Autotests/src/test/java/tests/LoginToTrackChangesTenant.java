package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.proxy.SelenideProxyServer;
import constants.Const;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.LoginBase;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getSelenideProxy;

@Listeners({ScreenShotOnFailListener.class})
public class LoginToTrackChangesTenant
{
    private final static int MAX_RETRY_COUNT = 5;
    private static Logger logger = Logger.getLogger(LoginToTrackChangesTenant.class);

    @Test(priority = 1)
    public void setup()
    {
        LoginBase loginBase = new LoginBase();

        if( LoginBase.isProd() )
        {
            logger.info("This is PROD -> Disabling proxy for TrackChangesTenant on PROD...");

            SelenideProxyServer proxy = getSelenideProxy();
            if( proxy != null && proxy.isStarted() )
            {
                logger.info("Stopping the previous running proxy...");
                proxy.shutdown();

                logger.info("Stopping of browser...");
                WebDriverRunner.getWebDriver().quit();
            }

            logger.info("Disabling proxyEnabled setting...");
            Configuration.proxyEnabled = false;
            Configuration.fileDownload = FileDownloadMode.HTTPGET;
        }

        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 40_000;

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
                open( loginBase.getTrackChangesUrl() );
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

    @Test(priority = 2)
    public void loginToTrackChangesTenant()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.TRACKCHANGES_USER.getEmail() );
        loginPage.setPassword( Const.TRACKCHANGES_USER.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");

        DashboardPage dashboardPage = loginPage.clickSignIn();
        dashboardPage.getSideBar().clickInProgressContracts(false);
    }
}
