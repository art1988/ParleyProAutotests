package tests.regression.at82;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.ContractInfo;
import pages.DashboardPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;


public class CreateManagedContract
{
    @Test
    public void createManagedContract()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(true).clickNewContractButton();

        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");

        contractInformation.clickSave();

        new AddDocuments().clickUploadExecutedDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );

        ContractInfo contractInfo = new ContractInfo();
        contractInfo.setSignatureDate();
        contractInfo.setEffectiveDate();
        contractInfo.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract has been updated."));

        Screenshoter.makeScreenshot();
    }
}
