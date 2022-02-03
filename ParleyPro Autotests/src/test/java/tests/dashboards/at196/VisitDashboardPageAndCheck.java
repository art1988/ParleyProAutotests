package tests.dashboards.at196;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.ChartsPage;
import pages.DashboardPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


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

        logger.info("Making sure that there is no grey screen on page...");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('.dashboard__body').find(\".error-boundary\").length == 0"), "There is at least one grey screen for dashboard on page !!!");

        Screenshoter.makeScreenshot();

        logger.info("Click by EXECUTED CONTRACTS tab...");
        chartsPage = chartsPage.clickExecutedContractsTab();
        Thread.sleep(1_000);

        logger.info("Assert that tab was selected...");
        $$(".tab-menu__item").filterBy(Condition.exactText("EXECUTED CONTRACTS")).first().shouldHave(Condition.cssClass("selected_yes"));

        $(byText("Contract executed in the past 6 months")).shouldBe(Condition.visible);

        softAssert.assertTrue(Selenide.executeJavaScript("return $('.dashboard__body').find(\".error-boundary\").length == 0"), "There is at least one grey screen for dashboard on page !!!");

        softAssert.assertAll();

        Screenshoter.makeScreenshot();
    }
}
