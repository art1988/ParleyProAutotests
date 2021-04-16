package tests.amendment.at69;

import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.ContractInfo;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateExecutedContract
{
    @Test(priority = 1)
    public void createExecutedContract()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar()
                                                                     .clickExecutedContracts(true)
                                                                     .clickNewContractButton();

        contractInformation.setContractTitle("at-69 Amendment");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country2");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department2");
        contractInformation.setContractCategory("Testcat"); // !!!
        contractInformation.setContractType("type2");

        // Fill all fields for 'Testcat' category
        for( int i = 1; i <= 10; i++ )
        {
            contractInformation.setValueForCustomField("Field" + i, FieldType.TEXT_AREA, "f" + i + " val");
        }

        contractInformation.clickSave();
    }

    @Test(priority = 2)
    public void uploadExecutedDoc()
    {
        new AddDocuments().clickUploadExecutedDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("Document AT-14 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        ContractInfo contractInfo = new ContractInfo();

        contractInfo.setSignatureDate();
        contractInfo.setEffectiveDate();
        contractInfo.setInitialTerm("12");

        contractInfo.clickSave();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("Contract has been updated."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);
    }
}
