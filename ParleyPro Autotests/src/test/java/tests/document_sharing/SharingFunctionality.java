package tests.document_sharing;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ShareForm;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class SharingFunctionality
{
    private String host     = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private static Logger logger = Logger.getLogger(SharingFunctionality.class);

    @Test
    @Description("This test opens SHARE modal, adds users and checks that email were received")
    public void sharingFunctionality() throws InterruptedException
    {
        ShareForm shareForm = new OpenedContract().clickSHARE("Formatting");

        // Add first user
        shareForm.addParticipant( Const.USER_GREG.getFirstName() ).clickSend();

        logger.info("Waiting for 15 seconds to make sure that email has been delivered...");
        Thread.sleep(15_000);

        EmailChecker.assertEmailBySubject(host, username, password, "Contract \"Contract DOCUMENT Sharing\": document review request");

        logger.info("Assert that status was changed to REVIEW after sharing...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        // Make resend
        shareForm = new OpenedContract().clickSHARE("Formatting").resendInvite( Const.USER_GREG.getFirstName() ).clickSend();

        logger.info("Assert email send notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Email has been successfully sent."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        // TODO: add email check after fixing of PAR-13145

        // Add second user
        shareForm.addParticipant( Const.USER_MARY.getFirstName() ).changeRoleOfInternalUser( Const.USER_MARY.getFirstName() ).setLeadRole();
        shareForm.clickAddNoteCheckbox();
        shareForm.setMessage("This is special message for Mary.");
        shareForm.clickSend();

        logger.info("Waiting for 15 seconds to make sure that email has been delivered...");
        Thread.sleep(15_000);

        EmailChecker.assertEmailBySubject(host, username, password, "Role assignment: Lead");
        EmailChecker.assertEmailBodyText("*Message:* This is special message for Mary.");
    }
}
