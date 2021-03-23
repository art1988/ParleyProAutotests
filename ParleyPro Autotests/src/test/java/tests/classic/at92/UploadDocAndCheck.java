package tests.classic.at92;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import forms.EmailWillBeSentToTheCounterparty;
import forms.StartNegotiation;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadDocAndCheck
{
    private String contractName = "Classic for AT-92";
    private String documentName = "Manufacturing_Agreement_draft_3";
    private static Logger logger = Logger.getLogger(UploadDocAndCheck.class);

    @Test(priority = 1)
    public void createContractAndUploadDoc()
    {
        // add contract
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle(contractName);
        contractInformationForm.checkClassicNegotiationModeCheckbox();
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // upload doc
        new AddDocuments().clickUploadMyTeamDocuments( Const.CLASSIC_AT92_V1 );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Production of Products\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"56.\")')");

        $(".notification-stack").waitUntil(Condition.appear, 6_000).shouldHave(Condition.exactText("Document " + documentName + " has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void uploadNewVer()
    {
        // Move to Negotiate
        new OpenedContract().switchDocumentToNegotiate(documentName, "CounterpartyAT", true).clickStart();

        $("#UPLOAD_VERSION_DOCUMENT").waitUntil(Condition.visible, 10_000).waitUntil(Condition.enabled, 10_000);

        new OpenedContract().clickUploadNewVersionButton(documentName)
                            .clickUploadCounterpartyDocument( Const.CLASSIC_AT92_V2, documentName, contractName )
                            .clickUpload(true)
                            .clickDocumentsTab();
    }
}
