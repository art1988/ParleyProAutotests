package tests.approval_workflow.at241;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckApprovalForDocxAndPDF
{
    private final String contractName = "at241 contract(Approval for docx and pdf)";

    private SideBar sideBar;
    private OpenedContract openedContract;
    private Discussions discussionBoard;

    private static Logger logger = Logger.getLogger(CheckApprovalForDocxAndPDF.class);


    @BeforeMethod
    public void addContractUploadDocx()
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformation = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle(contractName);
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
    public void checkApprovalForDocxAndPDF() throws InterruptedException
    {
        // docx
        addDiscussionForDocx();
        moveToApproveCheckPopupAndViewDiscussions();
        deleteDocx();

        // pdf
        uploadPDFAndAddDiscussion();
        switchPDFToApprove();
        loginAsApproverCheckPDFDiscussionAndApprove();
        loginBackAsCNAndCheckApprovalStatus();
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
        discussionBoard = openedContract.switchDocumentWithOpenDiscussionToPreNegotiateApproval("AT-14")
                                        .clickViewDiscussions("at241 contract(Approval for docx and pdf)");

        $$(".discussion-list .discussion2").shouldHave(CollectionCondition.size(1));
        $(".discussion-list .discussion2-header__row").shouldHave(Condition.text("Paragraph 2: Create comment here"));
        $(".discussion2-label__status").shouldHave(Condition.exactText("INTERNAL"));
        Screenshoter.makeScreenshot();
    }

    @Step
    public void deleteDocx() throws InterruptedException
    {
        openedContract = discussionBoard.clickDocumentsTab();

        openedContract.clickDocumentActionsMenu("AT-14").clickCancel().clickCancel();
        $(".cancelled").should(Condition.appear);
        openedContract.clickDocumentActionsMenu("AT-14").clickDelete().clickDelete();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text(" has been deleted."));
        Thread.sleep(1_000);
    }

    @Step
    public void uploadPDFAndAddDiscussion() throws InterruptedException
    {
        Thread.sleep(1_000);
        sideBar.clickInProgressContracts(false).selectContract(contractName);
        new AddDocuments().clickUploadMyTeamDocuments(new File(Const.PDF_DOCS_DIR + "/sample.pdf"));
        openedContract.switchDocumentToReview("sample").clickStart();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));
        openedContract.clickStartDiscussionForPDF().clickAddComment().setCommentForPDFDiscussion("comment for pdf discussion").clickPost();
        $(byText("comment for pdf discussion")).shouldBe(Condition.visible);
    }

    @Step
    public void switchPDFToApprove()
    {
        openedContract.switchDocumentToPreNegotiateApproval("sample").clickNext().clickStartApproval();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));
        $$(".contract-header-users__list .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("AL", "Ar")); // approver user icon was added to contract header
        logger.info("Wait until approver user icon is displayed in document header...");
        $$(".document__header-info .user").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText("Ar")); // approver user icon was added to document header
    }

    @Step
    public void loginAsApproverCheckPDFDiscussionAndApprove()
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail(Const.PREDEFINED_APPROVER_USER_1.getEmail());
        loginPage.setPassword(Const.PREDEFINED_APPROVER_USER_1.getPassword());

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract(contractName);
        $(byText("comment for pdf discussion")).shouldBe(Condition.visible);
        openedContract.clickApproveButton("sample").clickApproveButton();
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Document sample has been approved"));
        $$(".lifecycle__item.active .lifecycle__item-tick").shouldHave(CollectionCondition.size(2)); // 2 checkmarks were shown near APPROVAL stage
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));

        logger.info("Wait until checkmark near user icon in document header appear...");
        $(".document__header .user .user-icon-checked").should(Condition.appear); // checkmarks near user icon in document header should appear

        Screenshoter.makeScreenshot();
    }

    @Step
    public void loginBackAsCNAndCheckApprovalStatus()
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());
        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract(contractName);
        openedContract = new OpenedContract();
        $$(".lifecycle__item.active .lifecycle__item-tick").shouldHave(CollectionCondition.size(2)); // 2 checkmarks were shown near APPROVAL stage
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));

        logger.info("Wait until checkmark near user icon in document header appear...");
        $(".document__header .user .user-icon-checked").should(Condition.appear); // checkmarks near user icon in document header should appear

        $(byText("comment for pdf discussion")).shouldBe(Condition.visible);
        $(withText("A Simple PDF File")).shouldBe(Condition.visible);
        $(withText("And more text.")).shouldBe(Condition.visible);
        $(withText("The end, and just as well.")).shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();
    }

    @AfterMethod
    public void removeWorkflow()
    {
        String wrkflowName = "AT-241 workflow";

        sideBar.clickAdministration().clickWorkflowsTab().clickActionMenu(wrkflowName).clickDelete().clickDelete();
        $(byText(wrkflowName)).shouldNotBe(Condition.visible);
    }
}
