package tests.regression.at161;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AddContractFromTemplateAndCheck
{
    private static OpenedContract openedContract;
    private static SoftAssert softAssert = new SoftAssert();
    private static Logger logger = Logger.getLogger(AddContractFromTemplateAndCheck.class);

    @Test(priority = 1)
    public void addContractFromTemplate() throws InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("AT-161 ctr");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickSelectTemplateTab().selectTemplate("TemplateDOCXCapital_AT-161");
        $(".spinner").waitUntil(Condition.disappear, 60_000 * 2);

        $(".notification-stack").waitUntil(Condition.appear, 60_000).shouldHave(Condition.text("has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 75_000);
        openedContract = new OpenedContract();

        logger.info("Assert that contract is in expanded state...");
        softAssert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"CONTRACT AGREEMENT\")').text().trim()"), "CONTRACT AGREEMENT", "Caption 'CONTRACT AGREEMENT' is not on the page !!!");
        softAssert.assertEquals((long) Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"MADE this 1st day of July 2021\")').length"), 1l, "Text 'MADE this 1st day of July 2021' is not on the page !!!");
        softAssert.assertEquals((long) Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"NOW, THEREFORE\")').length"), 2l, "Text 'NOW, THEREFORE' is not on the page !!!");

        softAssert.assertAll();
        Screenshoter.makeScreenshot();

        collapseAndCheck();

        expandAndCheck();
    }

    @Test(priority = 2)
    public void moveToNegotiateAndCheck() throws InterruptedException
    {
        openedContract.switchDocumentToNegotiate("TemplateDOCXCapital_AT-161", "", false)
                      .clickNext(false)
                      .setCounterpartyOrganization("CounterpartyAT")
                      .setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com")
                      .clickStart();

        $(".notification-stack").waitUntil(Condition.appear, 35_000).shouldHave(Condition.text("is now visible to the Counterparty"));
        $(".notification-stack").waitUntil(Condition.disappear, 55_000);

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        collapseAndCheck();

        expandAndCheck();
    }

    private void collapseAndCheck() throws InterruptedException
    {
        logger.info("Collapse document...");
        $(".document__header-row .expand-collapse").click();
        Thread.sleep(1_000);

        logger.info("Making sure that no text is visible...");
        softAssert.assertFalse(Selenide.executeJavaScript("return $('.document__body-content').is(\":visible\");"));

        softAssert.assertAll();
        Screenshoter.makeScreenshot();
    }

    private void expandAndCheck() throws InterruptedException
    {
        logger.info("Expand document...");
        $(".document__header-row .expand-collapse").click();
        Thread.sleep(1_000);

        logger.info("Making sure that text is visible again...");
        softAssert.assertEquals(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"CONTRACT AGREEMENT\")').text().trim()"), "CONTRACT AGREEMENT", "Caption 'CONTRACT AGREEMENT' is not on the page !!!");
        softAssert.assertEquals((long) Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"MADE this 1st day of July 2021\")').length"), 1l, "Text 'MADE this 1st day of July 2021' is not on the page !!!");
        softAssert.assertEquals((long) Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"NOW, THEREFORE\")').length"), 2l, "Text 'NOW, THEREFORE' is not on the page !!!");

        softAssert.assertAll();
        Screenshoter.makeScreenshot();
    }
}
