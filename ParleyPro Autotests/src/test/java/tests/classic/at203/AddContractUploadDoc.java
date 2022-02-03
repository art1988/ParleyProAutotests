package tests.classic.at203;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import forms.ContractInformation;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import utils.Cache;

import static com.codeborne.selenide.Selenide.$$;


public class AddContractUploadDoc
{
    @Test
    public void addContractUploadDoc()
    {
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        String contractTitle = "AT-203: MoveFromOnlineToClassic CTR name";
        Cache.getInstance().setContractTitle(contractTitle);

        contractInformationForm.setContractTitle(contractTitle);
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_FORMATTING_SAMPLE );
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));
    }
}
