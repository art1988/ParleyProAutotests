package tests.regression.at110;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import forms.ManageDiscussions;
import forms.UploadDocumentDetectedChanges;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.Cache;
import utils.Screenshoter;
import utils.Waiter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class CreateContractAndUploadCPDocument
{
    private static Logger logger = Logger.getLogger(CreateContractAndUploadCPDocument.class);

    @Test(priority = 1)
    public void createContractAndUploadCPDocument() throws InterruptedException
    {
        ContractInformation contractInformation = new InProgressContractsPage(false).clickNewContractButton();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withLocale(Locale.US);
        String dynamicContractTitle = "Track changes AT-110_" + LocalDateTime.now().format(formatter);

        Cache.getInstance().setContractTitle(dynamicContractTitle);

        contractInformation.setContractTitle( Cache.getInstance().getCachedContractTitle() );
        contractInformation.checkClassicNegotiationModeCheckbox();
        contractInformation.clickSave();

        UploadDocumentDetectedChanges uploadDocumentDetectedChanges = new AddDocuments()
                .clickUploadMyTeamDocumentsWithDetectedChanges( Const.TRACK_CHANGES_AT110_V1 );

        uploadDocumentDetectedChanges.setCounterpartyOrganization("Eugene's Test, Inc.");
        uploadDocumentDetectedChanges.setCounterpartyNegotiatorEmail("counterparty@example.com");
        uploadDocumentDetectedChanges.clickOk();

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"March 1, 2021\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Exhibit B\")')");

        $(".notification-stack").waitUntil(Condition.appear, 6_000).shouldHave(Condition.exactText("Document Acorns Engagement Letter_party_changes_v1 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        OpenedContract openedContract = new OpenedContract();

        logger.info("Assert that total amount of discussions is equals 1...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "1", "Amount of total discussion is incorrect !!!");

        ManageDiscussions manageDiscussionsForm = openedContract.clickManageDiscussions();
        Assert.assertEquals(manageDiscussionsForm.getAmountOfInternalDiscussions(), "1", "Amount of internal discussions is incorrect !!!");
        manageDiscussionsForm.clickDone();

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void checkOneInternalDiscussion()
    {
        // Scroll to 4. Limitations on Damages and Indemnification.
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"Confidentiality.\")')[0].scrollIntoView({});");

        new OpenedContract(true).clickByDiscussionIcon("Limitations on Damages and Indemnification");

        logger.info("Assert that post was added and changes were applied...");
        $$(".discussion2-post").shouldHave(CollectionCondition.size(1));
        $$(".discussion2-post ins").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.texts("except in the case of a breach of section 3 hereto which will not have a limitation on"));
        $$(".discussion2-post del").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.texts("during the twelve (12) months preceding the date a claim for liability arises"));

        Screenshoter.makeScreenshot();

        Selenide.refresh();
        new OpenedContract();
    }
}
