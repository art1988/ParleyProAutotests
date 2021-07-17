package tests.classic.classic_runner;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import forms.EmailWillBeSentToTheCounterparty;
import forms.StartNegotiation;
import forms.UploadDocumentDetectedChanges;
import org.apache.log4j.Logger;
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
        logger.info("Uploading the following client's document: " + docNameWithoutExtension);
        // 2. UPLOAD MY TEAM DOCUMENTS
        if( documentHasRevisions == true ) // if document has revisions then after uploading it should be in Negotiate status
        {
            UploadDocumentDetectedChanges detectedChangesForm = new AddDocuments().clickUploadMyTeamDocumentsWithDetectedChanges( new File(Const.CLIENT_DOCS_DIR.getAbsolutePath() + "/" + documentName) );

            detectedChangesForm.setCounterpartyOrganization("CounterpartyAT");
            detectedChangesForm.setCounterpartyNegotiatorEmail("arthur.khasanov+cpat@parleypro.com");
            detectedChangesForm.clickOk();

            $(".spinner").waitUntil(Condition.disappear, 60_000 * 2);
            $(".document__body .spinner").waitUntil(Condition.disappear, 60_000 * 2);
        }
        else // else move it to Negotiate status
        {
            new AddDocuments().clickUploadMyTeamDocuments( new File(Const.CLIENT_DOCS_DIR.getAbsolutePath() + "/" + documentName) );

            $(".spinner").waitUntil(Condition.disappear, 60_000 * 2);
            $(".document__spinner .spinner").waitUntil(Condition.disappear, 60_000 * 2);
            $(".document__body .spinner").waitUntil(Condition.disappear, 60_000 * 2);

            Selenide.refresh();

            $(".spinner").waitUntil(Condition.disappear, 60_000);
            $(".document__body .spinner").waitUntil(Condition.disappear, 60_000);

            $(".contract-header__name").waitUntil(Condition.visible, 20_000);
            $(".contract-header__name").shouldBe(Condition.enabled);

            StartNegotiation startNegotiationForm = new OpenedContract().switchDocumentToNegotiate(docNameWithoutExtension, "", true);
            EmailWillBeSentToTheCounterparty emailWillBeSentToTheCounterpartyForm = startNegotiationForm.clickNext(true);

            emailWillBeSentToTheCounterpartyForm.setCounterpartyOrganization("CounterpartyAT");
            emailWillBeSentToTheCounterpartyForm.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");

            emailWillBeSentToTheCounterpartyForm.clickStart();

            logger.info("Assert that status was changed to NEGOTIATE...");
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

            logger.info("Assert that WITH MY TEAM label is visible...");
            $(".label_theme_orange").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("WITH MY TEAM"));

            Screenshoter.makeScreenshot();
        }
    }

    @Test(priority = 2)
    public void closeNotificationsPopups() throws InterruptedException
    {
        Thread.sleep(500);
        // close all notification popups if they are present
        if( $(".notification-stack").is(Condition.visible) )
        {
            if( $$(".notification-stack .notification-stack__item").isEmpty() )
            {
                return;
            }

            for( int i = 0; i < $$(".notification-stack .notification-stack__item").size(); i++ )
            {
                $$(".notification-stack .notification-stack__item").get(i).find(".notification__close").click();
            }
        }
    }
}
