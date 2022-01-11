package tests.dashboards.at195;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Issue;
import io.qameta.allure.Issues;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.Cache;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class VisitDashboardChartExamples
{
    private SoftAssert softAssert = new SoftAssert();
    private static Logger logger = Logger.getLogger(VisitDashboardChartExamples.class);


    @Test
    @Issues({
            @Issue("PAR-15275")
    })
    public void visitDashboardChartExamples() throws InterruptedException
    {
        logger.info("Opening /dashboard-chart-examples...");
        Selenide.open(Cache.getInstance().getCachedLoginBase().getMigrationUrl() + "#/dashboard-chart-examples");

        $(".spinner").should(Condition.disappear);
        Thread.sleep(3_000);

        logger.info("Making sure that there is no 'No data available' label on page...");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('span:contains(\"No data\")').length == 0"), "Page has 'No data available' label !!!");

        logger.info("Making sure that there is no grey screen on page...");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('.dashboard__body').find(\".error-boundary\").length == 0"), "There is at least one grey screen for dashboard on page !!!");

        Screenshoter.makeScreenshot();

        logger.info("Click by EXECUTED CONTRACTS tab...");
        $$(".tab-menu__item").filterBy(Condition.exactText("EXECUTED CONTRACTS")).first().click();
        Thread.sleep(1_000);

        logger.info("Assert that tab was selected...");
        $$(".tab-menu__item").filterBy(Condition.exactText("EXECUTED CONTRACTS")).first().shouldHave(Condition.cssClass("selected_yes"));

        $(byText("Contract executed in the past 6 months")).shouldBe(Condition.visible);

        softAssert.assertTrue(Selenide.executeJavaScript("return $('span:contains(\"No data\")').length == 0"), "Page has 'No data available' label !!!");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('.dashboard__body').find(\".error-boundary\").length == 0"), "There is at least one grey screen for dashboard on page !!!");

        softAssert.assertAll();

        Screenshoter.makeScreenshot();
    }
}
