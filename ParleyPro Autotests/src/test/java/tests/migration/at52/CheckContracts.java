package tests.migration.at52;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.ContractInfo;
import pages.DashboardPage;
import utils.Cache;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckContracts
{
    private static Logger logger = Logger.getLogger(CheckContracts.class);

    @Test(priority = 1)
    @Description("This test checks that both contracts are in the list, and all icons are in place and linked contracts are present")
    public void checkContractsThatAreInProgress()
    {
        // 1 start
        logger.info("Assert the first row of table...");
        StringBuffer jsCode = new StringBuffer("var firstRow = $('.contracts-list__table a').eq(0).text();");
                     jsCode.append("var lastActivity = $('.contracts-list__table a').eq(0).find(\".contracts-list__cell-activity\").text();");
                     jsCode.append("var res = firstRow.replace(lastActivity, \"\");");
                     jsCode.append("return res;");

        String rowWithoutLastActivity = Selenide.executeJavaScript(jsCode.toString());

        Assert.assertEquals(rowWithoutLastActivity, "ClassicEugene's Counterparty OrganizationParley ProNEGOTIATEchat_bubble_outline2USD 12,345.00");
        // check that LastActivity has some text too...
        jsCode = new StringBuffer("var lastActivity = $('.contracts-list__table a').eq(0).find(\".contracts-list__cell-activity\").text(); return lastActivity.length > 0;");
        Assert.assertTrue(Selenide.executeJavaScript(jsCode.toString()));
        // 1 end

        // 2 start
        logger.info("Assert the second row of table...");
        jsCode = new StringBuffer("var firstRow = $('.contracts-list__table a').eq(1).text();");
        jsCode.append("var lastActivity = $('.contracts-list__table a').eq(1).find(\".contracts-list__cell-activity\").text();");
        jsCode.append("var res = firstRow.replace(lastActivity, \"\");");
        jsCode.append("return res;");

        rowWithoutLastActivity = Selenide.executeJavaScript(jsCode.toString());

        Assert.assertEquals(rowWithoutLastActivity, "Normal contractEugene's Counterparty OrganizationParley ProDRAFTNov 30, 2024");
        // 2 end

        // 3 start
        logger.info("Assert the third row of table...");
        jsCode = new StringBuffer("var firstRow = $('.contracts-list__table a').eq(2).text();");
        jsCode.append("var lastActivity = $('.contracts-list__table a').eq(2).find(\".contracts-list__cell-activity\").text();");
        jsCode.append("var res = firstRow.replace(lastActivity, \"\");");
        jsCode.append("return res;");

        rowWithoutLastActivity = Selenide.executeJavaScript(jsCode.toString());

        Assert.assertEquals(rowWithoutLastActivity, "Online Contract One With a Long Long Long Long Long Long Long Long Long Long " +
                "Long Long Long Long Long Long Long TitleEugene's Counterparty with a Long Long Long Long Long Long Long Long Long Long Long Long Long Name, Inc.Parley ProNEGOTIATEUSD 100,000,000.00Dec 31, 2030");
        // check that LastActivity has some text too...
        jsCode = new StringBuffer("var lastActivity = $('.contracts-list__table a').eq(2).find(\".contracts-list__cell-activity\").text(); return lastActivity.length > 0;");
        Assert.assertTrue(Selenide.executeJavaScript(jsCode.toString()));
        // 3 end

        logger.info("Assert that link icons are visible for two contracts...");
        $$(".glyphicon-link").shouldHave(CollectionCondition.size(2));
        // get first icon
        Assert.assertEquals(Selenide.executeJavaScript("return window.getComputedStyle( document.querySelectorAll('.glyphicon-link')[0], ':before').getPropertyValue('content')"), "\"\ue144\"");
        // get second icon
        Assert.assertEquals(Selenide.executeJavaScript("return window.getComputedStyle( document.querySelectorAll('.glyphicon-link')[1], ':before').getPropertyValue('content')"), "\"\ue144\"");


        // Check svg icons of Stage columns for all contracts
        logger.info("Assert that first contract has word icon for Stage...");
        WebElement we = Selenide.executeJavaScript("return $('.contract-status').eq(0).find(\"path[fill-rule='evenodd']\")[0]");
        Assert.assertEquals($(we).getAttribute("d"), "M11 3v14.64l-9-1.901V4.702L11 3zm-1 4.83l-1.363.058-.724 3.574-.902-3.505-1.27.053-.855 3.367-.724-3.3L3 8.127l1.14 4.537 1.229.047.92-3.379.898 3.448 1.319.05 1.494-5zm2 0h3v1h-3v-1zm3 2h-3v1h3v-1zm-3 2h3v1h-3v-1zm0-6h3.5a.5.5 0 01.5.5v8a.5.5 0 01-.5.5H12v1h3.5a1.5 1.5 0 001.5-1.5v-8a1.5 1.5 0 00-1.5-1.5H12v1z");

        logger.info("Assert that second contract has pencil icon for Stage...");
        we = Selenide.executeJavaScript("return $('.contract-status').eq(1).find(\"path[fill-rule='evenodd']\")[0]");
        Assert.assertEquals($(we).getAttribute("d"), "M13.566 7.598l-1.414-1.414-.707.707 1.414 1.414.707-.707zm-1.06 1.061L11.09 7.245 6.995 11.34l-.354 1.768 1.768-.354 4.096-4.096z");

        logger.info("Assert that third contract has people icon for Stage...");
        we = Selenide.executeJavaScript("return $('.contract-status').eq(2).find(\"path[fill='#fff']\")[0]");
        Assert.assertEquals($(we).getAttribute("d"), "M11.628 10.223c.357 0 .746.053 1.168.16.421.108.81.283 1.168.524.357.242.536.517.536.826V12.8h-2.451v-1.067c0-.59-.268-1.088-.804-1.49.089-.014.217-.02.383-.02zm-4.424.16c.422-.107.81-.16 1.168-.16.358 0 .747.053 1.168.16.422.108.805.283 1.15.524.357.242.536.517.536.826V12.8H5.5v-1.067c0-.309.179-.584.536-.826a3.704 3.704 0 011.168-.523zm2.01-1.389c-.229.255-.51.383-.842.383a1.15 1.15 0 01-.861-.383 1.27 1.27 0 01-.364-.906c0-.349.121-.65.364-.906a1.15 1.15 0 01.861-.382c.332 0 .613.127.843.382.242.255.364.557.364.906 0 .35-.122.651-.364.906zm3.275 0a1.15 1.15 0 01-.861.383 1.15 1.15 0 01-.862-.383 1.27 1.27 0 01-.364-.906c0-.349.121-.65.364-.906a1.15 1.15 0 01.862-.382c.332 0 .619.127.861.382.243.255.364.557.364.906 0 .35-.121.651-.364.906z");

        Screenshoter.makeScreenshot();

        // 1
        logger.info("Hover over _first_ link icon and check data...");
        $$(".glyphicon-link").first().hover();

        $(".spinner").waitUntil(Condition.disappear, 30_000);
        $(".rc-tooltip-content").waitUntil(Condition.visible, 15_000).shouldBe(Condition.visible);
        $(".js-linked-contracts-title").shouldHave(Condition.exactText("Linked contracts: 1"));
        $(".js-linked-contracts-head").shouldHave(Condition.exactText("Child to:\nOnline Contract One With a Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long Title"));
        $(".js-linked-contracts-stage").shouldHave(Condition.exactText("Negotiate"));

        Screenshoter.makeScreenshot();

        jsCode = new StringBuffer();
        jsCode.append("$('.rc-tooltip-inner').hide(); $('.rc-tooltip').hide()"); // hide previous hovered popup
        Selenide.executeJavaScript(jsCode.toString());

        // 2
        logger.info("Hover over _second_ link icon and check data...");
        $$(".glyphicon-link").last().hover();

        $(".spinner").waitUntil(Condition.disappear, 30_000);
        $(".rc-tooltip-content").waitUntil(Condition.visible, 15_000).shouldBe(Condition.visible);

        $(".js-linked-contracts-title").shouldHave(Condition.exactText("Linked contracts: 2"));

        $$(".js-linked-contracts-head").shouldHave(CollectionCondition.size(2));
        $$(".js-linked-contracts-head").first().shouldHave(Condition.exactText("Supplemented by:\nShort"));
        $$(".js-linked-contracts-head").last().shouldHave(Condition.exactText("Parent to:\nClassic"));

        $$(".js-linked-contracts-stage").shouldHave(CollectionCondition.size(2));
        $$(".js-linked-contracts-stage").first().shouldHave(Condition.exactText("Managed"));
        $$(".js-linked-contracts-stage").last().shouldHave(Condition.exactText("Negotiate"));

        $(".rc-tooltip-inner").shouldHave(Condition.exactText("Linked contracts: 2\nSupplemented by:\nShort\nStage:\nManaged\nEffective date:\nDec 17, 2020\nRenewal date:\nDec 17, 2021\nExpiration date:\nJan 21, 2025\nParent to:\nClassic\nStage:\nNegotiate"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test goes to Executed contracts and verifies that all 3 contracts have correct text, have linked icons, one have question mark")
    public void checkExecutedContracts()
    {
        new DashboardPage().getSideBar().clickExecutedContracts(false);

        logger.info("Assert _first_ row in a table...");
        $$(".contracts-list__table a").get(0).shouldHave(Condition.exactText("Long values and that is a long long " +
                "long long long long long long long long long long long long long long long long long long long long long long " +
                "long title\nEugene's Counterparty with a Long Long Long Long Long Long Long Long Long Long Long Long Long Name, Inc.\nsigned\nUSD 1,000,000,000.00\nJan 21, 2025"));

        logger.info("Assert _second_ row in a table...");
        $$(".contracts-list__table a").get(1).shouldHave(Condition.exactText("Short\nEugene's Counterparty Organization\nmanaged\nUSD 1.00 Dec 17, 2020 Dec 17, 2020 Dec 17, 2021 Jan 21, 2025"));

        logger.info("Assert _third_ row in a table...");
        $$(".contracts-list__table a").get(2).shouldHave(Condition.exactText("Normal values in contract\nEugene's Counterparty Organization\nmanaged\nUSD 10,000.00 Dec 16, 2020 Dec 31, 2020"));

        logger.info("Assert that link icons are visible for two contracts...");
        $$(".glyphicon-link").shouldHave(CollectionCondition.size(2));

        // Check svg icons of Stage columns for all contracts
        // 1st row
        WebElement we = Selenide.executeJavaScript("return $('.contract-status').eq(0).find(\"path[fill-rule='evenodd']\")[0]");
        Assert.assertEquals($(we).getAttribute("d"), "M14.008 7.981l-4.729 5-3.25-2.964.942-1.034 2.235 2.037 3.785-4.001 1.017.962z");

        // 2nd row
        we = Selenide.executeJavaScript("return $('.contract-status').eq(1).find(\"path[fill-rule='evenodd']\")[0]");
        Assert.assertEquals($(we).getAttribute("d"), "M6 6.8l4-1.6 4 1.6v3.333c0 3.2-2.667 4.445-4 4.667-1.333-.222-4-1.467-4-4.667V6.8z");
        we = Selenide.executeJavaScript("return $('.contract-status').eq(1).find(\"path[fill-rule='evenodd']\")[1]");
        Assert.assertEquals($(we).getAttribute("d"), "M13.158 8.681l-3.665 3.874-2.565-2.338.943-1.034 1.55 1.412 2.72-2.876 1.017.962z");

        //3rd row
        we = Selenide.executeJavaScript("return $('.contract-status').eq(2).find(\"path[fill-rule='evenodd']\")[0]");
        Assert.assertEquals($(we).getAttribute("d"), "M6 6.8l4-1.6 4 1.6v3.333c0 3.2-2.667 4.445-4 4.667-1.333-.222-4-1.467-4-4.667V6.8z");
        we = Selenide.executeJavaScript("return $('.contract-status').eq(2).find(\"path[fill-rule='evenodd']\")[1]");
        Assert.assertEquals($(we).getAttribute("d"), "M13.158 8.681l-3.665 3.874-2.565-2.338.943-1.034 1.55 1.412 2.72-2.876 1.017.962z");

        // Check question mark
        logger.info("Hover over question mark and check data...");
        $(".contracts-list__new-expiration-date svg").hover();

        $(".spinner").waitUntil(Condition.disappear, 30_000);
        $(".rc-tooltip-content").waitUntil(Condition.visible, 15_000).shouldBe(Condition.visible);
        $(".js-linked-contracts-title").shouldHave(Condition.exactText("Contract was amended: 1"));
        $(".js-linked-contracts-head").shouldHave(Condition.exactText("Amended by:\nShort"));
        $(".js-linked-contracts-stage").shouldHave(Condition.exactText("Managed"));
        $(".rc-tooltip-inner").shouldHave(Condition.exactText("Contract was amended: 1\nOriginal Expiration Date: \nAmended by:\nShort\nStage:\nManaged\nEffective date:\nDec 17, 2020\nRenewal date:\nDec 17, 2021\nExpiration date:\nJan 21, 2025"));

        Screenshoter.makeScreenshot();

        StringBuffer jsCode = new StringBuffer();
        jsCode.append("$('.rc-tooltip-inner').hide(); $('.rc-tooltip').hide()"); // hide previous hovered popup
        Selenide.executeJavaScript(jsCode.toString());

        // 1
        logger.info("Hover over _first_ link icon and check data...");
        $$(".glyphicon-link").first().hover();

        $(".spinner").waitUntil(Condition.disappear, 30_000);
        $(".rc-tooltip-content").waitUntil(Condition.visible, 15_000).shouldBe(Condition.visible);
        $(".js-linked-contracts-title").shouldHave(Condition.exactText("Linked contracts: 1"));
        $(".js-linked-contracts-head").shouldHave(Condition.exactText("Amended by:\nShort"));
        $(".js-linked-contracts-stage").shouldHave(Condition.exactText("Managed"));
        $(".rc-tooltip-inner").shouldHave(Condition.exactText("Linked contracts: 1\nAmended by:\nShort\nStage:\nManaged\nEffective date:\nDec 17, 2020\nRenewal date:\nDec 17, 2021\nExpiration date:\nJan 21, 2025"));

        Screenshoter.makeScreenshot();

        jsCode = new StringBuffer();
        jsCode.append("$('.rc-tooltip-inner').hide(); $('.rc-tooltip').hide()"); // hide previous hovered popup
        Selenide.executeJavaScript(jsCode.toString());

        // 2
        logger.info("Hover over _second_ link icon and check data...");
        $$(".glyphicon-link").last().hover();

        $(".spinner").waitUntil(Condition.disappear, 30_000);
        $(".rc-tooltip-content").waitUntil(Condition.visible, 15_000).shouldBe(Condition.visible);

        $(".js-linked-contracts-title").shouldHave(Condition.exactText("Linked contracts: 2"));

        $$(".js-linked-contracts-head").shouldHave(CollectionCondition.size(2));
        $$(".js-linked-contracts-head").first().shouldHave(Condition.exactText("Amendment to:\nLong values and that is a long long long long long long long long long long long long long long long long long long long long long long long long long title"));
        $$(".js-linked-contracts-head").last().shouldHave(Condition.exactText("Addendum to:\nOnline Contract One With a Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long Title"));

        $$(".js-linked-contracts-stage").shouldHave(CollectionCondition.size(2));
        $$(".js-linked-contracts-stage").first().shouldHave(Condition.exactText("Signed"));
        $$(".js-linked-contracts-stage").last().shouldHave(Condition.exactText("Negotiate"));

        $(".rc-tooltip-inner").shouldHave(Condition.exactText("Linked contracts: 2\nAmendment to:\nLong values and that is a long long long long long long long long long long long long long long long long long long long long long long long long long title\nStage:\nSigned\nAddendum to:\nOnline Contract One With a Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long Long Title\nStage:\nNegotiate"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test click 'Short' contract in Executed contracts and validate all fields in Post-execution tab")
    public void checkValuesOfManagedContractShort() throws InterruptedException
    {
        ContractInfo contractInfo = new DashboardPage().getSideBar().clickExecutedContracts(false).selectContract("Short");
        Thread.sleep(2_000);

        // Check 3 icons of contract header
        $$(".contract-header__right > div").shouldHave(CollectionCondition.size(3));
        $(".private-label").shouldBe(Condition.visible).shouldHave(Condition.exactText("visibility_off"));
        $(".linked-contracts-label").shouldBe(Condition.visible).shouldHave(Condition.exactText("link 2"));
        $$(".contract-header-users .user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("PP", "(c"));

        logger.info("Asserting all fields on Contract Info -> Post-execution tab...");

        // The following expected values are all hardcoded and are correct only for 'Short' contract
        Assert.assertEquals(contractInfo.getSignatureDate(), "Dec 17, 2020");
        Assert.assertEquals(contractInfo.getEffectiveDate(), "Dec 17, 2020");
        Assert.assertEquals(contractInfo.getInitialTerm(), "1");
        Assert.assertEquals(contractInfo.getInitialTermDuration(), "Years");
        Assert.assertTrue(contractInfo.getAutoRenewalState());
        Assert.assertTrue( Selenide.executeJavaScript("return $('label span:contains(\"Number of renewals\")').is(':visible')") );
        Assert.assertTrue(contractInfo.getAutoRenewalState());

        Assert.assertEquals(contractInfo.getSubsequentTermMonths(), "1");
        Assert.assertEquals(contractInfo.getRenewal(), "Dec 17, 2021");
        Assert.assertEquals(contractInfo.getSubsequentTermNotification(), "1d");
        Assert.assertTrue(contractInfo.getRenewalEmailTo().contains("you@example.com") && contractInfo.getRenewalEmailTo().contains("my@example.com"));
        Assert.assertEquals(contractInfo.getNoticeOfNonRenewal(), "15");
        Assert.assertEquals(contractInfo.getNotice(), "Jan 2, 2022");
        Assert.assertEquals(contractInfo.getNoticeOfNonRenewalNotification(), "1d");
        Assert.assertTrue(contractInfo.getNoticeEmailTo().contains("you@example.com") && contractInfo.getNoticeEmailTo().contains("my@example.com"));
        Assert.assertEquals(contractInfo.getExpirationDate(), "Jan 21, 2025");
        Assert.assertEquals(contractInfo.getExpirationDateNotification(), "1d");
        Assert.assertTrue(contractInfo.getExpirationEmailTo().contains("you@example.com") && contractInfo.getExpirationEmailTo().contains("my@example.com"));

        // Scroll to Notes field
        Selenide.executeJavaScript("$('label:contains(\"Notes\")').parent().find(\"textarea\")[0].scrollIntoView({});");

        // Assert Custom fields
        Assert.assertEquals(contractInfo.getValueFromCustomField("PE Select One"), "Yes");
        Assert.assertEquals(contractInfo.getValueFromCustomField("PE Select Two"), "1");
        Assert.assertEquals(contractInfo.getValueFromCustomField("PE Multi Select"), "A very long long long long long long long long long long long long long long long long long long long long long long long value");
        Assert.assertEquals(contractInfo.getValueFromCustomField("Some strange date"), "Nov 30, 2020");
        Assert.assertEquals(contractInfo.getValueFromCustomField("Title"), "Custom Field");
        Assert.assertEquals(contractInfo.getValueFromCustomField("Value"), "1");
        Assert.assertEquals(contractInfo.getNotes(), "Note from me.");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    @Description("This test click 'Normal values in contract' contract in Executed contracts and validate all fields in Post-execution tab")
    public void checkValuesOfManagedContractNormal() throws InterruptedException
    {
        ContractInfo contractInfo = new DashboardPage().getSideBar().clickExecutedContracts(false).selectContract("Normal values in contract");
        Thread.sleep(2_000);

        logger.info("Asserting all fields of 'Normal values in contract' contract...");
        Assert.assertEquals(contractInfo.getSignatureDate(), "Dec 16, 2020");
        Assert.assertEquals(contractInfo.getEffectiveDate(), "Dec 31, 2020");
        Assert.assertEquals(contractInfo.getInitialTerm(), "");
        Assert.assertEquals(contractInfo.getInitialTermDuration(), "Months");
        Assert.assertFalse(contractInfo.getAutoRenewalState());
        Assert.assertEquals(contractInfo.getExpirationDate(), "");
        Assert.assertEquals(contractInfo.getExpirationEmailTo(), "");

        Assert.assertEquals(contractInfo.getValueFromCustomField("PE Select One"), "");
        Assert.assertEquals(contractInfo.getValueFromCustomField("PE Select Two"), "");
        Assert.assertEquals(contractInfo.getValueFromCustomField("PE Multi Select"), "");
        Assert.assertEquals(contractInfo.getValueFromCustomField("Some strange date"), "");

        Assert.assertEquals(contractInfo.getNotes(), "");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 5)
    @Description("This test click 'Classic' contract in In-progress contracts and validate values")
    public void checkValuesOfClassicContract() throws IOException
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("Classic");
        $(".spinner").waitUntil(Condition.disappear, 30_000);

        $(".contract-header__company").shouldHave(Condition.exactText("Eugene's Counterparty Organization\nUSD 12,345.00"));
        $(".contract-header__name .rename").shouldHave(Condition.exactText("Classic"));
        Assert.assertEquals(Selenide.executeJavaScript("return $('button:contains(\"Manage discussions\")').find(\"opened\").text()"), "2");
        $$(".contract-header-general__right button").shouldHave(CollectionCondition.size(5));
        Assert.assertEquals(Selenide.executeJavaScript("return $('.lifecycle__item.active').text()"), "REVIEW(1)NEGOTIATE(1)REVIEWNEGOTIATE");
        $(".contract-header__status .discussion-indicator").shouldBe(Condition.visible).shouldHave(Condition.exactText("chat_bubble_outline2"));
        $(".label_theme_orange").shouldBe(Condition.visible).shouldHave(Condition.exactText("WITH MY TEAM"));
        $$(".contract-header-users > div").shouldHave(CollectionCondition.size(2)); // check my team and counterparty circle icons in contract header

        // Check document titles in this contract...
        $$(".document__header-rename").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("Big Test Document v1", "wiki_article"));

        // Assert that for document Big Test Document v1 amount of discussions = 2
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document__header-row:contains(\"Big Test\")').next().find(\".discussion-indicator__count\").text()"), "2");

        Assert.assertEquals(Selenide.executeJavaScript("return $('.document__header-row:contains(\"Big Test\")').next().find(\".document__score\").text()"), "PP SHARE");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document__header-row:contains(\"wiki\")').next().find(\".document__score\").text()"), "PP(e SHARE");

        $(".label_theme_dgray").shouldBe(Condition.visible).shouldHave(Condition.exactText("3RD PARTY"));
        $("#UPLOAD_VERSION_DOCUMENT").shouldBe(Condition.visible).shouldHave(Condition.exactText("Upload new version"));

        // Assert that attach icon and title are visible
        $(".supporting-documents__title").shouldBe(Condition.visible).shouldHave(Condition.exactText("attach_fileAttachments"));

        // Assert that file itself is attached
        $(".supporting-documents__list").shouldBe(Condition.visible).shouldHave(Condition.exactText("Faro_-_July_2014_(7).jpg\n7.04 KB"));

        // Assert that Attachment icon is visible
        $(".supporting-documents__document-ico").shouldBe(Condition.visible).getCssValue("background").contains("images/f6b1a648d4931fe261ab371a6bc9151d.svg");

        Screenshoter.makeScreenshot();

        if( !Cache.getInstance().getCachedLoginBase().isProd() )
        {
            logger.info("Trying to download Attachment...");
            try
            {
                $(".supporting-documents__document-ico").download();

                new WebDriverWait(WebDriverRunner.getWebDriver(), 20).
                        until(d -> Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), "Faro_-_July_2014_(7).jpg").toFile().exists());

                Assert.assertTrue(new File(Const.DOWNLOAD_DIR.getAbsolutePath() + "/Faro_-_July_2014_(7).jpg").exists());
            }
            catch (FileNotFoundException e)
            {
                logger.error("FileNotFoundException", e);
            }

            FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
        }
    }
}
