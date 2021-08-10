package tests.regression.at163;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInNegotiation;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UploadDocAndCheck
{
    private static Logger logger = Logger.getLogger(UploadDocAndCheck.class);

    @Test(priority = 1)
    public void createContractAndUploadDoc() throws InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("CTR_AT163");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadCounterpartyDocuments(Const.REGRESSION_DOC_AT163);

        $(".notification-stack").waitUntil(Condition.visible, 45_000).shouldHave(Condition.text("1 unsupported formatting attributes")).find(".notification__close").click();
        new ContractInNegotiation("CTR_AT163").clickOk();

        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Cover Page\")')");

        logger.info("Doc is in expanded state. Check some arbitrary text...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Acorns Agreement\")').text().trim()"), "Acorns Agreement - May 2021", "There is no 'Acorns Agreement - May 2021' text on the page !!!");
        $$(".document-paragraph__content-text table").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(10)); // check presence of table
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Advertising and Promotional\")').text().trim()"), "Advertising and Promotional Services Agreement", "There is no 'Advertising and Promotional Services Agreement' text on the page !!!");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Appointment\")').text().trim()"), "1. Appointment", "There is no 'Appointment' text on the page !!!");

        logger.info("Collapse doc...");
        $(".document__header-row .expand-collapse").click();
        Thread.sleep(1_000);
        logger.info("Making sure that no text is visible...");
        Assert.assertFalse(Selenide.executeJavaScript("return $('.document__body-content').is(\":visible\");"));
        Screenshoter.makeScreenshot();

        logger.info("Expand doc...");
        $(".document__header-row .expand-collapse").click();
        Thread.sleep(1_000);

        logger.info("Making sure that text is visible again...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Acorns Agreement\")').text().trim()"), "Acorns Agreement - May 2021", "There is no 'Acorns Agreement - May 2021' text on the page !!!");
        $$(".document-paragraph__content-text table").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(10)); // check presence of table
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Advertising and Promotional\")').text().trim()"), "Advertising and Promotional Services Agreement", "There is no 'Advertising and Promotional Services Agreement' text on the page !!!");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"Appointment\")').text().trim()"), "1. Appointment", "There is no 'Appointment' text on the page !!!");
        Screenshoter.makeScreenshot();
    }
}
