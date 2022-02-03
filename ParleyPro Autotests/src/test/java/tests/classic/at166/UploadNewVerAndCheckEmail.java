package tests.classic.at166;

import constants.Const;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.EmailChecker;
import utils.Screenshoter;


public class UploadNewVerAndCheckEmail
{
    private String host     = "pop.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private String contractName = "AT-166 CTR";
    private String documentName = "AT-166_Manufacturing Agreement_1";
    private String emailSubject = contractName + " for AT-166";


    private static Logger logger = Logger.getLogger(UploadNewVerAndCheckEmail.class);


    @Test(priority = 1)
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

        logger.info("Assert that Counterparty User didn't received email with subject '" + emailSubject + "'...");
        Assert.assertFalse(EmailChecker.assertEmailBySubject(host, username, password, emailSubject),
                "CPU got email !!! Email with subject: '" + emailSubject + "' was found, but shouldn't !!!");

        Screenshoter.makeScreenshot();
    }
}
