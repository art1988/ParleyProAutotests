package tests.classic.at166;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.AuditTrail;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractAndUploadDoc
{
    private String contractTitle = "AT-166 CTR";
    private String cpOrganization = "CounterpartyAT";

    @Test
    public void createContractAndUploadDoc()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle(contractTitle);
        contractInformation.setCounterpartyOrganization(cpOrganization);
        contractInformation.setCounterpartyChiefNegotiator( Const.PREDEFINED_CCN.getEmail() );
        contractInformation.checkClassicNegotiationModeCheckbox();
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments( Const.DOC_AT166_ONE );

        $(".notification-stack").waitUntil(Condition.appear, 20_000);
        $(".notification-stack").waitUntil(Condition.disappear, 40_000);
        new ContractInNegotiation(contractTitle).clickOk();

        OpenedContract openedContract = new OpenedContract();

        openedContract.clickEmailContractToCounterparty(cpOrganization)
                      .clickNext()
                      .clickAddCounterpartyUsers()
                      .setEmailOfAdditionalCounterpartyUser( Const.USER_GREG.getEmail() )
                      .clickEmailContract();

        $(".notification-stack").waitUntil(Condition.appear, 20_000)
                .shouldHave(Condition.text("has been emailed to"));
        $(".notification-stack").waitUntil(Condition.disappear, 40_000);

        AuditTrail auditTrail = openedContract.clickAuditTrail();
        $$(".audit-log-contract-shared__user").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.textsInAnyOrder("arthur.khasanov+autotestcn@parleypro.com", "arthur.khasanov+ccn_at@parleypro.com", "arthur.khasanov+greg@parleypro.com"));
        Screenshoter.makeScreenshot();

        auditTrail.clickOk();
    }
}
