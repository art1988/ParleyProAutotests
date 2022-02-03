package tests.regression.at134;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import forms.ShareForm;
import forms.StartReview;
import org.apache.commons.io.FilenameUtils;
import org.testng.annotations.Test;
import pages.*;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$$;


public class UploadDocAndShare
{
    @Test(priority = 1)
    public void createContract()
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Share me");
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

        // 3. Move to Review stage
        StartReview startReviewForm = new OpenedContract().switchDocumentToReview(FilenameUtils.removeExtension(Const.DOCUMENT_DISCUSSIONS_SAMPLE.getName()));
        startReviewForm.clickStart();

        // Wait until status was changed to REVIEW
        $$(".lifecycle__item.active").first().waitUntil(Condition.exactText("REVIEW\n(1)"), 7_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.exactText("REVIEW"), 7_000);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void shareWithUserGreg() throws InterruptedException
    {
        ShareForm shareForm = new OpenedContract().clickSHARE("AT-14");

        shareForm.addParticipant( Const.USER_GREG.getFirstName() )
                 .changeRoleOfInternalUser( Const.USER_GREG.getFirstName() )
                 .setLeadRole();

        shareForm.clickSend();

        Thread.sleep(1_000);
        Screenshoter.makeScreenshot();

        // Logout as my team CN
        LoginPage loginPage = new DashboardPage().getSideBar().logout();
    }
}
