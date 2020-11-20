package tests.routing_workflow;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import forms.StartReview;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreateContractPositiveForContractRoutingWorkflow
{
    private static Logger logger = Logger.getLogger(CreateContractPositiveForContractRoutingWorkflow.class);

    @Test(priority = 1)
    @Description("This test creates contract that satisfies Contract Routing Workflow settings")
    public void createContractPositiveForContractRoutingWorkflow() throws InterruptedException
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Contract routing workflow positive");
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

        // Hide active hover menu
        Selenide.executeJavaScript("$('.contract-user__section').hide()");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    @Description("")
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
    }
}
