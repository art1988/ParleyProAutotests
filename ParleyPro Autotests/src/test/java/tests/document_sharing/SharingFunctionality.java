package tests.document_sharing;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ShareForm;
import io.qameta.allure.Description;
import model.User;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AuditTrail;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.administration.ManageUsers;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class SharingFunctionality
{
    private String host     = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private String uniqueEmail; // email that will be unique every time
    private Map<String, String> roles = new HashMap<>(3); // map user(key) to it's role(value)

    private static Logger logger = Logger.getLogger(SharingFunctionality.class);

    @Test(priority = 1)
    @Description("This test opens SHARE modal, adds users and checks that email were received. Changes roles for 2 users and disables one.")
    public void sharingFunctionality() throws InterruptedException
    {
        ShareForm shareForm = new OpenedContract().clickSHARE("Formatting");

        // Add first user [USER1] - GREG
        shareForm.addParticipant( Const.USER_GREG.getFirstName() ).clickSend();
        roles.put(Const.USER_GREG.getFirstName(), "Reviewer");

        logger.info("Waiting for 20 seconds to make sure that email has been delivered...");
        Thread.sleep(20_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Contract \"Contract DOCUMENT Sharing\": document review request"),
                "Email with subject: Contract \"Contract DOCUMENT Sharing\": document review request was not found !!!");

        logger.info("Assert that status was changed to REVIEW after sharing...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        // Make resend
        shareForm = new OpenedContract().clickSHARE("Formatting").resendInvite( Const.USER_GREG.getFirstName() ).clickSend();

        logger.info("Assert email send notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Email has been successfully sent."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Waiting for 20 seconds to make sure that email has been delivered...");
        Thread.sleep(20_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Contract \"Contract DOCUMENT Sharing\": document review request"),
                "Email with subject: Contract \"Contract DOCUMENT Sharing\": document review request was not found !!!");

        // Add second user [USER2] - MARY
        String emailMessage = "This is special message for Mary.";
        shareForm.addParticipant( Const.USER_MARY.getFirstName() ).changeRoleOfInternalUser( Const.USER_MARY.getFirstName() ).setLeadRole();
        roles.put(Const.USER_MARY.getFirstName(), "Lead");
        shareForm.clickAddNoteCheckbox();
        shareForm.setMessage(emailMessage);
        shareForm.clickSend();

        logger.info("Waiting for 20 seconds to make sure that email has been delivered...");
        Thread.sleep(20_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Role assignment: Lead"), "Email with subject: Role assignment: Lead was not found !!!");
        EmailChecker.assertEmailBodyText("*Message:* " + emailMessage);


        // Adding new unique user by email [USER3] - just email
        shareForm = new OpenedContract().clickSHARE("Formatting");
        uniqueEmail = "arthur.khasanov+" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime()) + "@parleypro.com";
        shareForm.addParticipant(uniqueEmail).clickSend();
        roles.put("unique", "Reviewer");


        logger.info("Waiting for 20 seconds to make sure that email has been delivered...");
        Thread.sleep(20_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Contract \"Contract DOCUMENT Sharing\": document review request"),
                "Email with subject: Contract \"Contract DOCUMENT Sharing\": document review request was not found !!!");

        Screenshoter.makeScreenshot();


        // 1
        logger.info("Hovering over all icons in document header...");
        $(".header-users .user").waitUntil(Condition.appear, 25_000); // wait until users icons will appear
        $$(".header-users .user").shouldHave(CollectionCondition.size(4)).shouldHave(CollectionCondition.textsInAnyOrder("a", "G", "M", "a"));
        hoverOverIcons(".header-users", roles);

        // 2
        logger.info("Hovering over all icons in contract header...");
        $(".contract-header-users__list .user").waitUntil(Condition.appear, 25_000); // wait until users icons will appear
        $$(".contract-header-users__list .user").shouldHave(CollectionCondition.size(4)).shouldHave(CollectionCondition.textsInAnyOrder("a", "G", "M", "a"));
        hoverOverIcons(".contract-header-users__list", roles);


        // Disable user with unique email
        shareForm = new OpenedContract().clickSHARE("Formatting").disableUser(uniqueEmail);

        logger.info("Swap roles between users [Mary] and [Greg]");
        shareForm.changeRoleOfInternalUser( Const.USER_MARY.getFirstName() ).setReviewerRole();
        roles.replace(Const.USER_MARY.getFirstName(), "Reviewer");
        Thread.sleep(500);

        shareForm.changeRoleOfInternalUser( Const.USER_GREG.getFirstName() ).setLeadRole();
        roles.replace(Const.USER_GREG.getFirstName(), "Lead");
        Thread.sleep(500);
        shareForm.clickDone();

        logger.info("Waiting for 20 seconds to make sure that emails were sent to users...");
        Thread.sleep(20_000);

        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Contract \"Contract DOCUMENT Sharing\": document review request"),
                "Email with subject: Contract \"Contract DOCUMENT Sharing\": document review request was not found !!!");
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Role assignment: Lead"),
                "Email with subject: Role assignment: Lead was not found !!!");

        // 3
        logger.info("Hovering over all icons in document header...");
        $$(".header-users .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("a", "G", "M"));
        hoverOverIcons(".header-users", roles);

        // 4
        logger.info("Hovering over all icons in contract header...");
        $$(".contract-header-users__list .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("a", "G", "M"));
        hoverOverIcons(".contract-header-users__list", roles);

        Screenshoter.makeScreenshot();

        logger.info("Check Audit trail events...");
        AuditTrail auditTrail = new OpenedContract().clickAuditTrail();
        Assert.assertEquals(Selenide.executeJavaScript("return $('.timeline-title').text()"),
                "Document user assignedDocument user removedDocument user assignedDocument user assignedDocument user assignedInvitation reminder sentDocument user assignedDocument uploadedContract created");
        auditTrail.clickOk();
    }

    /**
     * Helper method that allows to hover over user icons
     * @param cssClassName may be '.header-users' for document header or '.contract-header-users__list' for contract header
     */
    private void hoverOverIcons(String cssClassName, Map<String, String> roles) throws InterruptedException
    {
        for( int i = 0; i < $$(cssClassName + " .user").size(); i++ )
        {
            $$(cssClassName + " .user").get(i).hover();

            if( i == 0 )
            {
                Thread.sleep(1_000);
                continue; // first hover over all 4 icons - nothing to check
            }

            $(".spinner").waitUntil(Condition.disappear, 8_000);

            switch( $$(cssClassName + " .user").get(i).getText() )
            {
                case "G": // as Greg
                    $(".contract-user__name").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText(Const.USER_GREG.getFirstName() + " " + Const.USER_GREG.getLastName()));
                    $(".contract-user__status").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText(roles.get(Const.USER_GREG.getFirstName())));
                    break;

                case "M": // as Mary
                    $(".contract-user__name").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText(Const.USER_MARY.getFirstName() + " " + Const.USER_MARY.getLastName()));
                    $(".contract-user__status").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText(roles.get(Const.USER_MARY.getFirstName())));
                    break;

                case "A": // as unique, because email starts with 'a' symbol
                    $(".contract-user__name").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText(uniqueEmail));
                    $(".contract-user__status").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText(roles.get("unique")));
                    break;
            }
        }
    }

    @Test(priority = 2)
    @Description("After test clean up of unique user")
    public void removeUniqueUser()
    {
        ManageUsers manageUsers = new DashboardPage().getSideBar().clickAdministration().clickManageUsersTab();

        logger.info("Trying to delete unique user...");
        manageUsers.clickActionMenuByEmail(uniqueEmail).clickDelete(new User("", "", uniqueEmail, "")).clickDelete();

        logger.info("Making sure that it is not in the list anymore...");
        String allEmails = Selenide.executeJavaScript("return $('.usermanagement__userlist_content_row_email').text()");
        Assert.assertFalse(allEmails.contains(uniqueEmail));

        Screenshoter.makeScreenshot();
    }
}
