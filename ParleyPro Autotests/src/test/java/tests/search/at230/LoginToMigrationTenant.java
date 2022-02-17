package tests.search.at230;


import com.codeborne.selenide.Configuration;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.Cache;
import utils.LoginBase;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.open;

public class LoginToMigrationTenant
{
    private final static int MAX_RETRY_COUNT = 5;
    private static Logger logger = Logger.getLogger(LoginToMigrationTenant.class);

    @BeforeTest
    public void setup()
    {
        LoginBase loginBase = new LoginBase();

        if(LoginBase.isRc() || LoginBase.isProd())
        {
            logger.warn("Do not run AT-230 on RC or PROD ! Skipping !");
            throw new SkipException("Do not run AT-230 on RC or PROD...");
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
