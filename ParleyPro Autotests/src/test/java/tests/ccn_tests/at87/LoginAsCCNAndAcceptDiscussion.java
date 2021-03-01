package tests.ccn_tests.at87;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsCCNAndAcceptDiscussion
{
    private String host = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private Logger logger = Logger.getLogger(LoginAsCCNAndAcceptDiscussion.class);

    @Test(priority = 1)
    public void getEmail() throws InterruptedException
    {
        logger.info("Waiting for 20 seconds to make sure that email has been delivered...");
        Thread.sleep(20_000);

        String emailSubject = "[qa-autotests] autotest_cn fn ln shared contract \"CCN: READY FOR SIGNATURE button contract\" with you";
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, emailSubject), "Email with subject: " + emailSubject + " was not found !!!");


    }
}
