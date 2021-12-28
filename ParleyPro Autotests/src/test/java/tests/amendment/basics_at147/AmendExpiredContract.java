package tests.amendment.basics_at147;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import forms.CompleteSign;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.ExecutedContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.FileNotFoundException;
import java.util.Set;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AmendExpiredContract
{
    private static Logger logger = Logger.getLogger(AmendExpiredContract.class);
    private SoftAssert softAssert = new SoftAssert();

    @Test(priority = 1)
    public void amendExpiredContract()
    {
        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract("Executed Expired");

        new OpenedContract().clickContractActionsMenu().clickAmendContract().clickSaveFromAmendment();
        AddDocuments addDocuments = new AddDocuments();

        softAssert.assertEquals(addDocuments.getContractTitle(), "Executed Expired-A", "Contract title is wrong !!!");
        softAssert.assertAll();

        logger.info("Making sure that Linked contracts icon was shown in header...");
        $(".linked-contracts-label").shouldBe(Condition.visible).shouldHave(Condition.exactText("link 1"));

        logger.info("Hover over Linked contracts icon...");
        $(".linked-contracts-label").hover();
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        $(".rc-tooltip-inner .js-linked-contracts-title").shouldHave(Condition.text("Linked contracts: 1"));
        $(".rc-tooltip-inner .js-linked-contracts-head").shouldHave(Condition.text("Amendment to:\nExecuted Expired"));
        $(".rc-tooltip-inner .js-linked-contracts-stage").shouldHave(Condition.text("Expired"));
        $$($(".rc-tooltip-inner").findElements(By.cssSelector("div[class^='styles__date']"))).shouldHave(CollectionCondition.size(2))
                .shouldHave(CollectionCondition.textsInAnyOrder("Effective date:\nMay 31, 2021", "Expiration date:\nMay 31, 2021"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void createNewAmendmentForSigned()
    {
        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract("Executed Signed");

        new OpenedContract().clickContractActionsMenu().clickAmendContract().clickSaveFromAmendment();
        AddDocuments addDocuments = new AddDocuments();

        logger.info("Checking B in the name...");
        softAssert.assertEquals(addDocuments.getContractTitle(), "Executed Signed-B", "Contract title is wrong !!!");
        softAssert.assertAll();

        logger.info("Hover link icon and check 2 links and that one is linked as related...");
        $(".linked-contracts-label").shouldBe(Condition.visible).shouldHave(Condition.exactText("link 2"));

        $(".linked-contracts-label").hover();
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        $(".rc-tooltip-inner .js-linked-contracts-title").shouldHave(Condition.text("Linked contracts: 2"));
        $$(".rc-tooltip-inner .js-linked-contracts-head").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("Amendment to:\nExecuted Signed", "Related to:\nExecuted Signed-A"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void signAmendment() throws FileNotFoundException
    {
        new DashboardPage().getSideBar()
                           .clickInProgressContracts(false)
                           .selectContract("Executed Signed-A");

        new AddDocuments().clickUploadMyTeamDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Document AT-14 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        OpenedContract openedContract = new OpenedContract();

        openedContract.switchDocumentToSign("AT-14").clickStart();
        $("#COMPLETE_MANUAL_DOCUMENT").waitUntil(Condition.visible, 10_000);
        openedContract.clickCompleteSign("AT-14").clickComplete();
    }

    @Test(priority = 4)
    public void createAmendmentToAmendment()
    {
        new DashboardPage().getSideBar()
                           .clickExecutedContracts(false)
                           .selectContract("Executed Signed-A");

        new OpenedContract().clickContractActionsMenu().clickAmendContract().clickSaveFromAmendment();
        AddDocuments addDocuments = new AddDocuments();

        logger.info("Checking double A in the name...");
        softAssert.assertEquals(addDocuments.getContractTitle(), "Executed Signed-A-A", "Contract title is wrong !!!");
        softAssert.assertAll();

        logger.info("Checking that link has number 3...");
        $(".linked-contracts-label").shouldBe(Condition.visible).shouldHave(Condition.exactText("link 3"));

        $(".linked-contracts-label").hover();
        $(".spinner").waitUntil(Condition.disappear, 15_000);
        $(".rc-tooltip-inner .js-linked-contracts-title").shouldHave(Condition.text("Linked contracts: 3"));
        $$(".rc-tooltip-inner .js-linked-contracts-head").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.textsInAnyOrder("Amendment to:\nExecuted Signed-A", "Related to:\nExecuted Signed", "Related to:\nExecuted Signed-B"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 5)
    @Description("This test goes to Executed contracts and hover link icon of 'Executed Signed-A' contract.")
    public void hoverLinkIcon()
    {
        new DashboardPage().getSideBar().clickExecutedContracts(false);

        logger.info("Hover over link icon of 'Executed Signed-A' contract and check that 'Amendment to', 'Amended by' and 'Related to' are correct...");

        WebElement linkIcon = Selenide.executeJavaScript("return $('.contracts-list__contract-name:contains(\"Executed Signed-A\")').parent().next().find(\"span\")[0]");
        $(linkIcon).hover();
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        $(".rc-tooltip-inner .js-linked-contracts-title").shouldHave(Condition.text("Linked contracts: 3"));
        $$(".rc-tooltip-inner .js-linked-contracts-head").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.textsInAnyOrder("Amendment to:\nExecuted Signed", "Amended by:\nExecuted Signed-A-A", "Related to:\nExecuted Signed-B"));
        Screenshoter.makeScreenshot();

        // This action opens contract in a new tab - so save prev. tab
        WebDriver driver = WebDriverRunner.getWebDriver();
        String currentHandle = driver.getWindowHandle();

        logger.info("Click by link of 'Related to' contract...");
        $((WebElement) Selenide.executeJavaScript("return $('.rc-tooltip-inner .js-linked-contracts-head:contains(\"Related\")').find(\"a\")[0]")).click();

        Set<String> handles = driver.getWindowHandles();
        for ( String actual : handles )
        {
            if ( !actual.equalsIgnoreCase(currentHandle) )
            {
                logger.info("Switching to opened tab...");
                driver.switchTo().window(actual);
                break;
            }
        }
        AddDocuments addDocuments = new AddDocuments();

        softAssert.assertEquals(addDocuments.getContractTitle(), "Executed Signed-B", "Contract title is wrong !!!");
        softAssert.assertAll();

        driver.close(); // close current tab
        driver.switchTo().window(currentHandle); // switching to the original tab
    }
}
