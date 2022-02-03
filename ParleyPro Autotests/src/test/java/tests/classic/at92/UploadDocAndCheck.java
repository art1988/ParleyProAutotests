package tests.classic.at92;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;


public class UploadDocAndCheck
{
    private String contractName = "Classic for AT-92";
    private String documentName = "Manufacturing_Agreement_draft_3";
    private static Logger logger = Logger.getLogger(UploadDocAndCheck.class);

    @Test(priority = 1)
    public void createContractAndUploadDoc()
    {
        // add contract
        ContractInformation contractInformationForm = new InProgressContractsPage(true).clickNewContractButton();

        contractInformationForm.setContractTitle(contractName);
        contractInformationForm.checkClassicNegotiationModeCheckbox();
        contractInformationForm.setCounterpartyOrganization("CounterpartyAT");
        contractInformationForm.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // upload doc
        new AddDocuments().clickUploadMyTeamDocuments( Const.CLASSIC_AT92_V1 );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Production of Products\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"56.\")')");

        $(".notification-stack").waitUntil(Condition.appear, 6_000).shouldHave(Condition.exactText("Document " + documentName + " has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void uploadNewVer()
    {
        // Move to Negotiate
        new OpenedContract().switchDocumentToNegotiate(documentName, "CounterpartyAT", true).clickStart();

        $("#UPLOAD_VERSION_DOCUMENT").waitUntil(Condition.visible, 10_000).waitUntil(Condition.enabled, 10_000);

        new OpenedContract().clickUploadNewVersionButton(documentName)
                            .clickUploadCounterpartyDocument( Const.CLASSIC_AT92_V2, documentName, contractName )
                            .clickUpload(true)
                            .clickDocumentsTab();
    }

    @Test(priority = 3)
    @Description("This test checks that item '7. Test add' was added, item 'b.' under 8 was deleted and item 'b.' under 9 was added.")
    public void checkDocument() throws InterruptedException
    {
        Thread.sleep(1_000);

        logger.info("Scroll to item 5. ...");
        Selenide.executeJavaScript("$('p span:contains(\"5.\")').filter(function() { return $(this).text() === '5.'; })[0].scrollIntoView({});");

        logger.info("Assert that 7. item was added...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('p ins span:contains(\"Test add\")').parent().parent().text().replace(/\\s/g, '')"),
                "7.Testadd", "Looks like that 7. item wasn't added !!!");

        Screenshoter.makeScreenshot();

        logger.info("Scroll to item 8. ...");
        Selenide.executeJavaScript("$('p span:contains(\"8.\")').filter(function() { return $(this).text() === '8.'; })[0].scrollIntoView({});");

        logger.info("Assert that b. item was deleted...");

        Assert.assertTrue(Selenide.executeJavaScript("return $('p del span:contains(\"b.\")').length === 1"), "Looks like that b. item wasn't deleted !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('p del span:contains(\"On request Manufacturer shall furnish ABC\")').length === 1"), "Looks like that b. item wasn't deleted !!!");
        Screenshoter.makeScreenshot();

        logger.info("Scroll to item 9. ...");
        Selenide.executeJavaScript("$('p span:contains(\"9.\")').filter(function() { return $(this).text() === '9.'; })[0].scrollIntoView({});");

        logger.info("Assert that b. item was added...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('p ins span:contains(\"b.\")').length === 1"), "Looks like that b. item wasn't added !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('p ins span:contains(\"to ensure that Manufacturer has complied\")').length === 1"), "Looks like that b. item wasn't added !!!");
        Screenshoter.makeScreenshot();
    }
}
