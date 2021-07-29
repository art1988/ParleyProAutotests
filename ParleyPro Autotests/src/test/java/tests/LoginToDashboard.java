package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
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

@Listeners({ScreenShotOnFailListener.class})
public class LoginToDashboard extends LoginBase
{
    private final static int MAX_RETRY_COUNT = 5;
    private static Logger logger = Logger.getLogger(LoginToDashboard.class);

    @BeforeSuite
    private void setup()
    {
        Configuration.proxyEnabled   = true;
        Configuration.fileDownload   = FileDownloadMode.PROXY;
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
                open( getTenantUrl() );
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
        // headless mode: need to set window size for correct running
        //WebDriverRunner.getWebDriver().manage().window().setSize(new Dimension(1920, 1080));
    }

    @Test
    public void loginToDashboard()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());

        logger.info("Making sure that Dashboard was loaded correctly...");

        DashboardPage dashboardPage = loginPage.clickSignIn();

        dashboardPage.getSideBar().clickInProgressContracts(true);

        Screenshoter.makeScreenshot();
    }
}
