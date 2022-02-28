package tests.regression.at234;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.AcceptTypes;
import constants.Const;
import forms.ContractInformation;
import forms.ManageDiscussions;
import io.qameta.allure.Step;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckManageDiscussionsButton
{
    private SideBar sideBar;
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(CheckManageDiscussionsButton.class);


    @BeforeMethod
    public void uploadDocAndMoveToReview()
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformation = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("AT-234_ManageDiscussionsButton");
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
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
        openedContract.switchDocumentToReview(FilenameUtils.removeExtension(Const.DOCUMENT_DISCUSSIONS_SAMPLE.getName())).clickStart();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));
    }

    @Test
    public void checkManageDiscussionsButton() throws InterruptedException
    {
        addTwoDiscussions();
        checkDiscussionsOnDiscussionBoard();
        acceptViaManageDiscussionsButton();
    }

    @Step("Add two discussions and accept one")
    public void addTwoDiscussions() throws InterruptedException
    {
        logger.info("Delete Paragraph 1 to initiate discussion...");
        openedContract.clickByParagraph("Paragraph 1").deleteAllText().clickPost();
        $$(".document__body-content .discussion-indicator").shouldHave(CollectionCondition.size(1));

        logger.info("Add some text for Paragraph 2 to initiate discussion...");
        openedContract.clickByParagraph("Paragraph 2").setText(" some additional text").clickPost();
        $$(".document__body-content .discussion-indicator").shouldHave(CollectionCondition.size(2));

        logger.info("Accepting discussion for Paragraph 1...");
        openedContract.hover("Paragraph 1").clickAcceptChangesOnParagraph(AcceptTypes.ACCEPT).clickAcceptText();
        $$(".document__header .discussion-indicator").shouldHave(CollectionCondition.size(1));
    }

    @Step("Open discussion board and check that there are 2 discussions, one is opened and another is closed; Manage discussions button is visible")
    public void checkDiscussionsOnDiscussionBoard()
    {
        openedContract.clickByDiscussions();

        $$(".discussion-list .discussion2").shouldHave(CollectionCondition.size(2))
                                                    .shouldHave(CollectionCondition.textsInAnyOrder("Paragraph 2", "Delete section"));

        $$(".discussion2-label__status").shouldHave(CollectionCondition.size(2))
                                                 .shouldHave(CollectionCondition.textsInAnyOrder("INTERNAL", "ACCEPTED"));

        $$(".discussion2-label_is-closed").shouldHave(CollectionCondition.size(1));
        $(".discussion2-label_is-closed").closest(".discussion2-header__row").find("span[title]").shouldHave(Condition.text("Delete section"));

        $(".contract-header__manage-discussions").shouldBe(Condition.visible, Condition.enabled);

        Screenshoter.makeScreenshot();
    }

    @Step("Accept one opened discussion via Manage Discussions form => Manage Discussions button will disappear")
    public void acceptViaManageDiscussionsButton()
    {
        ManageDiscussions manageDiscussions = openedContract.clickManageDiscussions();

        $$(".manage-discussions-section__title").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.text("1 internal in Review"));
        manageDiscussions.acceptInternalDiscussions().confirmAccept().clickDone();

        $$(".discussion2-label_is-closed").shouldHave(CollectionCondition.size(2))
                                                   .shouldHave(CollectionCondition.textsInAnyOrder("INTERNAL ACCEPTED", "INTERNAL ACCEPTED"));

        $(".contract-header__manage-discussions").should(Condition.disappear);

        logger.info("Force page to reload...");
        Selenide.refresh();

        logger.info("Assert that 2 closed discussions are still in the list...");
        $$(".discussion2-label_is-closed").shouldHave(CollectionCondition.size(2))
                                                   .shouldHave(CollectionCondition.textsInAnyOrder("INTERNAL ACCEPTED", "INTERNAL ACCEPTED"));

        Screenshoter.makeScreenshot();
    }
}
