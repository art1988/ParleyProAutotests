package tests.numbered_lists.at53;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import forms.StartReview;
import io.qameta.allure.Description;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$$;


public class CreateContractAT53
{
    private static Logger logger = Logger.getLogger(CreateContractAT53.class);

    @Test(priority = 1)
    @Description("This test creates new contract and uploads CONTRACT_NUMBERED_WITH_SUBLISTS document.")
    public void createContract()
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Contract numbered list with sub-lists");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_NUMBERED_WITH_SUBLISTS );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"L0_Num_Point_1\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"L2_Bullet_3\")')");

        // 3. Move to Review stage
        StartReview startReviewForm = new OpenedContract().switchDocumentToReview(FilenameUtils.removeExtension(Const.DOCUMENT_NUMBERED_WITH_SUBLISTS.getName()));

        startReviewForm.clickStart();

        // Wait until status was changed to REVIEW
        $$(".lifecycle__item.active").first().waitUntil(Condition.visible, 7_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.visible, 7_000);

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void checkListsAfterUploading()
    {
        StringBuffer jsCode = new StringBuffer("var items = [];");
        jsCode.append("$('.document-paragraph__content-text p').each(");
        jsCode.append("function(i, paragraph) {");
        jsCode.append("items.push(paragraph.innerText.replace(/\\s+/, '')); } );");
        jsCode.append("items = items.filter(Boolean);");
        jsCode.append("return items.toString();");

        String actual = Selenide.executeJavaScript(jsCode.toString());

        logger.info("Assert numbered lists after uploading...");
        Assert.assertEquals(actual, "1.L0_Num_Point_1,2.L0_Num_Point_2,A.L1_Letter_capital_A,B.L1_Letter_capital_B," +
                "\uF0A7L2_Bullet_1,\uF0A7L2_Bullet_2,\uF0A7L2_Bullet_3,C.L1_Letter_capital_C,D.L1_Letter_capital_D,3.L0_Num_Point_3,4.L0_Num_Point_4");
    }
}
