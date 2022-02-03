package tests.regression.at140;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractUploadDocAndShare
{
    @Test(priority = 1)
    public void createContract()
    {
        // 1. + NEW CONTRACT
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle("CTR AT-140");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickUploadCounterpartyDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("has been successfully uploaded."));

        new ContractInNegotiation("CTR AT-140").clickOk();
    }

    @Test(priority = 2)
    public void shareWithCCN() throws InterruptedException
    {
        new OpenedContract().clickSendInvite()
                            .clickNext(false)
                            .setCounterpartyOrganization("CounterpartyAT")
                            .setCounterpartyChiefNegotiator( Const.PREDEFINED_CCN.getEmail() )
                            .clickStart();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Documents have been shared"));

        Screenshoter.makeScreenshot();

        // Logout
        new DashboardPage().getSideBar().logout();
    }
}
