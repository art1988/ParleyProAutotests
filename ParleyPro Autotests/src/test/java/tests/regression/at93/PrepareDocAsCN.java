package tests.regression.at93;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class PrepareDocAsCN
{
    private static Logger logger = Logger.getLogger(PrepareDocAsCN.class);

    @Test(priority = 1)
    public void createContractAndUploadDoc()
    {
        // 1. + NEW CONTRACT
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle("Grey screen - CCN");
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setCounterpartyChiefNegotiator( Const.PREDEFINED_CCN.getEmail() );
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");
        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"delete me\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Unused extra\")')");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void startNegotiationAddDiscussionsAndCloseThem() throws InterruptedException
    {
        String paragraph1 = "Create comment",
               paragraph2 = "Unused extra";

        OpenedContract openedContract = new OpenedContract();

        openedContract.switchDocumentToNegotiate("AT-14", "CounterpartyAT", false)
                      .clickNext(false)
                      .clickStart();

        $(".notification-stack").waitUntil(Condition.visible, 15_000)
                .shouldHave(Condition.text("Contract Grey screen - CCN is now visible to the Counterparty."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        CKEditorActive ckEditorActive = openedContract.hover(paragraph1).clickAddComment();
        ckEditorActive.setComment("comment #1 to initiate discussion");
        ckEditorActive.clickPost();
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        ckEditorActive = openedContract.hover(paragraph2).clickAddComment();
        ckEditorActive.setComment("comment #2 to initiate discussion");
        ckEditorActive.clickPost();
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Closing discussion for paragraph 1...");
        openedContract.clickByDiscussionIcon(paragraph1).clickResolveDiscussion().clickResolveDiscussion();
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("Discussion Paragraph 2: Create comment here has been closed."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Closing discussion for paragraph 2...");
        openedContract.clickByDiscussionIcon(paragraph2).clickResolveDiscussion().clickResolveDiscussion();
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("Discussion Paragraph 7: Unused extra paragraph has been closed."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void logoutAsMyTeamCN()
    {
        new DashboardPage().getSideBar().logout();
    }
}
