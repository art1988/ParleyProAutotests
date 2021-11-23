package tests.find_and_replace.at143;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import pages.subelements.FindAndReplacePopup;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadDocAndCheck
{
    private OpenedContract openedContract;
    private static Logger logger = Logger.getLogger(UploadDocAndCheck.class);

    @Test(priority = 1)
    public void createContractAndUploadDoc()
    {
        // 1. + NEW CONTRACT
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle("Contr AT-143 // Find and Replace");
        contractInformationForm.setContractingRegion("region2");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity2");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category2");
        contractInformationForm.setContractType("type3");

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.REGRESSION_DOC_AT141 );

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        // Move to Review
        openedContract = new OpenedContract();
        openedContract.switchDocumentToReview("dummyAT141").clickStart();

        // Wait until status was changed to REVIEW
        $$(".lifecycle__item.active").first().waitUntil(Condition.exactText("REVIEW\n(1)"), 7_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.exactText("REVIEW"), 7_000);

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));
    }

    @Test(priority = 2)
    @Description("This test finds word 'dummy' replaces it with word 'test', checks Matches counter and final result on document board.")
    public void findAndReplaceAndCheck()
    {
        FindAndReplacePopup findAndReplacePopup = openedContract.clickFindAndReplaceButton("dummyAT141")
                                                                .clickReplaceTab()
                                                                .findInDocument("dummy")
                                                                .replaceWith("test");

        logger.info("Assert that Matches count is correct...");
        $("div[class^=\"styles__footer\"] div").waitUntil(Condition.text("Matches: 1"), 10_000);

        Screenshoter.makeScreenshot();

        findAndReplacePopup.clickReviewAndReplaceButton()
                           .clickSave();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("1 discussion has been created"));

        logger.info("Checking that replacement was correct...");
        $("del").waitUntil(Condition.visible, 30_000);
        $$("del").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("dummy"));
        $$("ins").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("test"));
        $$(".document__body-content .discussion-indicator").shouldHave(CollectionCondition.size(1)); // one discussion was created

        Screenshoter.makeScreenshot();
    }
}
