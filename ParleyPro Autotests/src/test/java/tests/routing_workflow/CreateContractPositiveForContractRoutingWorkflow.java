package tests.routing_workflow;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import forms.StartReview;
import forms.workflows.ContractRoutingWorkflow;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import pages.subelements.CKEditorActive;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ ScreenShotOnFailListener.class})
public class CreateContractPositiveForContractRoutingWorkflow
{
    private static String contractName = "Contract routing workflow positive";
    private static Logger logger = Logger.getLogger(CreateContractPositiveForContractRoutingWorkflow.class);


    @Test(priority = 1)
    @Description("This test creates contract that satisfies Contract Routing Workflow settings")
    public void createContractPositiveForContractRoutingWorkflow() throws InterruptedException
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle(contractName);
        contractInformationForm.setContractCurrency("EUR");
        contractInformationForm.setContractValue("22450"); // in appropriate range between [15400, 32700]
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1"); // correct department
        contractInformationForm.setContractCategory("category1");        // correct category
        contractInformationForm.setContractType("type2");                // correct type

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickUploadMyTeamDocuments( Const.CONTRACT_DISCUSSIONS_SAMPLE );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"delete me\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Unused extra\")')");

        logger.info("Assert upload notification...");
        $(".notification-stack").waitUntil(Condition.visible, 25_000).shouldHave(Condition.exactText("Document AT-14 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);
    }

    @Test(priority = 2)
    @Description("This test moves document to review stage")
    public void moveToReview()
    {
        OpenedContract openedContract = new OpenedContract();

        StartReview startReviewForm = openedContract.switchDocumentToReview("AT-14");

        startReviewForm.clickStart();

        // Wait until status was changed to REVIEW
        $$(".lifecycle__item.active").first().waitUntil(Condition.visible, 14_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.visible, 14_000);

        logger.info("Assert that status was changed to REVIEW...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test verifies that Internal user1 was added to document and has reviewer role")
    public void checkThatUser1WasAddedAsReviewer()
    {
        logger.info("Assert that user icons are visible...");
        $(".header-users .user").waitUntil(Condition.appear, 25_000); // wait until users icons will appear
        $$(".header-users .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("a", "I"));

        logger.info("Assert that Internal user1 has Reviewer role...");

        // Hover over I user icon
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append("$('.header-users span:contains(\"I\")')[0].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());

        $(".contract-user__status").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Reviewer"));
        $(".contract-user__name").waitUntil(Condition.visible, 6_000)
                .shouldHave(Condition.exactText(Const.PREDEFINED_INTERNAL_USER_1.getFirstName() + " " + Const.PREDEFINED_INTERNAL_USER_1.getLastName()));

        Screenshoter.makeScreenshot();

        // Hide active hover menu
        Selenide.executeJavaScript("$('.contract-user__section').hide()");
    }

    @Test(priority = 4)
    @Description("This test verifies that Internal user2 was added to document after deletion of paragraph and has reviewer role")
    public void addDiscussionAndCheckThatUser2WasAdded() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        String paragraphTitle = "Paragraph 1: Hello, delete me please";
        CKEditorActive ckEditorActive = openedContract.hover(paragraphTitle).clickDelete();
        ckEditorActive.setComment("Just simple comment for test about contract routing workflow");
        ckEditorActive.clickPost();

        logger.info("Assert that internal discussion notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Internal discussion " + paragraphTitle + " has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that user icons are visible...");
        $(".header-users .user").waitUntil(Condition.appear, 25_000); // wait until users icons will appear
        $$(".header-users .user").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.exactTexts("A", "I", "I"));

        // Hover over second I user icon
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append("$('.header-users span:contains(\"I\")')[1].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());

        $(".contract-user__status").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Reviewer"));
        $(".contract-user__name").waitUntil(Condition.visible, 6_000)
                .shouldHave(Condition.exactText(Const.PREDEFINED_INTERNAL_USER_2.getFirstName() + " " + Const.PREDEFINED_INTERNAL_USER_2.getLastName()));

        Screenshoter.makeScreenshot();

        // Hide active hover menu
        Selenide.executeJavaScript("$('.contract-user__section').hide()");
    }

    @Test(priority = 5)
    @Description("This test uploads Counterparty document and verifies that Approval_User_2 was added to document and has Lead role")
    public void uploadCounterpartyDocument()
    {
        OpenedContract openedContract = new OpenedContract();

        AddDocuments addDocumentsPage = openedContract.clickNewDocument();

        // Upload Counterparty documents
        addDocumentsPage.clickUploadCounterpartyDocuments( Const.CONTRACT_FORMATTING_SAMPLE );

        logger.info("Assert upload notification...");
        $(".notification-stack").waitUntil(Condition.visible, 25_000).shouldHave(Condition.exactText("Document Formatting has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        logger.info("Assert that Contract 'Contract routing workflow positive' is now in Negotiation popup is visible...");
        new ContractInNegotiation(contractName).clickOk();

        logger.info("Assert that just uploaded document has 2 added user icons...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document__header-rename:contains(\"Formatting\")').parent().parent().parent().find(\".header-users .user\").text()"),
                "aA");

        // Hover over second I user icon
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append("$('.document__header-rename:contains(\"Formatting\")').parent().parent().parent().find(\".header-users .user\")[1].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());

        $(".contract-user__status").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Lead"));
        $(".contract-user__name").waitUntil(Condition.visible, 6_000)
                .shouldHave(Condition.exactText(Const.PREDEFINED_APPROVER_USER_2.getFirstName()));

        logger.info("Assert that document has 3RD PARTY mark...");
        $(".label.label_theme_dgray").shouldHave(Condition.exactText("3RD PARTY"));

        logger.info("Assert that button SEND INVITE is visible...");
        $("._button.scheme_blue.size_tier2").shouldHave(Condition.exactText("SEND INVITE"));

        Screenshoter.makeScreenshot();

        // Hide active hover menu
        Selenide.executeJavaScript("$('.contract-user__section').hide()");

        logger.info("Assert that contract header has 4 added users...");
        $$(".contract-header__status .user").shouldHave(CollectionCondition.size(4));
    }

    @Test(priority = 6)
    @Description("This test opens Audit trail and checks Document user assigned events")
    public void checkAuditTrailEvents()
    {
        OpenedContract openedContract = new OpenedContract();

        AuditTrail auditTrail = openedContract.clickAuditTrail();

        logger.info("Assert that there are 3 'Document user assigned' events...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.timeline-title:contains(\"Document user assigned\")').length"), Long.valueOf(3));
        Assert.assertEquals(Selenide.executeJavaScript("return $('.timeline-title:contains(\"Document user assigned\")').parent().parent().find('.timeline-body:contains(\"User\")').text()"),
                "User: arthur.khasanov+approval2@parleypro.comUser: arthur.khasanov+team2@parleypro.comUser: arthur.khasanov+team1@parleypro.com");

        Screenshoter.makeScreenshot();

        auditTrail.clickOk();
    }

    @Test(priority = 7)
    @Description("This test goes to workflow settings, removes 'Draft to review' event, change users in Text changed event, saves workflow and validate saving")
    public void checkEventRemovingInWorkflow() throws InterruptedException
    {
        DashboardPage dashboardPage = new DashboardPage();

        dashboardPage.getSideBar().clickAdministration().clickWorkflowsTab().clickActionMenu("Contract_routing_WFL_AT").clickEdit();

        ContractRoutingWorkflow contractRoutingWorkflow = new ContractRoutingWorkflow(true);

        contractRoutingWorkflow.deleteEvent("Draft to review");
        contractRoutingWorkflow.setTextChangedParticipant( Const.PREDEFINED_INTERNAL_USER_1.getFirstName() );
        contractRoutingWorkflow.clickSave();

        logger.info("Edit workflow again and assert that changes were applied...");
        dashboardPage.getSideBar().clickAdministration().clickWorkflowsTab().clickActionMenu("Contract_routing_WFL_AT").clickEdit();
        Thread.sleep(2_000);
        boolean draftToReviewEventExist = Selenide.executeJavaScript("return $('.workflows-autoassignment-events-event__title:contains(\"Draft to review\")').length === 1");
        // TODO: uncomment after fixing of PAR-12894
        // Assert.assertFalse(draftToReviewEventExist); // it should not exist
        String usersOfTextChangedEvent = Selenide.executeJavaScript("return $('.workflows-autoassignment-events-event__title:contains(\"Text changed\")').parent().parent().find(\".workflows-users-list .workflows-users-list__item-name\").text()");
        Assert.assertEquals(usersOfTextChangedEvent, "Internal user2 Internal user2 last nameInternal user1 Internal user1 last name");

        contractRoutingWorkflow.clickCancel();

        InProgressContractsPage inProgressContractsPage = dashboardPage.getSideBar().clickInProgressContracts(false);
        inProgressContractsPage.selectContract(contractName);
    }
}
