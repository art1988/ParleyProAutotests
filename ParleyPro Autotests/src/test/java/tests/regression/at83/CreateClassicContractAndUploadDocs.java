package tests.regression.at83;

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
import pages.*;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateClassicContractAndUploadDocs
{
    private String contractName = "AT83 Classic";
    private String documentName = "bdoc1";

    private static Logger logger = Logger.getLogger(CreateClassicContractAndUploadDocs.class);

    @Test(priority = 1)
    public void createClassicContract()
    {
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle(contractName);
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.checkClassicNegotiationModeCheckbox();
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();
    }

    @Test(priority = 2)
    public void uploadDocAsCounterparty()
    {
        new AddDocuments().clickUploadCounterpartyDocuments( Const.REGRESSION_DOC_AT83_BDOC1 );

        $(".notification-stack").waitUntil(Condition.appear, 20_000).shouldHave(Condition.exactText("Document " + documentName + " has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 45_000);

        new ContractInNegotiation(contractName).clickOk();

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test issue setDomainConfig('acme') command and uploads BDOC2 as my team.")
    public void uploadNewVersionOfDocAsMyTeam()
    {
        logger.info("Issue setDomainConfig('acme')...");
        Selenide.executeJavaScript("setDomainConfig('acme')");

        UploadNewVersionOfDocument uploadNewVersionOfDocument = new OpenedContract().clickUploadNewVersionButton(documentName);

        logger.info("Assert that green button 'Upload my team document' is enabled after issuing a command...");
        $(".js-upload-my-team-document-btn").shouldBe(Condition.enabled).shouldBe(Condition.visible);

        DocumentComparePreview documentComparePreview = uploadNewVersionOfDocument
                .clickUploadMyTeamDocument( Const.REGRESSION_DOC_AT83_BDOC2, documentName, contractName );

        DiscussionsOfSingleContract discussionsOfSingleContract = documentComparePreview.clickUpload(true);
        discussionsOfSingleContract.clickDocumentsTab();

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    @Description("This test make post as queued.")
    public void makeDiscussionQueued()
    {
        logger.info("Scroll to 'B. Client’s Right to Terminate.' discussion...");
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"5. Termination\")')[0].scrollIntoView({})");

        logger.info("Open discussion for 'Client’s Right to Terminate.' paragraph...");
        OpenedDiscussion openedDiscussion = new OpenedContract(true).clickByDiscussionIcon("Right to Terminate.");
        openedDiscussion.clickMakeQueued("Notwithstanding the foregoing");

        logger.info("Assert that discussion marked as queued...");
        $(".documents-discussion-panel .discussion-header__status .discussion2-label__status")
                .shouldBe(Condition.visible).shouldHave(Condition.exactText("queued"));
        logger.info("Assert that post became queued...");
        $$(".documents-discussion-panel .discussion2-post .is_active_queued")
                .shouldHave(CollectionCondition.size(1)).first().shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 5)
    @Description("Download doc as Counterparty so that queued post become external.")
    public void downloadForCounterparty() throws FileNotFoundException
    {
        new OpenedContract(true).clickDocumentActionsMenu(documentName).clickDownload(true).clickDownloadForCounterparty();
        $(".modal-body").waitUntil(Condition.disappear, 20_000);

        logger.info("Making sure that after downloading queued post become external...");

        Selenide.refresh();
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Services Agreement.\")')");

        logger.info("Assert that there is no '1 QUEUED' label anymore...");
        $(".label.label_theme_pink").should(Condition.disappear);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 6)
    public void uploadFinalDocAsCounterparty()
    {
        new OpenedContract().clickUploadNewVersionButton(documentName)
                            .clickUploadCounterpartyDocument( Const.REGRESSION_DOC_AT83_BDOC3, documentName, contractName)
                            .clickUpload(true)
                            .clickDocumentsTab();

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 7)
    @Description("This test opens 'B. Client’s Right to Terminate.' discussion and checks that del post is present")
    public void openDiscussion()
    {
        logger.info("Scroll to 'B. Client’s Right to Terminate.' discussion...");
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"5. Termination\")')[0].scrollIntoView({})");

        logger.info("Assert that paragraph doesn't have del and ins tags...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Client’s Right to Terminate.\")').find(\"ins\").length === 0"), "Paragraph shouldn't have insert tag !");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Client’s Right to Terminate.\")').find(\"del\").length === 0"), "Paragraph shouldn't have delete tag !");

        logger.info("Open discussion for 'Client’s Right to Terminate.' paragraph...");
        OpenedDiscussion openedDiscussion = new OpenedContract(true).clickByDiscussionIcon("Right to Terminate.");

        logger.info("Scroll discussion panel to the bottom...");
        Selenide.executeJavaScript("document.querySelector('.discussion2__body__scrollable-body').scrollTo(0,document.querySelector('.discussion2__body__scrollable-body').scrollHeight)");

        logger.info("Assert total number of posts...");
        Assert.assertEquals(openedDiscussion.getCountOfPosts(), "4", "Number of post should be = 4 !");

        logger.info("Assert that only one del present for the last post...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.discussion2__body .discussion2-post').last().find(\"del\").length === 1"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 8)
    public void cleanUpDownloadDir() throws IOException
    {
        // Clean download dir
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);

        // Set back qa-autotests configs
        logger.info("Issue setDomainConfig() to reset...");
        Selenide.executeJavaScript("setDomainConfig()");
    }
}
