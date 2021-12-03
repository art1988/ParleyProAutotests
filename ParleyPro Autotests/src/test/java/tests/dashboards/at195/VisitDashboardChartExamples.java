package tests.dashboards.at195;

import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.Cache;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class VisitDashboardChartExamples
{
    private static Logger logger = Logger.getLogger(VisitDashboardChartExamples.class);


    @Test
    public void visitDashboardChartExamples() throws InterruptedException
    {
        logger.info("Opening /dashboard-chart-examples...");

        Selenide.open(Cache.getInstance().getCachedLoginBase().getMigrationUrl() + "#/dashboard-chart-examples");

        Thread.sleep(5_000);
    }
}
