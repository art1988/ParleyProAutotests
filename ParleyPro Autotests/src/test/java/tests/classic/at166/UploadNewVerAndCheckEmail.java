package tests.classic.at166;

import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

@Listeners({ScreenShotOnFailListener.class})
public class UploadNewVerAndCheckEmail
{
    private String host     = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private String contractName = "AT-166 CTR";
    private String documentName = "AT-166_Manufacturing Agreement_1";
    private String emailSubject = "Contract  has  new external discussions";


    private static Logger logger = Logger.getLogger(UploadNewVerAndCheckEmail.class);

    @Test(priority = 1)
    public void deletePreviousEmailWithSubject()
    {
        // We assume that email with subject 'Contract  has  new external discussions' is already in INBOX from previous tests => need to delete it
        logger.info("Delete prev. email with subject " + emailSubject);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(host, username, password, emailSubject), "Email with subject: " + emailSubject + " was not found !!!");
    }

    @Test(priority = 2)
    public void uploadNewVerAndCheckEmail() throws InterruptedException
    {
        new DashboardPage().getSideBar()
                           .clickInProgressContracts(false)
                           .selectContract(contractName);

        new OpenedContract().clickUploadNewVersionButton(documentName)
                            .clickUploadCounterpartyDocument(Const.DOC_AT166_TWO, documentName, contractName)
                            .clickUpload(true);

        logger.info("Waiting for 60 seconds...");
        Thread.sleep(60_000);

        logger.info("Assert that CPU didn't received email with subject '" + emailSubject + "'...");
        Assert.assertFalse(EmailChecker.assertEmailBySubject(host, username, password, emailSubject),
                "CPU got email !!! Email with subject: '" + emailSubject + "' was found, but shouldn't !!!");

        Screenshoter.makeScreenshot();
    }
}
