package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.testng.ScreenShooter;
import constants.AcceptTypes;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import pages.tooltips.ParagraphActionsPopup;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ ScreenShooter.class})
public class SupportedNumberingFormats
{
    private static Logger logger = Logger.getLogger(SupportedNumberingFormats.class);

    @Test(priority = 1)
    @Description("This test inserts item before the very first item of lettered list (a. b. etc) and check recalculation")
    public void insertBeforeLetteredList() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Letter_lowercase_a");

        String addedItem = "L0_Letter_lowercase_ABOVE_a";

        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphAbove();
        Thread.sleep(2_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation after adding...");
        String actual = getList("a.", "d.");

        Assert.assertEquals(actual, "a.|" + addedItem + ",b.|L0_Letter_lowercase_a," +
                "c.|L0_Letter_lowercase_b,c.1.|L1_Letter_lowercase_b_1,d.|L0_Letter_lowercase_c");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test inserts item before sublevel of capital lettered list (A. B. etc.), accepts and check recalculation")
    public void insertAndAcceptBeforeSublevelOfCapitalLetteredList() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L1_Letter_capital_B_1");

        String addedItem = "L1_Letter_capital_ABOVE_B_1";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphAbove();
        Thread.sleep(1_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        openedContract.hover(addedItem).clickAcceptChangesOnParagraph(AcceptTypes.INSERT).clickAcceptText();

        logger.info("Assert insert notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation after adding...");
        String actual = getList("A.", "C.");

        Assert.assertEquals(actual, "A.|L0_Letter_capital_A,B.|L0_Letter_capital_B,B.1.|" + addedItem + ",B.2.|L1_Letter_capital_B_1,C.|L0_Letter_capital_C");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test removes item before sublevel of braced list ( (a) (b) etc ) and check recalculation")
    public void removeBeforeSublevelOfBracedList()
    {
        // scroll to bottom of page
        Selenide.executeJavaScript("document.querySelector('.documents__list').scrollTo(0,document.body.scrollHeight)");

        OpenedContract openedContract = new OpenedContract(true);

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Letter_braces_b");

        CKEditorActive ckEditorActive = paragraphActionsPopup.clickDelete();
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation after removing...");
        String actual = getList("(a)", "(b)");

        Assert.assertEquals(actual, "(a)|L0_Letter_braces_a,a.1.|L1_Letter_braces_b_1,(b)|L0_Letter_braces_c");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    @Description("This test removes the first item of roman capital list, accepts and check recalculation")
    public void removeAndAcceptFirstItemOfRomanCapitalList()
    {
        OpenedContract openedContract = new OpenedContract(true);

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Roman_capital_I");

        paragraphActionsPopup.clickDelete().clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        openedContract.hover("L0_Roman_capital_I").clickAcceptChangesOnParagraph(AcceptTypes.DELETE).clickAcceptText();

        logger.info("Assert delete notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation after removing...");
        String actual = getList("I.", "II.");

        Assert.assertEquals(actual, "I.|L0_Roman_capital_II,I.1.|L1_Roman_capital_II_1,II.|L0_Roman_capital_III");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 5)
    @Description("This test inserts at the end of roman lowered list and check recalculation")
    public void insertAtTheEndOfRomanLoweredList() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract(true);

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Roman_lower_iii");

        String addedItem = "last_added_roman_lowered";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphBelow();
        Thread.sleep(1_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation after adding...");
        String actual = getList("i.", "iv.");

        Assert.assertEquals(actual, "i.|L0_Roman_lower_i,ii.|L0_Roman_lower_ii,ii.1.|L1_Roman_lower_ii_1," +
                "iii.|L0_Roman_lower_iii,iv.|" + addedItem);

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 6)
    @Description("This test inserts item in sublevel of roman lowered list, accepts and check recalculation")
    public void insertAndAcceptInSublevelOfRomanLoweredList() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract(true);

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L1_Roman_lower_ii_1");

        String addedItem = "L1_Roman_lower_sublevel";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphBelow();
        Thread.sleep(1_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        openedContract.hover(addedItem).clickAcceptChangesOnParagraph(AcceptTypes.INSERT).clickAcceptText();

        logger.info("Assert insert notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        String actual = getList("i.", "iv.");

        Assert.assertEquals(actual, "i.|L0_Roman_lower_i,ii.|L0_Roman_lower_ii,ii.1.|L1_Roman_lower_ii_1," +
                "ii.2.|" + addedItem + ",iii.|L0_Roman_lower_iii,iv.|last_added_roman_lowered");

        Screenshoter.makeScreenshot();
    }

    /**
     * Get list that starts from startingItem and ends with endingItem
     * @param startingItem starting item of the list. May be a. A. (a) I. i.
     * @param endingItem ending item of the list
     * @return
     */
    private String getList(String startingItem, String endingItem)
    {
        StringBuffer jsCode = new StringBuffer("var paragraphs = $('.document-paragraph__content-text p').filter((ind, el) => $(el).find('span:first-child[contenteditable=false]').length); ");
        jsCode.append("var items = []; ");
        jsCode.append("paragraphs.each((ind, paragraph) => { ");
        jsCode.append("var parapgraphText = paragraph.innerText; ");
        jsCode.append("var numberingText = paragraph.children[0].innerText + paragraph.children[1].innerText; ");
        jsCode.append("items.push([paragraph.children[0].innerText, parapgraphText.replace(numberingText, '').trim()]); }); ");
        jsCode.append("var newItems = []; ");
        jsCode.append("var flag = false; ");
        jsCode.append("for (var i = 0; i < items.length; i++) { ");
        jsCode.append("var item = items[i]; ");
        jsCode.append("if( item[0] === '" + startingItem + "' ) { flag = true; } ");
        jsCode.append("if( flag === true ) { newItems.push(item); } ");
        jsCode.append("if (item[0] === '" + endingItem + "') { break } } ");
        jsCode.append("var string = newItems.map(item => item.join('|')).join(','); ");
        jsCode.append("return string; ");

        return Selenide.executeJavaScript(jsCode.toString());
    }
}
