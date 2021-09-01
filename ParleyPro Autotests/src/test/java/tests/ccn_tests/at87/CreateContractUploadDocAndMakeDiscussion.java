package tests.ccn_tests.at87;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import forms.EmailWillBeSentToTheCounterparty;
import forms.StartNegotiation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractUploadDocAndMakeDiscussion
{
    private String contractName = "CCN: READY FOR SIGNATURE button contract";
    private String counterpartyOrganization = "CounterpartyAT";
    private Logger logger = Logger.getLogger(CreateContractUploadDocAndMakeDiscussion.class);

    @Test(priority = 1)
    public void addNewContractAndUploadDoc()
    {
        // add new contract
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle( contractName );
        contractInformationForm.setCounterpartyOrganization( counterpartyOrganization );
        contractInformationForm.setCounterpartyChiefNegotiator( Const.PREDEFINED_CCN.getEmail() );
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // upload doc
        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Unused extra\")')");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void moveDocToNegotiate()
    {
        StartNegotiation startNegotiation = new OpenedContract().switchDocumentToNegotiate("AT-14", counterpartyOrganization, false);
        EmailWillBeSentToTheCounterparty emailWillBeSentToTheCounterparty = startNegotiation.clickNext(false);
        $(".select__loading").waitUntil(Condition.disappear, 7_000);

        logger.info("Assert that Counterparty organization and CCN email were fetched from contract info...");
        Assert.assertEquals(emailWillBeSentToTheCounterparty.getCounterpartyOrganization(), counterpartyOrganization);
        Assert.assertEquals(emailWillBeSentToTheCounterparty.getCounterpartyChiefNegotiator(), "CCN AT fn CCN AT ln (" + Const.PREDEFINED_CCN.getEmail() + ")");

        emailWillBeSentToTheCounterparty.clickStart();

        Screenshoter.makeScreenshot();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract " + contractName + " is now visible to the Counterparty. The email was sent to " + Const.PREDEFINED_CCN.getEmail()));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);
    }

    @Test(priority = 3)
    @Description("This test adds one discussion and makes it external.")
    public void makeExternalDiscussion() throws InterruptedException
    {
        String addedParagraphText = "Just new paragraph to initiate discussion.";

        new OpenedContract().hover("Unused extra")
                            .clickAddParagraphAbove()
                            .setText(addedParagraphText)
                            .setComment("new p above p#6")
                            .selectExternal()
                            .clickPost(addedParagraphText, counterpartyOrganization).clickPostExternally();

        logger.info("Assert post notification and discussion counter...");
        $(".notification-stack").waitUntil(Condition.visible, 45_000).shouldHave(Condition.exactText("External discussion " + addedParagraphText + " has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 55_000);

        Assert.assertEquals(new OpenedContract().getAmountOfContractDiscussion(), "1");

        Screenshoter.makeScreenshot();
    }
}
