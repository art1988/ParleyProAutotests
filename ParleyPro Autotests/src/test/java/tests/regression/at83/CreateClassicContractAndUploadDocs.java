package tests.regression.at83;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import java.io.FileNotFoundException;

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

        $(".notification-stack").waitUntil(Condition.appear, 6_000).shouldHave(Condition.exactText("Document " + documentName + " has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

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
        logger.info("Scroll to B. Client’s Right to Terminate. discussion...");
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"5. Termination\")')[0].scrollIntoView({})");

        OpenedDiscussion openedDiscussion = new OpenedContract(true).clickByDiscussionIcon("Client’s Right to Terminate.");
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
        UploadNewVersionOfDocument uploadNewVersionOfDocument = new OpenedContract().clickUploadNewVersionButton(documentName);

        DocumentComparePreview documentComparePreview = uploadNewVersionOfDocument
                .clickUploadCounterpartyDocument( Const.REGRESSION_DOC_AT83_BDOC3, documentName, contractName);

        DiscussionsOfSingleContract discussionsOfSingleContract = documentComparePreview.clickUpload(true);

        discussionsOfSingleContract.clickDocumentsTab();
    }

    // TODO: finish when PAR-13578 will be fixed
}
