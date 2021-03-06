package tests.basics.at8;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import forms.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.ExecutedContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ ScreenShotOnFailListener.class})
public class ProceedThroughLifecycleStages
{
    private final String DOCUMENT_NAME = FilenameUtils.removeExtension(Const.DOCUMENT_LIFECYCLE_SAMPLE.getName());


    private static Logger logger = Logger.getLogger(ProceedThroughLifecycleStages.class);

    @Test(priority = 1)
    public void switchToReview()
    {
        OpenedContract openedContract = new OpenedContract();

        StartReview startReviewForm = openedContract.switchDocumentToReview(DOCUMENT_NAME);

        startReviewForm.clickStart();

        // Wait until status was changed to REVIEW
        $$(".lifecycle__item.active").first().waitUntil(Condition.exactText("REVIEW\n(1)"), 7_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.exactText("REVIEW"), 7_000);

        logger.info("Assert that status was changed to REVIEW...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        logger.info("Assert that both contract and document icons have green color");
        $$(".lifecycle__item.active").first().getCssValue("background-color").equals("#19be9b;");
        $$(".lifecycle__item.active").last().getCssValue("background-color").equals("#19be9b;");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void switchToNegotiate()
    {
        OpenedContract openedContract = new OpenedContract();

        StartNegotiation startNegotiationForm = openedContract.switchDocumentToNegotiate(DOCUMENT_NAME, "CounterpartyAT", false);

        EmailWillBeSentToTheCounterparty emailWillBeSentToTheCounterpartyForm = startNegotiationForm.clickNext(false);
        emailWillBeSentToTheCounterpartyForm.clickStart();

        logger.info("Assert visible to the counterparty notification...");
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract Contract lifecycle autotest is now visible to the Counterparty. The email was sent to " + Const.PREDEFINED_USER_CN_ROLE.getEmail()));

        logger.info("Assert that status was changed to NEGOTIATE...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        logger.info("Assert that both contract and document icons have purple color...");
        $$(".lifecycle__item.active").first().getCssValue("background-color").equals("#7f6fcf;");
        $$(".lifecycle__item.active").last().getCssValue("background-color").equals("#7f6fcf;");

        logger.info("Assert that Counterparty user icon is present...");
        $(".contract-header-users__counterparty").shouldBe(Condition.visible);
        $(".header-users__counterparty").shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void switchToSign()
    {
        OpenedContract openedContract = new OpenedContract();

        SignContract signContractForm = openedContract.switchDocumentToSign(DOCUMENT_NAME, false);

        try
        {
            signContractForm.clickStart();

            Thread.sleep(5_000);
            logger.info("Assert that file was downloaded...");

            Assert.assertTrue(new WebDriverWait(WebDriverRunner.getWebDriver(), 20).
                    until(d -> Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), DOCUMENT_NAME + ".pdf").toFile().exists()));
        }
        catch (FileNotFoundException | InterruptedException e)
        {
            logger.error("FileNotFoundException", e);
        }

        // Wait until Complete Sign is visible...
        $("#COMPLETE_MANUAL_DOCUMENT").waitUntil(Condition.visible, 15_000);

        logger.info("Assert that status was changed to SIGN...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("SIGN\n(1)", "SIGN"));

        logger.info("Assert that both icons still have purple color...");
        $$(".lifecycle__item.active").first().getCssValue("background-color").equals("#7f6fcf;");
        $$(".lifecycle__item.active").last().getCssValue("background-color").equals("#7f6fcf;");

        logger.info("Assert that we see buttons COMPLETE SIGN and VOID SIGN...");
        $("#COMPLETE_MANUAL_DOCUMENT").shouldBe(Condition.visible).shouldHave(Condition.exactText("COMPLETE SIGN"));
        $("#VOID_DOCUMENT").shouldBe(Condition.visible).shouldHave(Condition.exactText("VOID SIGN"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    public void switchToManaged()
    {
        OpenedContract openedContract = new OpenedContract();

        CompleteSign completeSignForm = openedContract.clickCompleteSign(DOCUMENT_NAME);

        ContractInfo signContractInfo = completeSignForm.clickComplete();

        signContractInfo.setSignatureDate();
        signContractInfo.setEffectiveDate();
        signContractInfo.clickSave();

        logger.info("Assert update notification...");
        $(".notification-stack").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Contract has been updated."));

        logger.info("Assert that status was changed to MANAGED...");
        $(".lifecycle").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("MANAGED"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 5)
    public void verifyExecutedStatus()
    {
        ExecutedContractsPage executedContractsPage = new DashboardPage().getSideBar().clickExecutedContracts(false);

        executedContractsPage.search("Contract lifecycle autotest");

        logger.info("Assert that Found: 1 Executed contract...");
        $(".contracts-tabs").waitUntil(Condition.visible, 7_000);
        $(byText("1 Executed contract")).shouldBe(Condition.visible);

        logger.info("Assert that found contract has Managed stage...");
        $(".contracts-list__cell-stage").shouldBe(Condition.visible).shouldHave(Condition.exactText("MANAGED"));
    }

    @Test(priority = 6)
    public void addFieldInPostExecution() throws InterruptedException
    {
        ContractInfo contractInfo = new DashboardPage().getSideBar().clickExecutedContracts(false).selectContract("Contract lifecycle autotest");

        String newFieldsName  = "fieldPE_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime()),
               newFieldsValue = "post execution value for just added field";

        contractInfo.clickAddAField();
        contractInfo.setNewFieldsName(newFieldsName);
        contractInfo.setNewFieldsValue(newFieldsValue);
        contractInfo.clickSave();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract has been updated"));

        logger.info("Revisit managed contract and check that field has been saved...");
        contractInfo = new DashboardPage().getSideBar().clickExecutedContracts(false).selectContract("Contract lifecycle autotest");

        $(byText("Title")).shouldBe(Condition.visible);
        $(byText("Value")).shouldBe(Condition.visible);
        Thread.sleep(2_000);
        Assert.assertEquals(contractInfo.getNewFieldsName(), newFieldsName, "Name of added new field is wrong !!!");
        Assert.assertEquals(contractInfo.getNewFieldsValue(), newFieldsValue, "Value of added new field is wrong !!!");

        Screenshoter.makeScreenshot();
    }
}
