package tests.integrations.at217;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import model.AuditTrailEvent;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AuditTrail;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.util.List;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
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
            if( $(withText("AT-217_DocuSign_basics_CTR")).isDisplayed() )
            {
                break;
            }

            logger.info("[retry #" + i++ + "] : Can't find contract... Wait one more time...");
        }

        new DashboardPage().getSideBar().clickExecutedContracts(false).selectContract("AT-217_DocuSign_basics_CTR");

        logger.info("Assert that signed pdf document is in the list...");
        $(".label.label_theme_purple").shouldBe(Condition.visible).click(); // expand doc by clicking purple label

        $("canvas").shouldBe(Condition.visible);
        $(byText("PROVIDED BY DOCUSIGN ONLINE SIGNING SERVICE")).shouldBe(Condition.visible);
        Screenshoter.makeScreenshot();

        logger.info("Filling post-execution fields...");
        ContractInfo contractInfo = new ContractInfo();
        contractInfo.setEffectiveDate();
        contractInfo.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract has been updated."));

        logger.info("Check that status become MANAGED...");
        $(".lifecycle__item.active").shouldBe(Condition.visible).shouldHave(Condition.exactText("MANAGED"));

        logger.info("Opening Audit trail and check DocuSign events...");
        AuditTrail auditTrail = new OpenedContract().clickAuditTrail();

        $$(".timeline-title").shouldHave(CollectionCondition.sizeGreaterThan(9));
        List<AuditTrailEvent> allEvents = auditTrail.collectAllEvents();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(allEvents.contains(new AuditTrailEvent("Docusign event: Opened")), "There is no such event as 'Docusign event: Opened' !!!");
        softAssert.assertTrue(allEvents.contains(new AuditTrailEvent("Docusign event: Viewed")), "There is no such event as 'Docusign event: Viewed' !!!");
        softAssert.assertTrue(allEvents.contains(new AuditTrailEvent("Docusign event: Signed")), "There is no such event as 'Docusign event: Signed' !!!");
        softAssert.assertTrue(allEvents.contains(new AuditTrailEvent("Document imported from Docusign")), "There is no such event as 'Document imported from Docusign' !!!");
        softAssert.assertTrue(allEvents.contains(new AuditTrailEvent("Contract moved into Manage stage")), "There is no such event as 'Contract moved into Manage stage' !!!");
        softAssert.assertAll();

        Screenshoter.makeScreenshot();

        auditTrail.clickOk();
    }
}
