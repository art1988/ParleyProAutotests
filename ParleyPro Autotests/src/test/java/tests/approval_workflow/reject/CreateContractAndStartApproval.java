package tests.approval_workflow.reject;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ConfirmApprovers;
import forms.ContractInformation;
import forms.StartReview;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractAndStartApproval
{
    private static Logger logger = Logger.getLogger(CreateContractAndStartApproval.class);

    @Test(priority = 1)
    public void createContractThatMatchApprovalWorkflow()
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Approval rejection contract");
        contractInformationForm.setContractCurrency("GBP");
        contractInformationForm.setContractValue("1200"); // in appropriate range between [250, 1300]
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department2"); // correct department
        contractInformationForm.setContractCategory("category2");        // correct category
        contractInformationForm.setContractType("type2");                // correct type

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickUploadMyTeamDocuments( Const.DOCUMENT_LIFECYCLE_SAMPLE );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"PRAMATA\")')");
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

    @Test(priority = 3)
    public void startPreNegotiateApproval() throws InterruptedException
    {
        ConfirmApprovers confirmApproversForm = new OpenedContract().switchDocumentToPreNegotiateApproval("pramata");

        confirmApproversForm.deleteApprover("TEAM_3");
        confirmApproversForm.deleteApprover("Internal user1");
        confirmApproversForm.clickStartApproval();

        $(".header-users .user").waitUntil(Condition.appear, 25_000); // wait until users icons will appear
        Thread.sleep(2_000);

        logger.info("Assert that status was changed to APPROVAL for both contract and document...");
        $$(".lifecycle__item.active").first().waitUntil(Condition.exactText("APPROVAL\n(1)"), 7_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.exactText("APPROVAL"), 7_000);

        Screenshoter.makeScreenshot();

        new DashboardPage().getSideBar().logout(); // Logout
    }
}
