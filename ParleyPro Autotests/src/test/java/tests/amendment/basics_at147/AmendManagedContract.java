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
public class AmendManagedContract
{
    private static Logger logger = Logger.getLogger(AmendManagedContract.class);
    private SoftAssert softAssert = new SoftAssert();

    @Test
    public void amendManagedContract() throws InterruptedException
    {
        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract("Executed Managed");

        ContractInformation contractInformation = new OpenedContract().clickContractActionsMenu().clickAmendContract();

        logger.info("Making sure that populated fields are the same as they were before...");
        softAssert.assertEquals(contractInformation.getContractTitle(), "Executed Managed-A", "Contract title is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractValue(), "440,000.00", "Contract value is wrong !!!");
        softAssert.assertEquals(contractInformation.getCounterpartyOrganization(), "CounterpartyAT", "Counterparty organization is wrong !!!");
        softAssert.assertTrue(contractInformation.getCounterpartyChiefNegotiator().contains("arthur.khasanov+cpat@parleypro.com"), "Counterparty Chief Negotiator is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractingRegion(), "region1", "Contract region is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractingCountry(), "country1", "Contract country is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractEntity(), "entity1", "Contract entity is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractingDepartment(), "department1", "Contract department is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractCategory(), "category1", "Contract category is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractType(), "type1", "Contract type is wrong !!!");
        softAssert.assertTrue(contractInformation.getChiefNegotiator().contains("arthur.khasanov+autotestcn@parleypro.com"), "Chief Negotiator is wrong !!!");
        contractInformation.getTags().shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("Tag_for_AMENDMENT #2"));
        softAssert.assertEquals(contractInformation.getNotes(), "Notes for amendment of managed.", "Notes are wrong !!!");

        logger.info("Checking Amendment link...");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('.js-linked-contracts-title:visible').text() === \"Linked contracts: 1\""), "Amount of linked contracts is wrong !!!");
        softAssert.assertEquals($(".js-linked-contracts-head").getText(), "Amendment to:\nExecuted Managed", "Linked contract is wrong !!!");
        softAssert.assertEquals($("div[class^=styles__content]").find("div[class^=styles__date]").getText(), "Effective date:\n12:00 AM");

        softAssert.assertAll();
        Screenshoter.makeScreenshot();

        logger.info("Setting new values...");
        contractInformation.setContractTitle("New val for Executed Managed-A");
        contractInformation.setContractValue("330000");
        contractInformation.setContractingRegion("region2");
        contractInformation.setContractingCountry("country2");
        contractInformation.setContractEntity("entity2");
        contractInformation.setContractingDepartment("department2");
        contractInformation.setContractCategory("category2");
        contractInformation.setContractType("type2");
        contractInformation.setTag("New tag for amendment_managed");
        contractInformation.setNotes("Additional notes for amendment_managed. ABC.");

        contractInformation.clickSaveFromAmendment();

        AddDocuments addDocuments = new AddDocuments();
        softAssert.assertEquals(addDocuments.getContractTitle(), "New val for Executed Managed-A", "Contract title is wrong !!!");
        softAssert.assertAll();

        logger.info("Making sure that Linked contracts icon was shown in header...");
        $(".linked-contracts-label").shouldBe(Condition.visible).shouldHave(Condition.exactText("link 1"));

        logger.info("Hover over Linked contracts icon...");
        $(".linked-contracts-label").hover();
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        $(".rc-tooltip-inner .js-linked-contracts-title").shouldHave(Condition.text("Linked contracts: 1"));
        $(".rc-tooltip-inner .js-linked-contracts-head").shouldHave(Condition.text("Amendment to:\nExecuted Managed"));
        $(".rc-tooltip-inner .js-linked-contracts-stage").shouldHave(Condition.text("Managed"));
        softAssert.assertEquals($(".rc-tooltip-inner").find("div[class^='styles__date']").getText(), "Effective date:\n12:00 AM");
        softAssert.assertAll();

        logger.info("Open Contract Info and check all fields...");
        contractInformation = new OpenedContract().clickContractInfo();
        Thread.sleep(4_000);

        softAssert.assertEquals(contractInformation.getContractValue(), "330,000.00", "Contract value is wrong !!!");
        softAssert.assertEquals(contractInformation.getCounterpartyOrganization(), "CounterpartyAT", "Counterparty organization is wrong !!!");
        softAssert.assertTrue(contractInformation.getCounterpartyChiefNegotiator().contains("arthur.khasanov+cpat@parleypro.com"), "Counterparty Chief Negotiator is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractingRegion(), "region2", "Contract region is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractingCountry(), "country2", "Contract country is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractEntity(), "entity2", "Contract entity is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractingDepartment(), "department2", "Contract department is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractCategory(), "category2", "Contract category is wrong !!!");
        softAssert.assertEquals(contractInformation.getContractType(), "type2", "Contract type is wrong !!!");
        softAssert.assertTrue(contractInformation.getChiefNegotiator().contains("arthur.khasanov+autotestcn@parleypro.com"), "Chief Negotiator is wrong !!!");
        contractInformation.getTags().shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("New tag for amendment_managed", "Tag_for_AMENDMENT #2"));
        softAssert.assertEquals(contractInformation.getNotes(), "Notes for amendment of managed.Additional notes for amendment_managed. ABC.", "Notes are wrong !!!");

        softAssert.assertAll();
        Screenshoter.makeScreenshot();

        contractInformation.clickCancel();
    }
}
