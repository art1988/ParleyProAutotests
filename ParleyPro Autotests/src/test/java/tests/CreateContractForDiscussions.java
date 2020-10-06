package tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import forms.StartReview;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$$;

/**
 * This single test prepares contract with one single document AT-14
 */
public class CreateContractForDiscussions
{
    @Test
    public void createContractForDiscussions()
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Contract discussions autotest");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickUploadMyTeamDocuments(Const.CONTRACT_DISCUSSIONS_SAMPLE);

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"delete me\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Unused extra\")')");

        // 3. Move to Review stage
        OpenedContract openedContract = new OpenedContract();

        StartReview startReviewForm = openedContract.switchDocumentToReview();

        startReviewForm.clickStart();

        // Wait until status was changed to REVIEW
        $$(".lifecycle__item.active").first().waitUntil(Condition.visible, 7_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.visible, 7_000);

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        Screenshoter.makeScreenshot();
    }
}
