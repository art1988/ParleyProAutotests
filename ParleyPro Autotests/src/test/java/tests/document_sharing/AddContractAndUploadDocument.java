package tests.document_sharing;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddContractAndUploadDocument
{
    @Test
    public void createContractAndUploadDocument()
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Contract DOCUMENT Sharing");
        contractInformationForm.setContractingRegion("region2");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity2");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category2");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_FORMATTING_SAMPLE );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Times new roman bold 14 size\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Heading style calibri light 16 blue\")')");

        $(".notification-stack").waitUntil(Condition.appear, 6_000).shouldHave(Condition.exactText("Document Formatting has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        Screenshoter.makeScreenshot();
    }
}
