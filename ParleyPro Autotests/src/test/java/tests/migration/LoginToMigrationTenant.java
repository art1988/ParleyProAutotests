package tests.migration;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import tests.LoginBase;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.open;

@Listeners({ScreenShotOnFailListener.class})
public class LoginToMigrationTenant
{
    private final static int MAX_RETRY_COUNT = 5;
    private static Logger logger = Logger.getLogger(LoginToMigrationTenant.class);

    @BeforeSuite
    private void setup()
    {
        Configuration.proxyEnabled   = true;
        Configuration.fileDownload   = FileDownloadMode.PROXY;
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 40_000;

        System.setProperty( "chromeoptions.args", "--no-sandbox --disable-dev-shm-usage" );

        Const.DOWNLOAD_DIR.mkdirs();
        Const.SCREENSHOTS_DIR.mkdirs();

        Configuration.downloadsFolder = Const.DOWNLOAD_DIR.getAbsolutePath();
        Configuration.reportsFolder   = Const.SCREENSHOTS_DIR.getAbsolutePath();

        LoginBase loginBase = new LoginBase();

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
    }

    @Test()
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
