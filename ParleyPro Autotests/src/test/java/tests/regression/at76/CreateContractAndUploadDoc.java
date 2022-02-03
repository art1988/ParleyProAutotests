package tests.regression.at76;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;


public class CreateContractAndUploadDoc
{
    @Test
    public void createContractAndUploadDoc()
    {
        // 1. + NEW CONTRACT
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle("APLL: 50 emails");
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // 2. UPLOAD CP DOCUMENT
        new AddDocuments().clickUploadCounterpartyDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );
        new ContractInNegotiation("APLL: 50 emails").clickOk();

        // Assert notification
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("Document AT-14 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"delete me\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Unused extra\")')");

        Screenshoter.makeScreenshot();
    }
}
