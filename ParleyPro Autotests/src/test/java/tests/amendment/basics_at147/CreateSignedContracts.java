package tests.amendment.basics_at147;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.ContractInfo;
import pages.DashboardPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class CreateSignedContracts
{
    private static Logger logger = Logger.getLogger(CreateSignedContracts.class);

    @Test(priority = 1)
    @Description("This test creates executed contract. Post-execution tab is empty so that status become Signed.")
    public void makeExecutedSigned() throws InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("Executed Signed");
        contractInformation.setContractValue("550000");
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.setTag("Tag_for_AMENDMENT");
        contractInformation.setNotes("Notes for amendment...");
        contractInformation.clickSave();

        new AddDocuments().clickUploadExecutedDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("Document AT-14 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test creates Signed contract and fills Post-execution tab so that status become Managed.")
    public void makeExecutedManaged() throws InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(false).clickNewContractButton();

        contractInformation.setContractTitle("Executed Managed");
        contractInformation.setContractValue("440000");
        contractInformation.setCounterpartyOrganization("CounterpartyAT");
        contractInformation.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.setTag("Tag_for_AMENDMENT #2");
        contractInformation.setNotes("Notes for amendment of managed.");
        contractInformation.clickSave();

        new AddDocuments().clickUploadExecutedDocuments( Const.DOCUMENT_LIFECYCLE_SAMPLE );

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Document pramata has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        ContractInfo contractInfo = new ContractInfo();

        contractInfo.setSignatureDate();
        contractInfo.setEffectiveDate();
        contractInfo.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract has been updated."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test creates Signed contract and fills Post-execution tab with expired dates so that status become Expired.")
    public void makeExecutedExpired()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(false).clickNewContractButton();

        contractInformation.setContractTitle("Executed Expired");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments().clickUploadExecutedDocuments( Const.DOCUMENT_FORMATTING_SAMPLE );

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Document Formatting has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        ContractInfo contractInfo = new ContractInfo();
        contractInfo.setSignatureDate("May 31, 2021");
        contractInfo.setEffectiveDate("May 31, 2021");
        contractInfo.setExpirationDate("May 31, 2021");

        contractInfo.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract has been updated."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        logger.info("Assert that EXPIRED label was shown...");
        $(".label_theme_orange").waitUntil(Condition.visible, 10_000)
                                          .shouldBe(Condition.enabled)
                                          .shouldHave(Condition.exactText("EXPIRED"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    public void verifyContractsOnExecutedPage()
    {
        new DashboardPage().getSideBar().clickExecutedContracts(false);

        $$(".contract-status").shouldHave(CollectionCondition.size(3))
                                        .shouldHave(CollectionCondition.textsInAnyOrder("EXPIRED", "MANAGED", "SIGNED"));

        Screenshoter.makeScreenshot();
    }
}
