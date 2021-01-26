package tests.regression.at70;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import forms.StartReview;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContract
{
    private static Logger logger = Logger.getLogger(CreateContract.class);

    @Test(priority = 1)
    public void createContractAndUploadDocument()
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Paste two paragraphs");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickUploadMyTeamDocuments( Const.REGRESSION_DOC_AT70 );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Simple numbered one\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Simple numbered three\")')");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void moveToReview()
    {
        OpenedContract openedContract = new OpenedContract();

        StartReview startReviewForm = openedContract.switchDocumentToReview("Numbered_manual testing MAIN short");

        startReviewForm.clickStart();

        // Wait until status was changed to REVIEW
        $$(".lifecycle__item.active").first().waitUntil(Condition.visible, 7_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.visible, 7_000);

        logger.info("Assert that status was changed to REVIEW...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        logger.info("Assert that both contract and document icons have green color");
        $$(".lifecycle__item.active").first().getCssValue("background-color").equals("#19be9b;");
        $$(".lifecycle__item.active").last().getCssValue("background-color").equals("#19be9b;");

        Screenshoter.makeScreenshot();
    }
}
