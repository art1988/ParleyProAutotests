package tests.contract_info.at169;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.ContractInfo;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateManagedContractAndFillFields
{
    @Test(priority = 1)
    public void createManagedContract()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("AT-169 auto-renewal");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadExecutedDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );

        $(".notification-stack").waitUntil(Condition.visible, 40_000).shouldHave(Condition.exactText("Document AT-14 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 55_000);

        ContractInfo contractInfo = new ContractInfo();

        contractInfo.setSignatureDate();
        contractInfo.setEffectiveDate();
        contractInfo.setInitialTerm("2");
        contractInfo.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("Contract has been updated."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);
    }

    @Test(priority = 2)
    public void enableAutoRenewalAndFillFields() throws InterruptedException
    {
        ContractInfo contractInfo = new ContractInfo();

        contractInfo.clickAutoRenewalTumbler();
        Thread.sleep(2_000);
    }
}
