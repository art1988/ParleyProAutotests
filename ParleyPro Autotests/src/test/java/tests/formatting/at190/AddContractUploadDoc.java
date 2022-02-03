package tests.formatting.at190;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AddContractUploadDoc
{
    @Test
    public void addContractUploadDoc()
    {
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle("IEEE Bullets CTR");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        AddDocuments addDocuments = new AddDocuments();
        addDocuments.clickUploadCounterpartyDocuments( Const.DOC_FORMATTING_AT190 );

        new ContractInNegotiation("IEEE Bullets CTR").clickOk();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        // Close red notification warning...
        $$(".notification-stack").forEach( notif -> {
            SelenideElement warningMsg = notif.find(".documents-upload-warnings");
            warningMsg.closest(".notification-stack__item").find(".notification__close").click();
        } );
    }
}
