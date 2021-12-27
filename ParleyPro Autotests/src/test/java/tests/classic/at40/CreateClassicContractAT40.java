package tests.classic.at40;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import forms.EmailWillBeSentToTheCounterparty;
import forms.StartNegotiation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ ScreenShotOnFailListener.class})
public class CreateClassicContractAT40
{
    private static Logger logger = Logger.getLogger(CreateClassicContractAT40.class);

    @Test(priority = 1)
    @Description("Precondition: this test creates new contract with Classic negotiation mode")
    public void createClassicContractAT40()
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("AT40 Classic contract");
        contractInformationForm.checkClassicNegotiationModeCheckbox();
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_CLASSIC_AT40 );

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test moves document to negotiate")
    public void moveToNegotiate() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        StartNegotiation startNegotiationForm = openedContract.switchDocumentToNegotiate("AT-40", "", true);

        EmailWillBeSentToTheCounterparty emailWillBeSentToTheCounterpartyForm = startNegotiationForm.clickNext(true);

        Thread.sleep(1_000);
        emailWillBeSentToTheCounterpartyForm.setCounterpartyOrganization("CounterpartyAT");
        Thread.sleep(1_000);
        emailWillBeSentToTheCounterpartyForm.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");

        emailWillBeSentToTheCounterpartyForm.clickStart();

        logger.info("Assert visible to the counterparty notification...");
        $(".notification-stack").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Contract AT40 Classic contract is now in negotiation. No notification was sent to the Counterparty."));

        logger.info("Assert that status was changed to NEGOTIATE...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        logger.info("Assert that WITH MY TEAM label is visible...");
        $(".label_theme_orange").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("WITH MY TEAM"));

        Screenshoter.makeScreenshot();
    }
}
