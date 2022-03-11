package tests.basics.at238;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class ContractCancellationWithCommentTest
{
    private SideBar sideBar;
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(ContractCancellationWithCommentTest.class);


    @BeforeMethod
    public void addContractUploadDocAdd3Discussions() throws InterruptedException
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformation = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("AT-223 Contract Cancellation");
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setCounterpartyChiefNegotiator( Const.PREDEFINED_CCN.getEmail() );
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        openedContract = new OpenedContract();
        openedContract.switchDocumentToNegotiate("AT-14", "CounterpartyAT", false).clickNext(false).clickStart();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        logger.info("Adding internal discussion...");
        openedContract.clickByParagraph("Paragraph 1").setComment("at238 - Comment only").selectInternal().clickPost();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Internal discussion"));

        logger.info("Adding queued discussion...");
        openedContract.clickByParagraph("Paragraph 2").setText(" at238 - Added text - queued").selectQueued().clickPost();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Queued discussion"));

        logger.info("Adding external discussion...");
        openedContract.clickByParagraph("Paragraph 3").setText(" ext").setComment("Comment external").selectExternal().clickPost("Paragraph 3: Insert something above me ext", "CounterpartyAT").clickPostExternally();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("External discussion"));
    }

    @Test
    public void contractCancellationWithCommentTest()
    {
        cancelContract();
        traverseThroughAllDiscussions();
        restartContract();
    }

    @Step("Cancel contract with message, check status")
    public void cancelContract()
    {
        openedContract.clickContractActionsMenu().clickCancelContract()
                                                 .checkLeaveCommentCheckbox().setMessage("This is a cancellation reason")
                                                 .clickCancelContract();

        logger.info("Assert that 'Cancelled' label was shown...");
        $(".contract-header__right .label").shouldHave(Condition.text("CANCELLED")); // in contract header
        $(".document__header-info .lifecycle").shouldHave(Condition.text("CANCELLED")); // in document header
        $(".contract-header__status .discussion-indicator").should(Condition.disappear); // No discussion counter in the Contract header
    }

    @Step("Traverse through all discussions, check status and content")
    public void traverseThroughAllDiscussions()
    {
        $$(".document__body-content .discussion-indicator").shouldHave(CollectionCondition.size(3));

        logger.info("Check first discussion...");
        openedContract.clickByDiscussionIconSoft("Paragraph 1");
        $$(".discussion2-post__comment p").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("at238 - Comment only"));
        $(".discussion-header .discussion2-label__status").shouldHave(Condition.text("INTERNAL CLOSED"));

        logger.info("Check second discussion...");
        openedContract.clickByDiscussionIconSoft("Paragraph 2");
        $$(".discussion2-post__patch p").shouldHave(CollectionCondition.size(1)).first().find("ins").shouldHave(Condition.text("at238 - Added text - queued"));
        $(".discussion-header .discussion2-label__status").shouldHave(Condition.text("INTERNAL CLOSED"));

        logger.info("Check third discussion...");
        openedContract.clickByDiscussionIconSoft("Paragraph 3");
        $$(".discussion2-post__patch p").shouldHave(CollectionCondition.size(1)).first().find("ins").shouldHave(Condition.text("ext"));
        $$(".discussion2-post .discussion2-post__comment p").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("Comment external"));
        $(".discussion-header .discussion2-label__status").shouldHave(Condition.text("EXTERNAL CLOSED"));
    }

    @Step("Restart contract, check status and amount of discussions")
    public void restartContract()
    {
        openedContract.clickContractActionsMenu().clickRestartContract().clickRestart();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Contract AT-223 Contract Cancellation has been restarted"));
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));
        $(".contract-header__status .discussion-indicator").should(Condition.appear);
        Assert.assertEquals(openedContract.getAmountOfContractDiscussion(), "3", "Amount of contract discussion is wrong !!!");

        logger.info("Check first discussion after restarting...");
        openedContract.clickByDiscussionIconSoft("Paragraph 1");
        $$(".discussion2-post__foot div").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("MAKE EXTERNAL", "MAKE QUEUED"));
    }
}
