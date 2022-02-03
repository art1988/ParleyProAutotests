package tests.regression.at209;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Cache;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadDocAndShareWithCCN
{
    private static final String contractTitle = "AT-209 - Contract_VALUE";

    @Test(priority = 1)
    public void uploadDocAndShareWithCCN()
    {
        ContractInformation contractInformationForm = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        Cache.getInstance().setContractTitle(contractTitle);

        contractInformationForm.setContractTitle(contractTitle);
        contractInformationForm.setContractValue("177753082");
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setCounterpartyChiefNegotiator(Const.PREDEFINED_CCN.getEmail());
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");
        contractInformationForm.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        new OpenedContract().switchDocumentToNegotiate("AT-14", "CounterpartyAT", false).clickNext(false).clickStart();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract " + contractTitle + " is now visible to the Counterparty."));

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));
    }

    @Test(priority = 2)
    public void logoutAsCN()
    {
        new DashboardPage().getSideBar().logout();
    }
}
