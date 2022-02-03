package tests.regression.at211;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadDocExpandAndCheck
{
    private static Logger logger = Logger.getLogger(UploadDocExpandAndCheck.class);


    @Test
    public void uploadDocExpandAndCheck() throws InterruptedException
    {
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle("Resolve Marine_AT-211");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");
        contractInformationForm.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments(Const.DOC_AT211);
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        logger.info("Collapsing doc...");
        $(".document__header-row .expand-collapse").click();
        Thread.sleep(2_000);
        logger.info("Making sure that no text is visible...");
        Assert.assertFalse(Selenide.executeJavaScript("return $('.document__body-content').is(\":visible\");"));

        logger.info("Expand doc...");
        $(".document__header-row .expand-collapse").click();
        Thread.sleep(2_000);

        logger.info("Making sure that text is visible again...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"MARINE SERVICES AGREEMENT\")').text().trim()"), "MARINE SERVICES AGREEMENT", "There is no 'MARINE SERVICES AGREEMENT' text on the page !!!");
        String[] captions = {"Agreement", "Term", "Rates and Payment", "Relationship of the Parties", "Insurance"};
        ElementsCollection spans = $$(".document-paragraph__content-text span[style*='text-decoration:underline']").shouldHave(CollectionCondition.sizeGreaterThan(20));

        for( int i = 0; i < captions.length; i++ )
        {
            Assert.assertEquals(spans.get(i).getText(), captions[i], "There is no " + captions[i] + " text on the page !!!");
        }

        Assert.assertTrue(((String) Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Paying project\")').text().trim()")).contains("Paying project:"), "There is no 'Paying project' text on the page !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Authorized Signature\")').length === 2"), "There is no 'Authorized Signature' text on the page !!!");

        Screenshoter.makeScreenshot();
    }
}
