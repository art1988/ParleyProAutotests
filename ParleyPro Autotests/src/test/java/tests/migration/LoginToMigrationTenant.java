package tests.migration;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.Cache;
import utils.LoginBase;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.FileDownloadMode.HTTPGET;
import static com.codeborne.selenide.Selenide.open;

@Listeners({ScreenShotOnFailListener.class})
public class LoginToMigrationTenant
{
    private final static int MAX_RETRY_COUNT = 5;
    private static Logger logger = Logger.getLogger(LoginToMigrationTenant.class);

    @Test(priority = 1)
    @Parameters("enableProxy")
    public void setup(boolean enableProxy)
    {
        LoginBase loginBase = new LoginBase();

        label:
        if(enableProxy == true)
        {
            if( LoginBase.isProd() )
            {
                logger.info("Proxy OFF");
                Configuration.proxyEnabled = false;
                Configuration.fileDownload = HTTPGET;
                break label;
            }

            logger.info("Proxy ON");
            Configuration.proxyEnabled   = true;
            Configuration.fileDownload   = FileDownloadMode.PROXY;
        }
        else
        {
            logger.info("Proxy OFF");
            Configuration.proxyEnabled = false;
            Configuration.fileDownload = HTTPGET;
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
                open( loginBase.getMigrationUrl() );
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

        Cache.getInstance().setLoginBaseInstance(loginBase);
    }

    @Test(priority = 2)
    @Description("Logins to at50.parleypro as yevhen.uvin+at50@parleypro.com")
    public void loginToMigrationTenant()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.EVHEN_AT50_USER.getEmail() );
        loginPage.setPassword( Const.EVHEN_AT50_USER.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");

        DashboardPage dashboardPage = loginPage.clickSignIn();
        dashboardPage.getSideBar().clickInProgressContracts(false);

        Screenshoter.makeScreenshot();
    }
}
