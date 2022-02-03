package tests.basics.at61;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
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
public class CreateContractAndUploadTwoDocumentsAT61
{
    private static Logger logger = Logger.getLogger(CreateContractAndUploadTwoDocumentsAT61.class);

    @Test
    public void createContractAndUploadTwoDocumentsAT61()
    {
        // + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Contract - [delete and cancel documents AT]");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // 1
        logger.info("Uploading first document (AT-40)...");
        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_CLASSIC_AT40 );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"delete me\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Unused extra\")')");

        $(".notification-stack").waitUntil(Condition.appear, 6_000).shouldHave(Condition.exactText("Document AT-40 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);


        // 2
        logger.info("Uploading second document (Formatting)...");
        new OpenedContract().clickNewDocument().clickUploadMyTeamDocuments( Const.DOCUMENT_FORMATTING_SAMPLE );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Times new roman bold 14 size\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Heading style calibri light 16 blue\")')");

        $(".notification-stack").waitUntil(Condition.appear, 6_000).shouldHave(Condition.exactText("Document Formatting has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        Screenshoter.makeScreenshot();
    }
}
