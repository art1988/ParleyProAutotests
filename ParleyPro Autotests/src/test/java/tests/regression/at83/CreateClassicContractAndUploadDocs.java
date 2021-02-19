package tests.regression.at83;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

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
        new AddDocuments().clickUploadCounterpartyDocuments(Const.REGRESSION_DOC_AT83_BDOC1 );

        $(".notification-stack").waitUntil(Condition.appear, 6_000).shouldHave(Condition.exactText("Document " + documentName + " has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        new ContractInNegotiation(contractName).clickOk();

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
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
    }

    @Test(priority = 4)
    public void makeDiscussionQueued()
    {
        logger.info("Scroll to B. Client’s Right to Terminate. discussion...");
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"5. Termination\")')[0].scrollIntoView({})");

        new OpenedContract(true).clickByDiscussionIcon("Client’s Right to Terminate.");
    }
}
