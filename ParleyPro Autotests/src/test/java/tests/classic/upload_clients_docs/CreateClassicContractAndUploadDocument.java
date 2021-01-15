package tests.classic.upload_clients_docs;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import forms.EmailWillBeSentToTheCounterparty;
import forms.StartNegotiation;
import forms.UploadDocumentDetectedChanges;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateClassicContractAndUploadDocument
{
    private String documentName;

    private static Logger logger = Logger.getLogger(CreateClassicContractAndUploadDocument.class);

    @Test(priority = 1)
    @Parameters({"documentName", "documentHasRevisions"})
    public void createClassicContractAndUploadDocument(String documentName, boolean documentHasRevisions) throws InterruptedException
    {
        this.documentName = documentName.substring(0, documentName.indexOf("."));

        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Classic contract - client docs");
        contractInformationForm.checkClassicNegotiationModeCheckbox();
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        String docNameWithoutExtension = documentName.substring(0, documentName.indexOf("."));
        // 2. UPLOAD MY TEAM DOCUMENTS
        AddDocuments addDocuments = new AddDocuments();
        if( documentHasRevisions == true ) // if document has revisions then after uploading it should be in Negotiate status
        {
            UploadDocumentDetectedChanges detectedChangesForm = addDocuments.clickUploadMyTeamDocumentsWithDetectedChanges( new File(Const.CLIENT_DOCS_DIR.getAbsolutePath() + "/" + documentName) );

            detectedChangesForm.setCounterpartyOrganization("CounterpartyAT");
            detectedChangesForm.setCounterpartyNegotiatorEmail("cpName cpLastName (arthur.khasanov+cpat@parleypro.com)");
            detectedChangesForm.clickOk();

            $(".spinner").waitUntil(Condition.disappear, 25_000);
            $(".notification-stack").waitUntil(Condition.appear, 6_000).shouldHave(Condition.exactText("Document " + docNameWithoutExtension + " has been successfully uploaded."));
            $(".notification-stack").waitUntil(Condition.disappear, 25_000);
        }
        else // else move it to Negotiate status
        {
            addDocuments.clickUploadMyTeamDocuments( new File(Const.CLIENT_DOCS_DIR.getAbsolutePath() + "/" + documentName) );

            $(".spinner").waitUntil(Condition.disappear, 25_000);

            if( $(".notification-stack").getText().contains("unsupported formatting attributes were found") )
            {
                $(".notification-stack .notification__close").click(); // Close that warning popup
            }

            $(".notification-stack").waitUntil(Condition.appear, 6_000).shouldHave(Condition.exactText("Document " + docNameWithoutExtension + " has been successfully uploaded."));
            $(".notification-stack").waitUntil(Condition.disappear, 25_000);

            logger.info("Scroll to top of page...");
            Selenide.executeJavaScript("document.querySelector('.documents__list').scrollTo(0,0)");
            Thread.sleep(1_000);

            StartNegotiation startNegotiationForm = new OpenedContract().switchDocumentToNegotiate(docNameWithoutExtension, true);
            EmailWillBeSentToTheCounterparty emailWillBeSentToTheCounterpartyForm = startNegotiationForm.clickNext(true);

            emailWillBeSentToTheCounterpartyForm.setCounterpartyOrganization("CounterpartyAT");
            Thread.sleep(1_000);
            emailWillBeSentToTheCounterpartyForm.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");

            emailWillBeSentToTheCounterpartyForm.clickStart();

            logger.info("Assert visible to the counterparty notification...");
            $(".notification-stack").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Contract Classic contract - client docs is now in negotiation. No notification was sent to the Counterparty."));

            logger.info("Assert that status was changed to NEGOTIATE...");
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

            logger.info("Assert that WITH MY TEAM label is visible...");
            $(".label_theme_orange").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("WITH MY TEAM"));

            Screenshoter.makeScreenshot();
        }
    }
}
