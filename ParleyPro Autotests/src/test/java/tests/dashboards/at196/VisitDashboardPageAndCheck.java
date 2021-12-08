package tests.dashboards.at196;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.ChartsPage;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class VisitDashboardPageAndCheck
{
    private SoftAssert softAssert = new SoftAssert();
    private static Logger logger = Logger.getLogger(VisitDashboardPageAndCheck.class);

    @Test
    public void visitDashboardPageAndCheck() throws InterruptedException
    {
        ChartsPage chartsPage = new DashboardPage().getSideBar().clickDashboard().clickInProgressContractsTab();

        $(".spinner").should(Condition.disappear);
        Thread.sleep(3_000);

        logger.info("Making sure that there is no 'No data available' label on page...");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('span:contains(\"No data\")').length == 0"), "Page has 'No data available' label !!!");

        softAssert.assertAll();
    }
}
