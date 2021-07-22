package tests.amendment.at69;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.FieldType;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateAmendmentAndCheckFields
{
    private static Logger logger = Logger.getLogger(CreateAmendmentAndCheckFields.class);

    @Test
    public void createAmendmentAndCheckFields() throws InterruptedException
    {
        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract("at-69 Amendment");

        ContractInformation contractInformationAfterAmend = new OpenedContract().clickContractActionsMenu()
                                                                                .clickAmendContract();

        logger.info("Assert fields on Contract information after clicking of amendment...");
        Assert.assertEquals(contractInformationAfterAmend.getContractTitle(), "at-69 Amendment-A", "Wrong title of contract after amendment !!!");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.js-linked-contracts-title:visible').text()"), "Linked contracts: 1", "Wrong number of linked contracts after amendment !!!");
        Assert.assertEquals($(".js-linked-contracts-head").getText(), "Amendment to:\nat-69 Amendment", "Amendment to refers to wrong contract !!!");

        // Scroll to 'Contract category' field
        Selenide.executeJavaScript("$('.modal-content label span:contains(\"category\")')[0].scrollIntoView({})");

        contractInformationAfterAmend.chooseNewContractCategory("AmendmentTestcat"); // switching Category from 'CatTest' to 'AmendmentTestcat'
        $(".modal-content label[for='field1']").shouldBe(Condition.visible); // and other are visible too...

        ////
        logger.info("Assert that fields Field1 - ... - Field10 have values entered earlier; fields AmendField1 - ... - AmendField5 are empty...");
        for( int fIndex = 1; fIndex <= 10; fIndex++ )
        {
            Assert.assertEquals(Selenide.executeJavaScript("return $('.modal-content label[for=\"field" + fIndex + "\"]').parent().find('textarea').text()"), "f" + fIndex +" val",
                    "Value for field Field " + fIndex + " is wrong !!!");
            Thread.sleep(100);
        }

        for( int fIndex = 1; fIndex <= 5; fIndex++ )
        {
            Assert.assertEquals(Selenide.executeJavaScript("return $('.modal-content label[for=\"amendfld" + fIndex + "\"]').parent().find('textarea').text()"), "",
                    "Value for AmendField should be empty !!!");
            Thread.sleep(100);
        }
        Screenshoter.makeScreenshot();
        ////

        logger.info("Filling AmendField1 - ... - AmendField5 fields...");
        for( int i = 1; i <= 5; i++ )
        {
            contractInformationAfterAmend.setValueForCustomField("AmendFld" + i, FieldType.TEXT_AREA, "amend" + i + " val");
        }

        contractInformationAfterAmend.clickSave();
        Thread.sleep(5_000);

        ContractInformation contractInformation = new OpenedContract(true).clickContractInfo();

        // Scroll to 'Tags' field, i.e. to the very bottom
        Selenide.executeJavaScript("$('.modal-content label span:contains(\"Tags\")')[0].scrollIntoView({})");

        logger.info("Checking that all fields are filled...");
        for( int fIndex = 1; fIndex <= 5; fIndex++ )
        {
            Assert.assertEquals(Selenide.executeJavaScript("return $('.modal-content label[for=\"amendfld" + fIndex + "\"]').parent().find('textarea').text()"), "amend" + fIndex + " val",
                    "Fields AmendField are still empty but shouldn't !!!");
            Thread.sleep(100);
        }

        Screenshoter.makeScreenshot();

        contractInformation.clickCancel();
    }
}
