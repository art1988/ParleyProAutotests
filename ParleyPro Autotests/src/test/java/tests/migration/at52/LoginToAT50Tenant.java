package tests.migration.at52;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import constants.Const;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.open;

@Listeners({ScreenShotOnFailListener.class})
public class LoginToAT50Tenant
{
    private static Logger logger = Logger.getLogger(LoginToAT50Tenant.class);

    @BeforeSuite
    private void setup()
    {
        Configuration.proxyEnabled   = true;
        Configuration.fileDownload   = FileDownloadMode.PROXY;
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 10_000;

        WebDriverManager.chromedriver().driverVersion("91").setup();
        System.setProperty("chromeoptions.args", "\"--no-sandbox\",\"--disable-dev-shm-usage\",\"--remote-debugging-port=9222\"");
        Configuration.headless = true;

        Const.DOWNLOAD_DIR.mkdirs();
        Const.SCREENSHOTS_DIR.mkdirs();

        Configuration.downloadsFolder = Const.DOWNLOAD_DIR.getAbsolutePath();
        Configuration.reportsFolder   = Const.SCREENSHOTS_DIR.getAbsolutePath();

        open( Const.AT50_TENANT_URL );
    }

    @Test()
    public void loginToAT50Tenant()
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.EVHEN_AT50_USER.getEmail() );
        loginPage.setPassword( Const.EVHEN_AT50_USER.getPassword() );

        logger.info("Making sure that Dashboard was loaded correctly...");

        DashboardPage dashboardPage = loginPage.clickSignIn(true).selectTenant("at50");
        dashboardPage.getSideBar().clickInProgressContracts(false);

        Screenshoter.makeScreenshot();
    }
}
