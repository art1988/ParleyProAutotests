package tests.document_sharing;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.ShareForm;
import io.qameta.allure.Description;
import model.User;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.administration.ManageUsers;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AddingOfNewUsersViaShareModal
{
    private String host     = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private String uniqueEmailUser1;
    private String uniqueEmailUser2;

    private static Logger logger = Logger.getLogger(AddingOfNewUsersViaShareModal.class);

    @Test(priority = 1)
    @Description("This test opens share modal, adds 2 unique users by email")
    public void addingOfNewUsersViaShareModal() throws InterruptedException
    {
        ShareForm shareForm = new OpenedContract().clickSHARE("Formatting");

        uniqueEmailUser1 = "arthur.khasanov+user1_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime()) + "@parleypro.com";
        shareForm.addParticipant(uniqueEmailUser1);

        uniqueEmailUser2 = "arthur.khasanov+user2_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime()) + "@parleypro.com";
        shareForm.addParticipant(uniqueEmailUser2);

        shareForm.changeRoleOfInternalUser(uniqueEmailUser1).setLeadRole(); // Set Lead role for uniqueUser1

        /* This assertion was commented because of PAR-13448
        logger.info("Asserting that Lead role is disabled for User2...");
        boolean noRolesDropdownForUser2 = Selenide.executeJavaScript("return $('.manage-users-user__fullname:contains(\"" + uniqueEmailUser2 + "\")').parent().parent().next().find(\"button\").length === 0");
        Assert.assertTrue(noRolesDropdownForUser2, "Because only one Lead is acceptable");
         */

        Screenshoter.makeScreenshot();

        shareForm.clickSend();

        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);

        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Role assignment: Lead"), "Email with subject: Role assignment: Lead was not found !!!");
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, "Contract \"Contract DOCUMENT Sharing\": document review request"), "Email with subject: Contract \"Contract DOCUMENT Sharing\": document review request was not found !!!");

        logger.info("Check user icons in document header...");
        $(".header-users .user").waitUntil(Condition.appear, 25_000); // wait until users icons will appear
        $$(".header-users .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("a", "a", "a"));
        hoverOverIcons(".header-users");

        logger.info("Check user icons in contract header...");
        $(".contract-header-users__list .user").waitUntil(Condition.appear, 25_000); // wait until users icons will appear
        $$(".contract-header-users__list .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.textsInAnyOrder("a", "a", "a"));
        hoverOverIcons(".contract-header-users__list");

        Screenshoter.makeScreenshot();
    }

    /**
     * Helper method that allows to hover over user icons
     * @param cssClassName may be '.header-users' for document header or '.contract-header-users__list' for contract header
     */
    private void hoverOverIcons(String cssClassName) throws InterruptedException
    {
        for( int i = 0; i < $$(cssClassName + " .user").size(); i++ )
        {
            $$(cssClassName + " .user").get(i).hover();

            if( i == 0 )
            {
                Thread.sleep(1_000);
                continue; // first hover over all user icons - nothing to check
            }

            $(".rc-tooltip-inner .spinner").waitUntil(Condition.disappear, 8_000);

            String userRole = $(".contract-user__status").waitUntil(Condition.visible, 5_000).getText(),
                   userName = $(".contract-user__name").waitUntil(Condition.visible, 5_000).getText();

            // Since we don't know the order of icons in header, we check that for Lead it should be uniqueEmailUser1...
            if( userRole.equals("LEAD") )
            {
                Assert.assertEquals(userName, uniqueEmailUser1);
            }
            else if( userRole.equals("REVIEWER") ) // ...and for Reviewer it should be uniqueEmailUser2
            {
                Assert.assertEquals(userName, uniqueEmailUser2);
            }
            else
            {
                Assert.fail("Users doesn't match their roles !!!");
            }
        }
    }

    @Test(priority = 2)
    @Description("Clean up of unique users that were added before")
    public void deleteUsers()
    {
        ManageUsers manageUsers = new DashboardPage().getSideBar().clickAdministration().clickManageUsersTab();

        logger.info("Trying to delete unique users...");
        manageUsers.clickActionMenuByEmail(uniqueEmailUser1).clickDelete(new User("", "", uniqueEmailUser1, "")).clickDelete();
        manageUsers.clickActionMenuByEmail(uniqueEmailUser2).clickDelete(new User("", "", uniqueEmailUser2, "")).clickDelete();

        logger.info("Making sure that it is not in the list anymore...");
        String allEmails = Selenide.executeJavaScript("return $('.usermanagement__userlist_content_row_email').text()");
        Assert.assertFalse( allEmails.contains(uniqueEmailUser1) & allEmails.contains(uniqueEmailUser2) );

        Screenshoter.makeScreenshot();
    }
}
