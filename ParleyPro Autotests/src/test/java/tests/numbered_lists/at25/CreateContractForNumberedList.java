package tests.numbered_lists.at25;

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
public class CreateContractForNumberedList
{
    private static Logger logger = Logger.getLogger(CreateContractForNumberedList.class);

    @Test(priority = 0)
    @Description("Precondition: this test creates new contract and uploads CONTRACT_NUMBERED_LIST_SAMPLE document.")
    public void createContractForNumberedList()
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Contract numbered list autotest");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickUploadMyTeamDocuments(Const.CONTRACT_NUMBERED_LIST_SAMPLE);

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"L0_Number_Point_1\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Bullet_3\")')");

        // 3. Move to Review stage
        OpenedContract openedContract = new OpenedContract();

        StartReview startReviewForm = openedContract.switchDocumentToReview(FilenameUtils.removeExtension(Const.CONTRACT_NUMBERED_LIST_SAMPLE.getName()));

        startReviewForm.clickStart();

        // Wait until status was changed to REVIEW
        $$(".lifecycle__item.active").first().waitUntil(Condition.visible, 7_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.visible, 7_000);

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 1)
    @Description("This test checks that formatting of all numbered lists were saved after uploading")
    public void checkNumberingAfterUploading()
    {
        StringBuffer jsCode = new StringBuffer("var paragraphs = $('.document-paragraph__content-text p').filter((ind, el) => $(el).find('span:first-child[contenteditable=false]').length); ");
        jsCode.append("var items = []; ");
        jsCode.append("paragraphs.each((ind, paragraph) => { ");
        jsCode.append("var parapgraphText = paragraph.innerText; ");
        jsCode.append("var numberingText = paragraph.children[0].innerText + paragraph.children[1].innerText; ");
        jsCode.append("items.push([paragraph.children[0].innerText, parapgraphText.replace(numberingText, '')]); }); ");
        jsCode.append("var string = items.map(item => item.join('|')).join(','); ");
        jsCode.append("return string;");

        String actual = Selenide.executeJavaScript(jsCode.toString());

        logger.info("Assert numbered lists formatting...");
        Assert.assertEquals(actual, "1.|L0_Number_Point_1,2.|L0_Number_Point_2,2.1.|L1_Number_Point_2_1," +
                "2.2.|L1_Number_Point_2_2,2.2.1.|L2_Number_Point_2_2_1,2.2.1.1.|L3_Number_Point_2_2_1_1," +
                "2.2.1.1.1.|L4_Number_Point_2_2_1_1_1,2.2.1.1.1.1.|L5_Number_Point_2_2_1_1_1_1," +
                "2.2.1.1.1.1.1.|L6_Number_Point_2_2_1_1_1_1_1,2.2.2.|L2_Number_Point_2_2_2," +
                "2.3.|L1_Number_Point_2_3,2.4.|L1_Number_Point_2_4,3.|L0_Number_Point_3,4.|L0_Number_Point_4," +
                "5.|L0_Number_Point_5,a.|L0_Letter_lowercase_a,b.|L0_Letter_lowercase_b,b.1.|L1_Letter_lowercase_b_1," +
                "c.|L0_Letter_lowercase_c,A.|L0_Letter_capital_A,B.|L0_Letter_capital_B,B.1.|L1_Letter_capital_B_1," +
                "C.|L0_Letter_capital_C,(a)|L0_Letter_braces_a,(b)|L0_Letter_braces_b,b.1.|L1_Letter_braces_b_1," +
                "(c)|L0_Letter_braces_c,I.|L0_Roman_capital_I,II.|L0_Roman_capital_II,II.1.|L1_Roman_capital_II_1," +
                "III.|L0_Roman_capital_III,i.|L0_Roman_lower_i,ii.|L0_Roman_lower_ii,ii.1.|L1_Roman_lower_ii_1," +
                "iii.|L0_Roman_lower_iii,•|L0_Bullet_1,•|L0_Bullet_2,o|L1_Bullet_2_1,•|L0_Bullet_3");
    }
}