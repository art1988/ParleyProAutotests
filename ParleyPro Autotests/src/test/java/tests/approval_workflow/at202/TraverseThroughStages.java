package tests.approval_workflow.at202;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import constants.Const;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import utils.ScreenShotOnFailListener;

import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class TraverseThroughStages
{
    private String docName = "AT-14";
    private OpenedContract openedContract;
    private Discussions discussionsTab;


    private static Logger logger = Logger.getLogger(TraverseThroughStages.class);

    @Test(priority = 1)
    public void addContractAndUploadDoc()
    {
        ContractInformation contractInformationForm = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformationForm.setContractTitle("AT-202 Traverse ctr");
        contractInformationForm.checkClassicNegotiationModeCheckbox();
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();


        AddDocuments addDocuments = new AddDocuments();
        addDocuments.clickUploadMyTeamDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        openedContract = new OpenedContract();
        discussionsTab = openedContract.clickByDiscussions();

        logger.info("Checking status on DISCUSSIONS tab[should be DRAFT]...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)"));
        $(byText("BACK TO DOCUMENTS")).click();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));
    }

    @Test(priority = 2)
    public void traverseThroughStages() throws FileNotFoundException
    {
        ////// Move document to Review stage
        {
            openedContract.switchDocumentToReview(docName).clickStart();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

            openedContract.clickByDiscussions();
            logger.info("Checking status on DISCUSSIONS tab[should be REVIEW]...");
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)"));
            $(byText("BACK TO DOCUMENTS")).click();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));
        }

        hideTooltip();

        ////// Move document to pre-negotiation approval stage
        {
            openedContract.switchDocumentToPreNegotiateApproval(docName).clickNext().clickStartApproval();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));

            openedContract.clickByDiscussions();
            logger.info("Checking status on DISCUSSIONS tab[should be APPROVAL]...");
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)"));
            $(byText("BACK TO DOCUMENTS")).click();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));
        }

        hideTooltip();

        ////// Click Approve button
        {
            openedContract.clickApproveButton(docName).clickApproveButton();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));
            $$(".lifecycle__item.active .lifecycle__item-tick").shouldHave(CollectionCondition.size(2)); // checkmarks are also present

            openedContract.clickByDiscussions();
            logger.info("Checking status on DISCUSSIONS tab[should be APPROVAL]...");
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)"));
            $$(".lifecycle__item.active .lifecycle__item-tick").shouldHave(CollectionCondition.size(1)); // checkmark is also present

            $(byText("BACK TO DOCUMENTS")).click();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));
        }

        hideTooltip();

        ////// Move document to negotiation stage
        {
            openedContract.switchDocumentToNegotiate(docName, "CounterpartyAT", true).clickStart();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

            openedContract.clickByDiscussions();
            logger.info("Checking status on DISCUSSIONS tab[should be NEGOTIATE]...");
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)"));

            $(byText("BACK TO DOCUMENTS")).click();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));
        }

        hideTooltip();

        ////// Move document to pre-signature approval stage.
        {
            openedContract.switchDocumentToPreSignApproval(docName).clickNext().clickStartApproval();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));

            openedContract.clickByDiscussions();
            logger.info("Checking status on DISCUSSIONS tab[should be APPROVAL]...");
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)"));

            $(byText("BACK TO DOCUMENTS")).click();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));
        }

        hideTooltip();

        ////// Click Approve button
        {
            openedContract.clickApproveButton(docName).clickApproveButton();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));
            $$(".lifecycle__item.active .lifecycle__item-tick").shouldHave(CollectionCondition.size(2)); // checkmarks are also present

            openedContract.clickByDiscussions();
            logger.info("Checking status on DISCUSSIONS tab[should be APPROVAL]...");
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)"));
            $$(".lifecycle__item.active .lifecycle__item-tick").shouldHave(CollectionCondition.size(1)); // checkmark is also present

            $(byText("BACK TO DOCUMENTS")).click();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));
        }

        hideTooltip();

        ////// Click Sign and initiate manual signature process
        {
            openedContract.switchDocumentToSign(docName, false).clickStart();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.exactTexts("SIGN\n(1)", "SIGN"));

            openedContract.clickByDiscussions();
            logger.info("Checking status on DISCUSSIONS tab[should be SIGN]...");

            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("SIGN\n(1)"));

            $(byText("BACK TO DOCUMENTS")).click();
            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("SIGN\n(1)", "SIGN"));
        }

        hideTooltip();

        ////// Complete signature
        {
            ContractInfo contractInfo = openedContract.clickCompleteSign(docName).clickComplete();

            contractInfo.setSignatureDate();
            contractInfo.setEffectiveDate();
            contractInfo.clickSave();

            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("MANAGED"));
            $(byText("GO TO DOCUMENTS")).click();

            openedContract.clickByDiscussions();
            logger.info("Checking status on DISCUSSIONS tab[should be MANAGED]...");

            $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("MANAGED"));
        }
    }

    private void hideTooltip()
    {
        SelenideElement tooltip = $(".rc-tooltip-inner");

        if( tooltip.isDisplayed() )
        {
            Selenide.executeJavaScript("$('.rc-tooltip-inner').hide()");
        }
    }
}
