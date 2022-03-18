package tests.approval_workflow.at241;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.Discussions;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckApprovalForDocx
{
    private SideBar sideBar;
    private OpenedContract openedContract;
    private Discussions discussionBoard;


    @BeforeMethod
    public void addContractUploadDocx()
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformation = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("at241 contract(Approval for docx and pdf)");
        contractInformation.setContractValue("250000");
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
    }

    @Test
    public void checkApprovalForDocx() throws InterruptedException
    {
        // docx
        addDiscussionForDocx();
        moveToApproveCheckPopupAndViewDiscussions();
        deleteDocx();


    }

    @Step
    public void addDiscussionForDocx() throws InterruptedException
    {
        openedContract.switchDocumentToReview("AT-14").clickStart();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        openedContract.clickByParagraph("Paragraph 2").setText(". Added new text_at141").clickPost();
        $$(".discussion-indicator").shouldHave(CollectionCondition.size(3));
    }

    @Step
    public void moveToApproveCheckPopupAndViewDiscussions()
    {
        discussionBoard = openedContract.switchDocumentToPreNegotiateApproval("AT-14", true)
                                        .clickViewDiscussions("at241 contract(Approval for docx and pdf)");

        $$(".discussion-list .discussion2").shouldHave(CollectionCondition.size(1));
        $(".discussion-list .discussion2-header__row").shouldHave(Condition.text("Paragraph 2: Create comment here"));
        $(".discussion2-label__status").shouldHave(Condition.exactText("INTERNAL"));
    }

    @Step
    public void deleteDocx()
    {
        openedContract = discussionBoard.clickDocumentsTab();

        openedContract.clickDocumentActionsMenu("AT-14").clickCancel().clickCancel();
        $(".cancelled").should(Condition.appear);
        openedContract.clickDocumentActionsMenu("AT-14").clickDelete().clickDelete();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text(" has been deleted."));
    }

    @Step
    public void uploadPDFAndAddDiscussion()
    {

    }
}
