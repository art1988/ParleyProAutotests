package tests.customer_params.at254;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class ExecutedContractTest
{
    private static final String CONTRACT_NAME = "AT-254: Executed_COMBINE_ACTIVE_AND_UNFINISHED";

    private SideBar sideBar;
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(ExecutedContractTest.class);


    @Test
    public void executedContractTest() throws InterruptedException, FileNotFoundException
    {
        createExecutedContractAndUploadPDFAsExecuted();
        uploadDocAsMyTeamAndShareWithCCN();
        moveDocToSign();
    }

    @Step
    public void createExecutedContractAndUploadPDFAsExecuted()
    {
        sideBar = new DashboardPage().getSideBar();

        ContractInformation contractInformation = sideBar.clickExecutedContracts(true).clickNewContractButton();

        contractInformation.setContractTitle(CONTRACT_NAME);
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");

        contractInformation.clickSave();

        new AddDocuments().clickUploadExecutedTab().clickUploadExecutedDocuments(new File(Const.PDF_DOCS_DIR + "/At a Glance.pdf"));
        $(withText("Adobe")).shouldBe(Condition.visible);
        $(".label").shouldBe(Condition.visible).shouldHave(Condition.exactText("SIGNED COPY"));

        ContractInfo contractInfo = new ContractInfo();

        logger.info("Fill in post-execution data...");
        contractInfo.setSignatureDate();
        contractInfo.setEffectiveDate();
        contractInfo.clickSave();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract has been updated"));

        logger.info("Check MANAGED status...");
        $(".lifecycle__item.active").shouldBe(Condition.visible).shouldHave(Condition.exactText("MANAGED"));
    }

    @Step
    public void uploadDocAsMyTeamAndShareWithCCN() throws InterruptedException
    {
        $(withText("GO TO DOCUMENTS")).shouldBe(Condition.visible).click();

        openedContract = new OpenedContract();

        openedContract.clickNewDocument().clickUploadMyTeamDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Document AT-14 has been successfully uploaded"));
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(3))
                                              .shouldHave(CollectionCondition.textsInAnyOrder("DRAFT\n(1)", "MANAGED", "DRAFT"));

        logger.info("Move doc to Negotiate and share with CCN...");
        openedContract.switchDocumentToNegotiate("AT-14", "", false)
                      .clickNext(false)
                      .setCounterpartyOrganization("CounterpartyAT")
                      .setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com")
                      .clickStart();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("is now visible to the Counterparty"));

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2))
                                              .shouldHave(CollectionCondition.textsInAnyOrder("NEGOTIATE\n(1)", "NEGOTIATE"));

        $(".lifecycle__item.disabled.last.managed").shouldBe(Condition.visible).shouldHave(Condition.exactText("MANAGED")); // MANAGED status is grey
        Screenshoter.makeScreenshot();
    }

    @Step
    public void moveDocToSign() throws FileNotFoundException, InterruptedException
    {
        openedContract.switchDocumentToSign("AT-14", false).clickStart();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2))
                                              .shouldHave(CollectionCondition.textsInAnyOrder("SIGN\n(2)", "SIGN"));

        ContractInfo contractInfo = openedContract.clickCompleteSign("AT-14").clickComplete();

        $$(".lifecycle__item.done").shouldHave(CollectionCondition.size(2))
                                            .shouldHave(CollectionCondition.textsInAnyOrder("SIGN", "SIGN"));

        $(".modal-content").should(Condition.disappear);
        $(".spinner").should(Condition.disappear);
        Thread.sleep(2_000);

        logger.info("Updating post-execution...");
        contractInfo.setSignatureDate("Apr 2, 2025");
        contractInfo.clickSave();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract has been updated"));

        logger.info("Check MANAGED status of contract...");
        $(".lifecycle__item.active").shouldBe(Condition.visible).shouldHave(Condition.exactText("MANAGED"));
        Screenshoter.makeScreenshot();
    }
}
