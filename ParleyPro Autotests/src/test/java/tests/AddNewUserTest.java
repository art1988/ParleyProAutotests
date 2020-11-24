package tests;

import com.codeborne.selenide.Selenide;
import forms.AddNewUser;
import io.qameta.allure.Description;
import model.User;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.ManageUsers;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import java.util.Arrays;
import java.util.Properties;

@Listeners({ ScreenShotOnFailListener.class})
public class AddNewUserTest
{
    // User that will be added
    private User newUser = new User("AT User first_name", "last_name AT", "arthur.khasanov+at_user@parleypro.com", "");

    private static Logger logger = Logger.getLogger(AddNewUserTest.class);

    @Test(priority = 1)
    @Description("This test goes to administration, creates new user and verifies that is was saved")
    public void addNewUser()
    {
        DashboardPage dashboardPage = new DashboardPage();

        ManageUsers manageUsersTab = dashboardPage.getSideBar().clickAdministration().clickManageUsersTab();

        AddNewUser addNewUserForm = manageUsersTab.clickAddNewUser();

        addNewUserForm.setFirstName(newUser.getFirstName());
        addNewUserForm.setLastName(newUser.getLastName());
        addNewUserForm.setEmail(newUser.getEmail());
        addNewUserForm.clickAddRole();
        addNewUserForm.setRole("Requester");
        addNewUserForm.clickAddUser();

        // Re-click tabs to refresh page
        dashboardPage.getSideBar().clickAdministration().clickTeamsTab();
        dashboardPage.getSideBar().clickAdministration().clickManageUsersTab();

        logger.info("Assert that user is in the list");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.usermanagement__userlist_content_row_fullname:contains(\"" + newUser.getFirstName() + "\")').length === 1"));

        logger.info("Edit of just created user and assert that all fields were saved correctly...");
        addNewUserForm = manageUsersTab.clickActionMenu(newUser.getFirstName()).clickEdit();

        Assert.assertEquals(addNewUserForm.getFirstName(), newUser.getFirstName());
        Assert.assertEquals(addNewUserForm.getLastName(), newUser.getLastName());
        Assert.assertEquals(addNewUserForm.getEmail(), newUser.getEmail());
        Assert.assertTrue(addNewUserForm.getRoles().contains("Requester"));

        Screenshoter.makeScreenshot();

        addNewUserForm.clickCancel();
    }

    @Test(priority = 2)
    @Description("This test checks that invitation email was sent")
    public void checkInvitationEmail()
    {
        logger.info("Waiting for 15 seconds...");
        try
        {
            Thread.sleep(15_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        String host = "imap.gmail.com";
        String mailStoreType = "imap";
        String username = "arthur.khasanov@parleypro.com";
        String password = "ParGd881";

        check(host, mailStoreType, username, password);
    }

    private static void check(String host, String storeType, String user, String password)
    {
        try
        {
            Properties properties = new Properties();

            properties.put("mail.imap.host", host);
            properties.put("mail.imap.port", "993");
            properties.put("mail.imap.starttls.enable", "true");
            properties.put("mail.imap.ssl.trust", host);

            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");

            store.connect(host, user, password);

            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);

            // Fetch unseen messages from inbox folder
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            logger.info("Amount of emails : " + messages.length);

            logger.info("Sort messages from recent to oldest...");
            Arrays.sort( messages, ( m1, m2 ) -> {
                try {
                    return m2.getSentDate().compareTo( m1.getSentDate() );
                } catch ( MessagingException e ) {
                    throw new RuntimeException( e );
                }
            } );

            boolean found = false;
            for ( Message message : messages )
            {
                if( message.getSubject().equals("Role assignment: Requester") )
                {
                    found = true;
                    logger.info("Email was found: " + message.getSubject() + " , " + message.getReceivedDate());
                    Address[] froms = message.getFrom();
                    String email = froms == null ? null : ((InternetAddress) froms[0]).getAddress();

                    Assert.assertEquals(email, "notification@parleypro.com");

                    logger.info("Delete this email...");
                    message.setFlag(Flags.Flag.DELETED, true);
                }
            }

            if( !found ) // Email was not found !
            {
                logger.error("Email with subject Role assignment: Requester was not found !!!");
            }

            inbox.close(false);
            store.close();
        } catch (NoSuchProviderException e) {
            logger.error("NoSuchProviderException", e);
        } catch (MessagingException e) {
            logger.error("MessagingException", e);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }

    @Test(priority = 3)
    @Description("This test delete user")
    public void cleanUp()
    {
        DashboardPage dashboardPage = new DashboardPage();

        ManageUsers manageUsersTab = dashboardPage.getSideBar().clickAdministration().clickManageUsersTab();

        manageUsersTab.clickActionMenu(newUser.getFirstName()).clickDelete(newUser).clickDelete();

        logger.info("Assert that user is no longer in the list...");
        Assert.assertFalse(Selenide.executeJavaScript("return $('.usermanagement__userlist_content_row_fullname:contains(\"AT User first_name\")').length === 1"));

        Screenshoter.makeScreenshot();
    }
}
