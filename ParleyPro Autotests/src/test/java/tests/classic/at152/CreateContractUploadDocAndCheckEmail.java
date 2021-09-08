package tests.classic.at152;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractUploadDocAndCheckEmail
{
    private String host     = "imap.gmail.com";
    private String username = "arthur.khasanov@parleypro.com";
    private String password = "ParGd881";

    private String contractTitle = "AT152 - Counterparty receives unexpected emails";
    private static Logger logger = Logger.getLogger(CreateContractUploadDocAndCheckEmail.class);

    @Test(priority = 1)
    public void createContractAndUploadDoc() throws InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle(contractTitle);
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setCounterpartyChiefNegotiator( Const.PREDEFINED_CCN.getEmail() );
        contractInformation.checkClassicNegotiationModeCheckbox();
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );
        new ContractInNegotiation(contractTitle).clickOk();

        Selenide.refresh();

        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"delete me\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Unused extra\")')");
    }

    @Test(priority = 2)
    public void createQueuedPostAndDownloadForCP() throws InterruptedException, FileNotFoundException
    {
        new OpenedContract().hover("Paragraph 1")
                            .clickDelete()
                            .setComment("del")
                            .selectQueued()
                            .clickPost();

        $(".notification-stack").waitUntil(Condition.appear, 25_000).shouldHave(Condition.text("Queued discussion Paragraph 1"));

        logger.info("Making sure that queued post indicator is visible...");
        $$(".discussion-indicator.queued").shouldHave(CollectionCondition.size(1));
        $(".label.label_theme_pink").shouldBe(Condition.visible).shouldHave(Condition.exactText("1 QUEUED"));

        logger.info("Downloading doc as CP...");
        new OpenedContract().clickDocumentActionsMenu("AT-14")
                            .clickDownload(true)
                            .clickDownloadForCounterparty();

        logger.info("Check that discussion become internal...");
        $(".label.label_theme_pink").waitUntil(Condition.hidden, 35_000);
        $$(".discussion-indicator.negotiating").shouldHave(CollectionCondition.size(3));
    }

    @Test(priority = 3)
    public void cleanUpDownloadDir() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }

    @Test(priority = 4)
    public void addNewQueuedPostAcceptDiscussionCheckEmail() throws InterruptedException
    {
        new OpenedContract().clickByParagraph("Paragraph 1")
                            .setComment("new queued")
                            .selectQueued()
                            .clickPost();

        $(".notification-stack").waitUntil(Condition.appear, 25_000).shouldHave(Condition.text("Queued post"));

        new OpenedContract().clickManageDiscussions()
                            .acceptExternalDiscussions()
                            .confirmAccept()
                            .clickDone();

        logger.info("Checking that closed discussion is only one...");
        $$(".discussion-indicator.closed").shouldHave(CollectionCondition.size(1));

        Screenshoter.makeScreenshot();

        logger.info("Waiting for 60 sec...");
        Thread.sleep(60_000);

        String emailSubjToVerify = contractTitle + " for AT-166";
        logger.info("Assert that CCN didn't received email with subject '" + emailSubjToVerify + "'...");

        Assert.assertFalse(EmailChecker.assertEmailBySubject(host, username, password, emailSubjToVerify),
                "Email with subject: '" + emailSubjToVerify + "' was found, but shouldn't !!!");
    }
}
