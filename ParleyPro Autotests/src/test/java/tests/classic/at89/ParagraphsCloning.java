package tests.classic.at89;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
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
public class ParagraphsCloning
{
    private String contractName = "AT-89: paragraphs cloning";
    private String documentName = "ParagraphClonning_AT89";

    private Logger logger = Logger.getLogger(ParagraphsCloning.class);

    @Test(priority = 1)
    public void createClassicContractAndUploadDoc()
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
        new AddDocuments().clickUploadMyTeamDocumentsWithDetectedChanges( Const.DOC_PARAGRAPH_CLONING_AT89_V1 ).clickOk();

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Chapter 1\")')");

        $(".notification-stack").waitUntil(Condition.appear, 6_000).shouldHave(Condition.exactText("Document " + documentName + " has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test uploads doc2 as counterparty and verifies that paragraphs weren't duplicated.")
    public void uploadDocV2AsCounterpartyAndCheckParagraphs() throws InterruptedException
    {
        DocumentComparePreview documentComparePreview = new OpenedContract().clickUploadNewVersionButton(documentName)
                .clickUploadCounterpartyDocument( Const.DOC_PARAGRAPH_CLONING_AT89_V2, documentName, contractName );

        logger.info("Assert that only 3 comments were added. Other counters should be empty.");
        Assert.assertEquals(documentComparePreview.getCounterCommented(), "3", "Number of comments is different ! Should be 3 !");
        Assert.assertEquals(documentComparePreview.getCounterAdded(), "", "Number of added discussions should be empty !");
        Assert.assertEquals(documentComparePreview.getCounterDeleted(), "", "Number of deleted discussions should be empty !");
        Assert.assertEquals(documentComparePreview.getCounterEdited(), "", "Number of edited discussions should be empty !");

        documentComparePreview.clickUpload(true).clickDocumentsTab();

        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Second\")')");
        Thread.sleep(1_000);

        logger.info("Assert that paragraphs weren't duplicated...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Chapter 1\")').length === 1"), "Looks like that 'Chapter 1' header was doubled !");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Second\")').length === 1"), "Looks like that 'Second paragraph.' was doubled !");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph .discussion-indicator').length === 1"), "Looks like that discussion icon was doubled !");

        Screenshoter.makeScreenshot();
    }
}
