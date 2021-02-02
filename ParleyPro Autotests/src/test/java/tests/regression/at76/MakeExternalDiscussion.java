package tests.regression.at76;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AuditTrail;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class MakeExternalDiscussion
{
    private static Logger logger = Logger.getLogger(MakeExternalDiscussion.class);

    @Test(priority = 1)
    public void makeExternalDiscussion() throws InterruptedException
    {
        String paragraph = "Paragraph 2: Create comment here";

        CKEditorActive ckEditorActive = new OpenedContract().hover(paragraph).clickAddComment();

        ckEditorActive.setComment("comment for external post");
        ckEditorActive.selectExternal();
        ckEditorActive.clickPost(paragraph, "CounterpartyAT").clickPostExternally();

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
                "Email sent to:arthur.khasanov+cpat@parleypro.com - NegotiatorDocuments:AT-14.docxDocument name: AT-14.docx");

        Screenshoter.makeScreenshot();

        auditTrailPage.clickOk();
    }
}
