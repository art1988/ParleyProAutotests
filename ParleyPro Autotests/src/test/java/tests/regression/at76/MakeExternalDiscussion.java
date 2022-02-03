package tests.regression.at76;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AuditTrail;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class MakeExternalDiscussion
{
    private String host     = "pop.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private static Logger logger = Logger.getLogger(MakeExternalDiscussion.class);

    @Test(priority = 1)
    @Description("This test makes new external discussion for Paragraph 2")
    public void makeExternalDiscussion() throws InterruptedException
    {
        String paragraph = "Paragraph 2: Create comment here";

        new OpenedContract().hover(paragraph)
                            .clickAddComment()
                            .setComment("comment for external post")
                            .selectExternal()
                            .clickPost(paragraph, "CounterpartyAT").clickPostExternally();

        logger.info("Check notification...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("External discussion " + paragraph + " has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);
    }

    @Test(priority = 2)
    public void checkAuditTrailEventsAfterExternalDiscussion()
    {
        AuditTrail auditTrailPage = new OpenedContract().clickAuditTrail();

        Assert.assertEquals(Selenide.executeJavaScript("return $('.timeline-title').text()"),
                "Online negotiation startedCounterparty document uploadedContract created");

        Assert.assertEquals(Selenide.executeJavaScript("return $('.timeline-body').text()"),
                "Email sent to:arthur.khasanov+cpat@parleypro.com - NegotiatorDocuments: AT-14.docxDocument name: AT-14.docx");

        Screenshoter.makeScreenshot();

        auditTrailPage.clickOk();
    }

    @Test(priority = 3)
    @Description("This test checks that no other emails were sent to CCN")
    public void checkThatNoEmailsWereSent()
    {
        logger.info("Assert that CCN didn't received email with subject [qa-autotests] autotest_cn fn ln shared contract \"APLL: 50 emails\" with you...");

        try
        {
            logger.info("Waiting for 30 seconds...");
            Thread.sleep(30_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        Assert.assertFalse(EmailChecker.assertEmailBySubject(host, username, password, "[qa-autotests] autotest_cn fn ln shared contract \"APLL: 50 emails\" with you"),
                "Email with subject: [qa-autotests] autotest_cn fn ln shared contract \"APLL: 50 emails\" with you was found, but shouldn't !!!");
    }
}
