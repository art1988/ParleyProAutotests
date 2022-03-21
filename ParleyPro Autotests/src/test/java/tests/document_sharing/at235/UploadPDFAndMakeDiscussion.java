package tests.document_sharing.at235;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.OpenedDiscussionPDF;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadPDFAndMakeDiscussion
{
    private SideBar sideBar;
    private OpenedContract openedContract;
    private OpenedDiscussionPDF openedDiscussionPDF;

    private static final File PDF_DOC = new File(Const.PDF_DOCS_DIR + "/At a Glance.pdf");
    private static Logger logger = Logger.getLogger(UploadPDFAndMakeDiscussion.class);

    @BeforeMethod
    public void addContractAndUploadPdf()
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformation = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("AT-235_PDF_share");
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setCounterpartyChiefNegotiator(Const.PREDEFINED_CCN.getEmail());
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");

        contractInformation.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments( PDF_DOC );
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        openedContract = new OpenedContract();
        openedContract.switchDocumentToReview(FilenameUtils.removeExtension(PDF_DOC.getName())).clickStart();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));
    }

    @Test
    public void uploadPDFAndMakeDiscussion() throws InterruptedException
    {
        addInternalDiscussion();
        moveToNegotiateAndAddExternalDiscussion();

        sideBar.logout();
    }

    @Step("Add one internal discussion")
    public void addInternalDiscussion() throws InterruptedException
    {
        String commentToAdd = "PDF // discussion by CN";

        openedDiscussionPDF = openedContract.clickStartDiscussionForPDF();

        openedDiscussionPDF.clickAddComment()
                           .setCommentForPDFDiscussion(commentToAdd)
                           .clickPost();

        logger.info("Assert that internal discussion was added...");
        $$(".documents-pdf-discussion-post").shouldHave(CollectionCondition.size(1))
                .first().find(".documents-pdf-discussion-post__comment").shouldHave(Condition.text(commentToAdd));
        $(".documents-pdf-discussion-post").find(".documents-pdf-discussion-post__head .discussion2-label__status")
                .shouldHave(Condition.text("INTERNAL"));

        openedDiscussionPDF.closePanel();
        Thread.sleep(2_000);
    }

    @Step("Add one external discussion")
    public void moveToNegotiateAndAddExternalDiscussion() throws InterruptedException
    {
        openedContract.switchDocumentToNegotiate("At a Glance", "CounterpartyAT", false)
                      .clickNext(false)
                      .clickStart();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        Thread.sleep(3_000);

        openedDiscussionPDF.clickMakeExternal("PDF");
        $$(".documents-pdf-discussion-post__head-right .external").shouldHave(CollectionCondition.size(1));
    }
}
