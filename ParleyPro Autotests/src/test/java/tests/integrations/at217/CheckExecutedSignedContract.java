package tests.integrations.at217;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AuditTrail;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckExecutedSignedContract
{
    private static Logger logger = Logger.getLogger(CheckExecutedSignedContract.class);


    @Test
    @Description("This test verifies that signed pdf doc is in the list. Also checks some DocuSign event.")
    public void checkExecutedSignedContract() throws InterruptedException
    {
        Selenide.open( Cache.getInstance().getCachedLoginBase().getTenantUrl() );

        new DashboardPage().getSideBar().clickExecutedContracts(true);

        int i = 0;
        final int RETRIES = 15;
        while ( i < RETRIES )
        {
            Selenide.refresh();

            logger.info("Wait 10 sec...");
            Thread.sleep(10_000);

            logger.info("Checking whether contract became visible on Executed page...");
            if( $(byText("AT-217: DocuSign basics CTR")).isDisplayed() )
            {
                break;
            }

            logger.info("[retry #" + i++ + "] : Can't find contract... Wait one more time...");
        }

        new DashboardPage().getSideBar().clickExecutedContracts(false).selectContract("AT-217: DocuSign basics CTR");

        logger.info("Assert that signed pdf document is in the list...");
        $(".label.label_theme_purple").shouldBe(Condition.visible).click(); // expand doc by clicking purple label

        $("canvas").shouldBe(Condition.visible);
        $(byText("PROVIDED BY DOCUSIGN ONLINE SIGNING SERVICE")).shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();

        logger.info("Opening Audit trail and check DocuSign events...");
        AuditTrail auditTrail = new OpenedContract().clickAuditTrail();

        $$(".timeline-title").shouldHave(CollectionCondition.sizeGreaterThan(9));
        List<String> allEvents = $$(".timeline-title").stream().map(SelenideElement::text).collect(Collectors.toList());

        Assert.assertTrue(allEvents.contains("Docusign event: Opened"), "There is no such event as 'Docusign event: Opened' !!!");
        Assert.assertTrue(allEvents.contains("Docusign event: Viewed"), "There is no such event as 'Docusign event: Viewed' !!!");
        Assert.assertTrue(allEvents.contains("Docusign event: Signed"), "There is no such event as 'Docusign event: Signed' !!!");
        Assert.assertTrue(allEvents.contains("Document imported from Docusign"), "There is no such event as 'Document imported from Docusign' !!!");

        Screenshoter.makeScreenshot();

        auditTrail.clickOk();
    }
}
