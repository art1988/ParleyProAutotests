package tests.approval_workflow;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import forms.StartReview;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ ScreenShotOnFailListener.class})
public class CreateContractPositiveForApprovalWorkflow
{
    private static Logger logger = Logger.getLogger(CreateContractPositiveForApprovalWorkflow.class);

    @Test(priority = 1)
    @Description("This test creates contract that satisfies Approval Workflow settings and verifies that APPROVE stage is available")
    public void createContractPositiveForApprovalWorkflow()
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Approval workflow positive");
        contractInformationForm.setContractCurrency("GBP");
        contractInformationForm.setContractValue("855"); // in appropriate range between [250, 1300]
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");            // correct entity
        contractInformationForm.setContractingDepartment("department2"); // correct department
        contractInformationForm.setContractCategory("category2");        // correct category
        contractInformationForm.setContractType("type2");                // correct type

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickUploadMyTeamDocuments( Const.DOCUMENT_LIFECYCLE_SAMPLE );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"PRAMATA\")')");

        logger.info("Assert upload notification...");
        $(".notification-stack").waitUntil(Condition.visible, 25_000).shouldHave(Condition.exactText("Document pramata has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        logger.info("Assert that _contract_ header have APPROVE option...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.contract-header__status .lifecycle').text()"), "DRAFT(1)REVIEWAPPROVALNEGOTIATEAPPROVALSIGNMANAGED");

        // hover on DRAFT to show all available stages...
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"pramata\")').parent().parent().parent().next().find('.lifecycle')";
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString+ ".find(\"div:contains('REVIEW')\")");

        logger.info("Assert that _document_ header have APPROVE option...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document__header-info .lifecycle').text()"), "DRAFTREVIEWAPPROVALAPPROVAL");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test moves document to review stage")
    public void moveToReview()
    {
        OpenedContract openedContract = new OpenedContract();

        StartReview startReviewForm = openedContract.switchDocumentToReview("pramata");

        startReviewForm.clickStart();

        // Wait until status was changed to REVIEW
        $$(".lifecycle__item.active").first().waitUntil(Condition.visible, 14_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.visible, 14_000);

        logger.info("Assert that status was changed to REVIEW...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        Screenshoter.makeScreenshot();
    }
}
