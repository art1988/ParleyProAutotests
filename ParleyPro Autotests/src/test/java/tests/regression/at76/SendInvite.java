package tests.regression.at76;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AuditTrail;
import pages.OpenedContract;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class SendInvite
{
    private String host     = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private static Logger logger = Logger.getLogger(SendInvite.class);

    @Test(priority = 1)
    public void sendInvite() throws InterruptedException
    {
        new OpenedContract().clickSendInvite().clickNext(false).clickStart();

        Screenshoter.makeScreenshot();

        logger.info("Check notification...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000)
                .shouldHave(Condition.exactText("Documents have been shared with the Counterparty.The notification email was sent to arthur.khasanov+cpat@parleypro.com."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Waiting for 15 seconds to make sure that email has been delivered...");
        Thread.sleep(15_000);

        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "[qa-autotests] autotest_cn fn ln shared contract \"APLL: 50 emails\" with you"),
                "Email with subject: [qa-autotests] autotest_cn fn ln shared contract \"APLL: 50 emails\" with you was not found !!!");

        logger.info("Assert that there is no more SEND INVITE button...");
        $("._button.scheme_blue.size_tier2").should(Condition.disappear);
    }

    @Test(priority = 2)
    public void checkAuditTrailEvents()
    {
        AuditTrail auditTrailPage = new OpenedContract().clickAuditTrail();

        Assert.assertEquals(Selenide.executeJavaScript("return $('.timeline-title').text()"),
                "Online negotiation startedCounterparty document uploadedContract created");

        Assert.assertEquals(Selenide.executeJavaScript("return $('.timeline-body').text()"),
                "Email sent to:arthur.khasanov+cpat@parleypro.com - NegotiatorDocuments:AT-14.docxDocument name: AT-14.docx");

        Screenshoter.makeScreenshot();

        auditTrailPage.clickOk();
    }
}
