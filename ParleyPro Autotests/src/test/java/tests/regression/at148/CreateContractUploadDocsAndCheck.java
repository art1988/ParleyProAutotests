package tests.regression.at148;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import forms.SendInvitation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractUploadDocsAndCheck
{
    private String contractTitle = "AT-148 contract";
    private String docName = "Manufacturing_Agreement_AT148_1";
    private OpenedContract openedContract;
    private static Logger logger = Logger.getLogger(CreateContractUploadDocsAndCheck.class);

    @Test(priority = 1)
    public void createContractAndUploadFirstDoc()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar()
                                                                     .clickInProgressContracts(true)
                                                                     .clickNewContractButton();

        contractInformation.setContractTitle(contractTitle);
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        contractInformation.checkClassicNegotiationModeCheckbox();
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments( Const.DOC_AT148_ONE );

        $(".notification-stack").waitUntil(Condition.appear, 20_000);
        $(".notification-stack").waitUntil(Condition.disappear, 20_000);
        new ContractInNegotiation(contractTitle).clickOk();

        openedContract = new OpenedContract();

        logger.info("Assert total amount of discussions...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "5", "Total amount of discussions is wrong !!! Should be 5 !");
        $$(".document__body .discussion-indicator.reviewing").shouldHave(CollectionCondition.size(5));

        openedContract.clickManageDiscussions()
                      .makeExternalAllInternalDiscussions()
                      .confirmAccept();

        new SendInvitation(contractTitle).clickStart();
        $(".notification-stack").waitUntil(Condition.appear, 20_000).shouldHave(Condition.text("is now in negotiation"));

        logger.info("Assert that contract and doc was switched to NEGOTIATE state...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        logger.info("Assert that amount of discussions is still 5...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "5", "Total amount of discussions is wrong !!! Should be 5 !");

        $$(".document__body .discussion-indicator.negotiating").shouldHave(CollectionCondition.size(5));
    }

    @Test(priority = 2)
    public void uploadSecondDoc()
    {
        openedContract.clickUploadNewVersionButton(docName)
                      .clickUploadCounterpartyDocument(Const.DOC_AT148_TWO, docName, contractTitle)
                      .clickUpload(true)
                      .clickDocumentsTab();

        logger.info("Assert that total number of discussions become 4...");
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "4", "Total amount of discussions is wrong !!! Should be 4 !");
    }
}
