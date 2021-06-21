package tests.amendment.basics_at147;

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

@Listeners({ScreenShotOnFailListener.class})
public class AmendSignedContract
{
    private static Logger logger = Logger.getLogger(AmendSignedContract.class);
    private SoftAssert softAssert = new SoftAssert();

    @Test(priority = 1)
    public void amendSignedContract()
    {
        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract("Executed Signed");

        ContractInformation contractInformation = new OpenedContract().clickContractActionsMenu().clickAmendContract();

        logger.info("Making sure that populated fields are the same as they were before...");
        softAssert.assertEquals(contractInformation.getContractTitle(), "Executed Signed-A", "Contract title is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractValue(), "550,000.00", "Contract value is wrong !!!");
        softAssert.assertEquals(contractInformation.getCounterpartyOrganization(), "CounterpartyAT", "Counterparty organization is wrong !!!");
        softAssert.assertTrue(contractInformation.getCounterpartyChiefNegotiator().contains("arthur.khasanov+cpat@parleypro.com"), "Counterparty Chief Negotiator is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractingRegion(), "region1", "Contract region is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractingCountry(), "country1", "Contract country is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractEntity(), "entity1", "Contract entity is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractingDepartment(), "department1", "Contract department is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractCategory(), "category1", "Contract category is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractType(), "type1", "Contract type is wrong !!!");
        softAssert.assertTrue(contractInformation.getChiefNegotiator().contains("arthur.khasanov+autotestcn@parleypro.com"), "Chief Negotiator is wrong !!!");
        contractInformation.getTags().shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("Tag_for_AMENDMENT"));
        softAssert.assertEquals(contractInformation.getNotes(), "Notes for amendment...", "Notes are wrong !!!");

        logger.info("Checking Amendment link...");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('.js-linked-contracts-title:visible').text() === \"Linked contracts: 1\""), "Amount of linked contracts is wrong !!!");
        softAssert.assertEquals($(".js-linked-contracts-head").getText(), "Amendment to:\nExecuted Signed", "Linked contract is wrong !!!");

        softAssert.assertAll();
        Screenshoter.makeScreenshot();

        contractInformation.clickSave();
        AddDocuments addDocuments = new AddDocuments();

        softAssert.assertEquals(addDocuments.getContractTitle(), "Executed Signed-A", "Contract title is wrong !!!");
        softAssert.assertAll();

        logger.info("Making sure that Linked contracts icon was shown in header...");
        $(".linked-contracts-label").shouldBe(Condition.visible).shouldHave(Condition.exactText("link 1"));

        logger.info("Hover over Linked contracts icon...");
        $(".linked-contracts-label").hover();
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        $(".rc-tooltip-inner .js-linked-contracts-title").shouldHave(Condition.text("Linked contracts: 1"));
        $(".rc-tooltip-inner .js-linked-contracts-head").shouldHave(Condition.text("Amendment to:\nExecuted Signed"));
        $(".rc-tooltip-inner .js-linked-contracts-stage").shouldHave(Condition.text("Signed"));

        logger.info("Open Contract Info and check fields...");
        contractInformation = new OpenedContract().clickContractInfo();
        softAssert.assertEquals(contractInformation.getContractValue(), "550,000.00", "Contract value is wrong !!!");
        softAssert.assertEquals(contractInformation.getCounterpartyOrganization(), "CounterpartyAT", "Counterparty organization is wrong !!!");
        softAssert.assertTrue(contractInformation.getCounterpartyChiefNegotiator().contains("arthur.khasanov+cpat@parleypro.com"), "Counterparty Chief Negotiator is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractingRegion(), "region1", "Contract region is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractingCountry(), "country1", "Contract country is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractEntity(), "entity1", "Contract entity is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractingDepartment(), "department1", "Contract department is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractCategory(), "category1", "Contract category is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractType(), "type1", "Contract type is wrong !!!");
        softAssert.assertTrue(contractInformation.getChiefNegotiator().contains("arthur.khasanov+autotestcn@parleypro.com"), "Chief Negotiator is wrong !!!");
        contractInformation.getTags().shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("Tag_for_AMENDMENT"));
        softAssert.assertEquals(contractInformation.getNotes(), "Notes for amendment...", "Notes are wrong !!!");

        logger.info("Checking Amendment link...");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('.js-linked-contracts-title:visible').text() === \"Linked contracts: 1\""), "Amount of linked contracts is wrong !!!");
        softAssert.assertEquals($(".js-linked-contracts-head").getText(), "Amendment to:\nExecuted Signed", "Linked contract is wrong !!!");

        softAssert.assertAll();
        Screenshoter.makeScreenshot();

        contractInformation.clickCancel();
    }
}
