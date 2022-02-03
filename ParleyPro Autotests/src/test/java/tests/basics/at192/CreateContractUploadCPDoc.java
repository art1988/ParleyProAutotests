package tests.basics.at192;

import com.codeborne.selenide.CollectionCondition;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;

import java.io.File;

import static com.codeborne.selenide.Selenide.$$;


public class CreateContractUploadCPDoc
{
    private static Logger logger = Logger.getLogger(CreateContractUploadCPDoc.class);

    @Test
    public void createContractUploadCPDoc()
    {
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle("Discussions board CTR");
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        logger.info("Uploading Kellogg Company_Warehouse Services Agreement Master_short.docx...");

        AddDocuments addDocuments = new AddDocuments();
        addDocuments.clickUploadCounterpartyDocuments(new File(System.getProperty("user.dir") + "/Documents/classicClientDocs/Kellogg Company_Warehouse Services Agreement Master_short.docx"));
        new ContractInNegotiation("Discussions board CTR").clickOk();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));
    }
}
