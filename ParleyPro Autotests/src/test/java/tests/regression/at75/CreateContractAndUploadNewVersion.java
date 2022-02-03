package tests.regression.at75;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import forms.UploadDocumentDetectedChanges;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DocumentComparePreview;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractAndUploadNewVersion
{
    private String contractName = "MSW Classic: Wrong numbering";
    private String documentName = "MSW_v1";

    @Test(priority = 1)
    public void createContract() throws InterruptedException
    {
        // 1. + NEW CONTRACT
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

        // 2. UPLOAD MY TEAM DOCUMENTS
        UploadDocumentDetectedChanges uploadDocumentForm = new AddDocuments().clickUploadMyTeamDocumentsWithDetectedChanges( Const.REGRESSION_DOC_AT75_V1 );

        uploadDocumentForm.setCounterpartyOrganization("CounterpartyAT");
        uploadDocumentForm.setCounterpartyNegotiatorEmail("arthur.khasanov+cpat@parleypro.com");
        uploadDocumentForm.clickOk();

        $(".notification-stack").waitUntil(Condition.visible, 35_000).shouldHave(Condition.exactText("Document " + documentName + " has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Adoption and Translation Agreement\")')");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void uploadNewVersion()
    {
        OpenedContract openedContract = new OpenedContract();

        DocumentComparePreview documentComparePreview = openedContract.clickUploadNewVersionButton(documentName)
                .clickUploadCounterpartyDocument( Const.REGRESSION_DOC_AT75_V2 , documentName, contractName);

        documentComparePreview.clickUpload(true).clickDocumentsTab();
    }
}
