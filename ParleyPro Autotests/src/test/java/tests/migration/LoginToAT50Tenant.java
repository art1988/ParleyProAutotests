package tests.migration;

import com.codeborne.selenide.*;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.*;

public class LoginToAT50Tenant
{
    private static Logger logger = Logger.getLogger(LoginToAT50Tenant.class);

    @BeforeSuite
    private void setup()
    {
        Configuration.proxyEnabled   = true;
        Configuration.fileDownload   = FileDownloadMode.PROXY;
        Configuration.startMaximized = true;

        Const.DOWNLOAD_DIR.mkdirs();
        Const.SCREENSHOTS_DIR.mkdirs();

        Configuration.downloadsFolder = Const.DOWNLOAD_DIR.getAbsolutePath();
        Configuration.reportsFolder   = Const.SCREENSHOTS_DIR.getAbsolutePath();

        open( Const.AT50_TENANT_URL );
    }

    @Test(priority = 1)
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

    @Test(priority = 2)
    @Description("This test checks that both contracts are in the list, and all icons are in place")
    public void checkContracts()
    {
        logger.info("Assert the first row of table...");
        $$(".contracts-list__table a").first().shouldHave(Condition.exactText("Second linked contract\nTest Counterparty, Inc. Parley Pro\ndraft\nDec 15, 2020 1:06 PM USD 100,000.00 Jan 2, 2026"));

        logger.info("Assert the second row of table...");
        $$(".contracts-list__table a").last().shouldHave(Condition.exactText("This is an In-Progress Contract_Online_Jan/02/2031\n Test Counterparty, Inc. Parley Pro\ndraft\nDec 15, 2020 1:01 PM USD 1,000,234.00 Jan 2, 2031"));

        logger.info("Assert that link icons are visible for both contracts...");
        $$(".contracts-list__labels-content").shouldHave(CollectionCondition.size(2));
        // get first icon
        Assert.assertEquals(Selenide.executeJavaScript("return window.getComputedStyle( document.querySelectorAll('.contracts-list__labels-content .glyphicon.glyphicon-link')[0], ':before').getPropertyValue('content')"), "\"\ue144\"");
        // get second icon
        Assert.assertEquals(Selenide.executeJavaScript("return window.getComputedStyle( document.querySelectorAll('.contracts-list__labels-content .glyphicon.glyphicon-link')[1], ':before').getPropertyValue('content')"), "\"\ue144\"");

        logger.info("Assert that both green circles of draft status are visible...");
        $$(".contract-status circle").shouldHave(CollectionCondition.size(2));


        logger.info("Hover over first link icon and check data...");
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
                     jsCode.append("$('.contracts-list__labels-content')[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        $(".spinner").waitUntil(Condition.disappear, 7_000);
        $(".rc-tooltip-content").waitUntil(Condition.visible, 7_000).shouldBe(Condition.visible);

    }
}
