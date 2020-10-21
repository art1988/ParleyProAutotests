package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.open;

public class LoginToDashboard
{
    private static Logger logger = Logger.getLogger(LoginToDashboard.class);


    @BeforeSuite
    private void setup()
    {
        Configuration.proxyEnabled   = true;
        Configuration.fileDownload   = FileDownloadMode.PROXY;
        Configuration.startMaximized = true;
        //Configuration.headless = true; //headless off

        Const.DOWNLOAD_DIR.mkdirs();
        Const.SCREENSHOTS_DIR.mkdirs();

        Configuration.downloadsFolder = Const.DOWNLOAD_DIR.getAbsolutePath();
        Configuration.reportsFolder   = Const.SCREENSHOTS_DIR.getAbsolutePath();

        open(Const.QA_TENANT_URL);
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
