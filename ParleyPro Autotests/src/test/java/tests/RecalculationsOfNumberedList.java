package tests;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import pages.tooltips.ParagraphActionsPopup;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

public class RecalculationsOfNumberedList
{
    private static Logger logger = Logger.getLogger(RecalculationsOfNumberedList.class);

    @Test(priority = 1)
    @Description("This test adds line in level 1 (without sublevels) and check recalculations")
    public void addLineInLevel1WithoutSublevels() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        // add new item after 1-rst
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Number_Point_1");

        String addedItem = "new added item - L0_Number_Point_A";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphBelow();
        Thread.sleep(1_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation of the first list after adding...");

        String actual = getFirstList("6.");

        Assert.assertEquals(actual, "1.|L0_Number_Point_1,2.|" + addedItem + "," +
                "3.|L0_Number_Point_2,3.1.|L1_Number_Point_2_1,3.2.|L1_Number_Point_2_2,3.2.1.|L2_Number_Point_2_2_1," +
                "3.2.1.1.|L3_Number_Point_2_2_1_1,3.2.1.1.1.|L4_Number_Point_2_2_1_1_1," +
                "3.2.1.1.1.1.|L5_Number_Point_2_2_1_1_1_1,3.2.1.1.1.1.1.|L6_Number_Point_2_2_1_1_1_1_1,3.2.2.|L2_Number_Point_2_2_2," +
                "3.3.|L1_Number_Point_2_3,3.4.|L1_Number_Point_2_4,4.|L0_Number_Point_3,5.|L0_Number_Point_4,6.|L0_Number_Point_5");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test adds line in level 1 (with sublevels) and check recalculations")
    public void addLineInLevel1WithSublevels() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        // add 1 line below item that has sublevels
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Number_Point_2");

        String addedItem = "L0_Number_Point_B with sublevels";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphBelow();
        Thread.sleep(1_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation of the first list after adding...");

        String actual = getFirstList("7.");

        Assert.assertEquals(actual, "1.|L0_Number_Point_1,2.|new added item - L0_Number_Point_A,3.|L0_Number_Point_2," +
                "4.|" + addedItem + ",4.1.|L1_Number_Point_2_1,4.2.|L1_Number_Point_2_2," +
                "4.2.1.|L2_Number_Point_2_2_1,4.2.1.1.|L3_Number_Point_2_2_1_1,4.2.1.1.1.|L4_Number_Point_2_2_1_1_1," +
                "4.2.1.1.1.1.|L5_Number_Point_2_2_1_1_1_1,4.2.1.1.1.1.1.|L6_Number_Point_2_2_1_1_1_1_1," +
                "4.2.2.|L2_Number_Point_2_2_2,4.3.|L1_Number_Point_2_3,4.4.|L1_Number_Point_2_4,5.|L0_Number_Point_3," +
                "6.|L0_Number_Point_4,7.|L0_Number_Point_5");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test adds line in sublevel and check recalculations")
    public void addLineInSublevel() throws InterruptedException
    {
        refreshPage(); // need to refresh page, otherwise hover will be performed on wrong paragraph

        OpenedContract openedContract = new OpenedContract();

        // add 1 line above item that has sublevels
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L2_Number_Point_2_2_1");

        String addedItem = "L2_Number_Point_C in sublevel";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphAbove();
        Thread.sleep(1_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation of the first list after adding...");

        String actual = getFirstList("7.");

        Assert.assertEquals(actual, "1.|L0_Number_Point_1,2.|new added item - L0_Number_Point_A,3.|L0_Number_Point_2," +
                "4.|L0_Number_Point_B with sublevels,4.1.|L1_Number_Point_2_1,4.2.|L1_Number_Point_2_2," +
                "4.2.1.|" + addedItem + ",4.2.2.|L2_Number_Point_2_2_1,4.2.2.1.|L3_Number_Point_2_2_1_1," +
                "4.2.2.1.1.|L4_Number_Point_2_2_1_1_1,4.2.2.1.1.1.|L5_Number_Point_2_2_1_1_1_1," +
                "4.2.2.1.1.1.1.|L6_Number_Point_2_2_1_1_1_1_1,4.2.3.|L2_Number_Point_2_2_2,4.3.|L1_Number_Point_2_3," +
                "4.4.|L1_Number_Point_2_4,5.|L0_Number_Point_3,6.|L0_Number_Point_4,7.|L0_Number_Point_5");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    @Description("This test deletes line in level 1 (without sublevels) and check recalculations")
    public void deleteLineInLevel1WithoutSublevels()
    {
        OpenedContract openedContract = new OpenedContract();

        // hover over the line that we want to delete
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Number_Point_2");

        CKEditorActive ckEditorActive = paragraphActionsPopup.clickDelete();
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation of the first list after adding...");

        String actual = getFirstList("6."); // changed to 6.

        Assert.assertEquals(actual, "1.|L0_Number_Point_1,2.|new added item - L0_Number_Point_A," +
                "3.|L0_Number_Point_B with sublevels,3.1.|L1_Number_Point_2_1,3.2.|L1_Number_Point_2_2," +
                "3.2.1.|L2_Number_Point_C in sublevel,3.2.2.|L2_Number_Point_2_2_1,3.2.2.1.|L3_Number_Point_2_2_1_1," +
                "3.2.2.1.1.|L4_Number_Point_2_2_1_1_1,3.2.2.1.1.1.|L5_Number_Point_2_2_1_1_1_1," +
                "3.2.2.1.1.1.1.|L6_Number_Point_2_2_1_1_1_1_1,3.2.3.|L2_Number_Point_2_2_2,3.3.|L1_Number_Point_2_3," +
                "3.4.|L1_Number_Point_2_4,4.|L0_Number_Point_3,5.|L0_Number_Point_4,6.|L0_Number_Point_5");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 5)
    @Description("This test deletes line in sublevel and check recalculations")
    public void deleteLineInSublevel()
    {
        refreshPage(); // Need to refresh again

        OpenedContract openedContract = new OpenedContract();

        // hover over the line that we want to delete
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L1_Number_Point_2_2");

        CKEditorActive ckEditorActive = paragraphActionsPopup.clickDelete();
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation of the first list after adding...");

        String actual = getFirstList("6."); // still 6.

        Assert.assertEquals(actual, "1.|L0_Number_Point_1,2.|new added item - L0_Number_Point_A," +
                "3.|L0_Number_Point_B with sublevels,3.1.|L1_Number_Point_2_1,3.1.1.|L2_Number_Point_C in sublevel," +
                "3.1.2.|L2_Number_Point_2_2_1,3.1.2.1.|L3_Number_Point_2_2_1_1,3.1.2.1.1.|L4_Number_Point_2_2_1_1_1," +
                "3.1.2.1.1.1.|L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.|L6_Number_Point_2_2_1_1_1_1_1," +
                "3.1.3.|L2_Number_Point_2_2_2,3.2.|L1_Number_Point_2_3,3.3.|L1_Number_Point_2_4,4.|L0_Number_Point_3," +
                "5.|L0_Number_Point_4,6.|L0_Number_Point_5");

        Screenshoter.makeScreenshot();
    }

    /**
     * Get first numbered list
     * @param endOfList - the last item of the list. Indicates the end of the list. May be 6. or 7., etc.
     * @return
     */
    private String getFirstList(String endOfList)
    {
        StringBuffer jsCode = new StringBuffer("var paragraphs = $('.document-paragraph__content-text p').filter((ind, el) => $(el).find('span:first-child[contenteditable=false]').length); ");
        jsCode.append("var items = []; ");
        jsCode.append("paragraphs.each((ind, paragraph) => { ");
        jsCode.append("var parapgraphText = paragraph.innerText; ");
        jsCode.append("var numberingText = paragraph.children[0].innerText + paragraph.children[1].innerText; ");
        jsCode.append("items.push([paragraph.children[0].innerText, parapgraphText.replace(numberingText, '').trim()]); }); ");
        jsCode.append("var newItems = []; ");
        jsCode.append("for (var i = 0; i < items.length; i++) { ");
        jsCode.append("var item = items[i]; ");
        jsCode.append("newItems.push(item); ");
        jsCode.append("if (item[0] === '" + endOfList + "') { break } } ");
        jsCode.append("var string = newItems.map(item => item.join('|')).join(','); ");
        jsCode.append("return string;");

        return Selenide.executeJavaScript(jsCode.toString());
    }

    private void refreshPage()
    {
        Selenide.refresh();

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"L0_Number_Point_1\")')");
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"Bullet_3\")')");
    }
}
