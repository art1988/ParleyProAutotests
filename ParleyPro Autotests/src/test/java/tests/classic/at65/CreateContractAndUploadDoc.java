package tests.classic.at65;


import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;


public class CreateContractAndUploadDoc
{
    @Test
    public void createContractAndUploadDoc()
    {
        // Add new contract
        ContractInformation contractInformationForm = new DashboardPage().getSideBar()
                                                                         .clickInProgressContracts(true)
                                                                         .clickNewContractButton();

        contractInformationForm.setContractTitle("at-65: comment glues");
        contractInformationForm.checkClassicNegotiationModeCheckbox();
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // Upload doc as CP
        new AddDocuments().clickUploadCounterpartyDocuments( Const.DOCUMENT_AT65_GLUE );

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("Document Dynatrace_Vendor_Addendum has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        new ContractInNegotiation("at-65: comment glues").clickOk();

        Screenshoter.makeScreenshot();
    }
}
