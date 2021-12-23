package tests.basics.at21;

import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import utils.Screenshoter;
import utils.Waiter;

public class CreateContractForFormatting
{
    @Test
    @Description("Precondition: this test creates new contract and uploads CONTRACT_FORMATTING_SAMPLE document.")
    public void createContractForFormatting()
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Contract formatting autotest");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickUploadMyTeamDocuments(Const.DOCUMENT_FORMATTING_SAMPLE);

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Times new roman bold 14 size\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Heading style calibri light 16 blue\")')");

        Screenshoter.makeScreenshot();
    }
}
