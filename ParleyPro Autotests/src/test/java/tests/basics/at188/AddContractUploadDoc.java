package tests.basics.at188;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import forms.ContractInformation;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;

import static com.codeborne.selenide.Selenide.$$;


public class AddContractUploadDoc
{
    @Test
    public void addContractUploadDoc()
    {
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle("CKE_Mentioning ctr");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        AddDocuments addDocuments = new AddDocuments();
        addDocuments.clickUploadMyTeamDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        new OpenedContract().switchDocumentToReview("AT-14").clickStart();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));
    }
}
