package tests.numbered_lists.at25;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.AcceptTypes;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.OpenedDiscussion;
import pages.subelements.CKEditorActive;
import pages.tooltips.ParagraphActionsPopup;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
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

        Assert.assertEquals(getWholeList(), "1.L0_Number_Point_1,2." + addedItem + ",3.L0_Number_Point_2,3.1.L1_Number_Point_2_1," +
                "3.2.L1_Number_Point_2_2,3.2.1.L2_Number_Point_2_2_1,3.2.1.1.L3_Number_Point_2_2_1_1,3.2.1.1.1.L4_Number_Point_2_2_1_1_1," +
                "3.2.1.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.2.1.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1,3.2.2.L2_Number_Point_2_2_2," +
                "3.3.L1_Number_Point_2_3,3.4.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4,6.L0_Number_Point_5," +
                "a.L0_Letter_lowercase_a,b.L0_Letter_lowercase_b,b.1.L1_Letter_lowercase_b_1,c.L0_Letter_lowercase_c," +
                "A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a," +
                "(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c,I.L0_Roman_capital_I,II.L0_Roman_capital_II," +
                "II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

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

        Assert.assertEquals(getWholeList(), "1.L0_Number_Point_1,2.new added item - L0_Number_Point_A,3.L0_Number_Point_2," +
                "4." + addedItem + ",4.1.L1_Number_Point_2_1,4.2.L1_Number_Point_2_2,4.2.1.L2_Number_Point_2_2_1," +
                "4.2.1.1.L3_Number_Point_2_2_1_1,4.2.1.1.1.L4_Number_Point_2_2_1_1_1,4.2.1.1.1.1.L5_Number_Point_2_2_1_1_1_1," +
                "4.2.1.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1,4.2.2.L2_Number_Point_2_2_2,4.3.L1_Number_Point_2_3,4.4.L1_Number_Point_2_4," +
                "5.L0_Number_Point_3,6.L0_Number_Point_4,7.L0_Number_Point_5,a.L0_Letter_lowercase_a,b.L0_Letter_lowercase_b," +
                "b.1.L1_Letter_lowercase_b_1,c.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1," +
                "C.L0_Letter_capital_C,(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c," +
                "I.L0_Roman_capital_I,II.L0_Roman_capital_II,II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i," +
                "ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

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

        Assert.assertEquals(getWholeList(), "1.L0_Number_Point_1,2.new added item - L0_Number_Point_A," +
                "3.L0_Number_Point_2,4.L0_Number_Point_B with sublevels,4.1.L1_Number_Point_2_1,4.2.L1_Number_Point_2_2," +
                "4.2.1." + addedItem + ",4.2.2.L2_Number_Point_2_2_1,4.2.2.1.L3_Number_Point_2_2_1_1," +
                "4.2.2.1.1.L4_Number_Point_2_2_1_1_1,4.2.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,4.2.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1," +
                "4.2.3.L2_Number_Point_2_2_2,4.3.L1_Number_Point_2_3,4.4.L1_Number_Point_2_4,5.L0_Number_Point_3,6.L0_Number_Point_4," +
                "7.L0_Number_Point_5,a.L0_Letter_lowercase_a,b.L0_Letter_lowercase_b,b.1.L1_Letter_lowercase_b_1,c.L0_Letter_lowercase_c," +
                "A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a," +
                "(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c,I.L0_Roman_capital_I,II.L0_Roman_capital_II," +
                "II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1," +
                "iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    @Description("This test deletes line in level 1 (without sublevels) and check recalculations")
    public void deleteLineInLevel1WithoutSublevels()
    {
        // hover over the line that we want to delete
        new OpenedContract().hover("L0_Number_Point_2")
                            .clickDelete()
                            .clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation of the first list after deletion...");

        Assert.assertEquals(getWholeList(), "1.L0_Number_Point_1,2.new added item - L0_Number_Point_A,2.L0_Number_Point_2," +
                "3.L0_Number_Point_B with sublevels,3.1.L1_Number_Point_2_1,3.2.L1_Number_Point_2_2,3.2.1.L2_Number_Point_C in sublevel," +
                "3.2.2.L2_Number_Point_2_2_1,3.2.2.1.L3_Number_Point_2_2_1_1,3.2.2.1.1.L4_Number_Point_2_2_1_1_1," +
                "3.2.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.2.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1,3.2.3.L2_Number_Point_2_2_2," +
                "3.3.L1_Number_Point_2_3,3.4.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4,6.L0_Number_Point_5," +
                "a.L0_Letter_lowercase_a,b.L0_Letter_lowercase_b,b.1.L1_Letter_lowercase_b_1,c.L0_Letter_lowercase_c," +
                "A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a," +
                "(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c,I.L0_Roman_capital_I,II.L0_Roman_capital_II," +
                "II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1," +
                "iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 5)
    @Description("This test deletes line in sublevel and check recalculations")
    public void deleteLineInSublevel()
    {
        refreshPage(); // Need to refresh again

        // hover over the line that we want to delete
        new OpenedContract().hover("L1_Number_Point_2_2")
                            .clickDelete()
                            .clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation of the first list after deletion...");

        Assert.assertEquals(getWholeList(), "1.L0_Number_Point_1,2.new added item - L0_Number_Point_A,2.L0_Number_Point_2," +
                "3.L0_Number_Point_B with sublevels,3.1.L1_Number_Point_2_1,2.2.L1_Number_Point_2_2,3.1.1.L2_Number_Point_C in sublevel," +
                "3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1,3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1," +
                "3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1,3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4," +
                "4.L0_Number_Point_3,5.L0_Number_Point_4,6.L0_Number_Point_5,a.L0_Letter_lowercase_a,b.L0_Letter_lowercase_b,b.1.L1_Letter_lowercase_b_1," +
                "c.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a," +
                "(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c,I.L0_Roman_capital_I,II.L0_Roman_capital_II,II.1.L1_Roman_capital_II_1," +
                "III.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 6)
    @Description("This test adds line above the first item and check recalculations")
    public void addLineAboveFirstItem() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        // hover over the very _first_ list item
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Number_Point_1");

        String addedItem = "above first item";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphAbove();
        Thread.sleep(1_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation of the first list after adding...");

        Assert.assertEquals(getWholeList(), "1." + addedItem + ",2.L0_Number_Point_1,3.new added item - L0_Number_Point_A," +
                "2.L0_Number_Point_2,4.L0_Number_Point_B with sublevels,4.1.L1_Number_Point_2_1,2.2.L1_Number_Point_2_2,4.1.1.L2_Number_Point_C in sublevel," +
                "4.1.2.L2_Number_Point_2_2_1,4.1.2.1.L3_Number_Point_2_2_1_1,4.1.2.1.1.L4_Number_Point_2_2_1_1_1,4.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1," +
                "4.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1,4.1.3.L2_Number_Point_2_2_2,4.2.L1_Number_Point_2_3,4.3.L1_Number_Point_2_4," +
                "5.L0_Number_Point_3,6.L0_Number_Point_4,7.L0_Number_Point_5,a.L0_Letter_lowercase_a,b.L0_Letter_lowercase_b," +
                "b.1.L1_Letter_lowercase_b_1,c.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1," +
                "C.L0_Letter_capital_C,(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c," +
                "I.L0_Roman_capital_I,II.L0_Roman_capital_II,II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i," +
                "ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 7)
    @Description("This test adds line below the last item and check recalculations")
    public void addLineBelowLastItem() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        // hover over the very _last_ list item
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Number_Point_5");

        String addedItem = "below last item";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphBelow();
        Thread.sleep(1_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation of the first list after adding...");

        Assert.assertEquals(getWholeList(), "1.above first item,2.L0_Number_Point_1,3.new added item - L0_Number_Point_A," +
                "2.L0_Number_Point_2,4.L0_Number_Point_B with sublevels,4.1.L1_Number_Point_2_1,2.2.L1_Number_Point_2_2," +
                "4.1.1.L2_Number_Point_C in sublevel,4.1.2.L2_Number_Point_2_2_1,4.1.2.1.L3_Number_Point_2_2_1_1,4.1.2.1.1.L4_Number_Point_2_2_1_1_1," +
                "4.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,4.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1,4.1.3.L2_Number_Point_2_2_2," +
                "4.2.L1_Number_Point_2_3,4.3.L1_Number_Point_2_4,5.L0_Number_Point_3,6.L0_Number_Point_4,7.L0_Number_Point_5," +
                "8." + addedItem + ",a.L0_Letter_lowercase_a,b.L0_Letter_lowercase_b,b.1.L1_Letter_lowercase_b_1,c.L0_Letter_lowercase_c," +
                "A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a," +
                "(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c,I.L0_Roman_capital_I,II.L0_Roman_capital_II," +
                "II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1," +
                "iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 8)
    @Description("This test deletes the first list item and check recalculations")
    public void deleteFirstListItem()
    {
        OpenedContract openedContract = new OpenedContract();

        // hover over the very _first_ list item
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("above first item");

        paragraphActionsPopup.clickAcceptChangesOnParagraph(AcceptTypes.INSERT).clickAcceptText(); // and click accept via tooltip

        paragraphActionsPopup = openedContract.hover("above first item"); // hover again over the first item

        paragraphActionsPopup.clickDelete().clickPost(); // and click delete icon

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation of the first list after deletion...");

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.new added item - L0_Number_Point_A," +
                "2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels,3.1.L1_Number_Point_2_1,2.2.L1_Number_Point_2_2," +
                "3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1,3.1.2.1.1.L4_Number_Point_2_2_1_1_1," +
                "3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1,3.1.3.L2_Number_Point_2_2_2," +
                "3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4,6.L0_Number_Point_5," +
                "7.below last item,a.L0_Letter_lowercase_a,b.L0_Letter_lowercase_b,b.1.L1_Letter_lowercase_b_1,c.L0_Letter_lowercase_c," +
                "A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a," +
                "(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c,I.L0_Roman_capital_I,II.L0_Roman_capital_II," +
                "II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1," +
                "iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 9)
    @Description("This test discards added line and check recalculation")
    public void discardAddedLine()
    {
        OpenedContract openedContract = new OpenedContract();

        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon("new added item - L0_Number_Point_A");

        openedDiscussion.clickDiscardDiscussion().clickDiscardDiscussion();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        openedDiscussion.close();
        $(".paragraph-discussions").waitUntil(Condition.disappear, 5_000); // wait until right panel disappear

        logger.info("Assert recalculation of the first list after discard...");

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,2.L0_Number_Point_B with sublevels," +
                "2.1.L1_Number_Point_2_1,2.2.L1_Number_Point_2_2,2.1.1.L2_Number_Point_C in sublevel,2.1.2.L2_Number_Point_2_2_1," +
                "2.1.2.1.L3_Number_Point_2_2_1_1,2.1.2.1.1.L4_Number_Point_2_2_1_1_1,2.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1," +
                "2.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1,2.1.3.L2_Number_Point_2_2_2,2.2.L1_Number_Point_2_3,2.3.L1_Number_Point_2_4," +
                "3.L0_Number_Point_3,4.L0_Number_Point_4,5.L0_Number_Point_5,6.below last item,a.L0_Letter_lowercase_a,b.L0_Letter_lowercase_b," +
                "b.1.L1_Letter_lowercase_b_1,c.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1," +
                "C.L0_Letter_capital_C,(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c," +
                "I.L0_Roman_capital_I,II.L0_Roman_capital_II,II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i," +
                "ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 10)
    @Description("This test discards deleted line and check recalculation")
    public void discardDeletedLine()
    {
        OpenedContract openedContract = new OpenedContract();

        // open discussion for deleted line
        OpenedDiscussion openedDiscussion = openedContract.clickByDiscussionIcon("L0_Number_Point_2");

        openedDiscussion.clickDiscardDiscussion().clickDiscardDiscussion();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        openedDiscussion.close();
        $(".paragraph-discussions").waitUntil(Condition.disappear, 5_000); // wait until right panel disappear

        logger.info("Assert recalculation of the first list after discard...");

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                "3.1.L1_Number_Point_2_1,2.2.L1_Number_Point_2_2,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1," +
                "3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1," +
                "3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4," +
                "6.L0_Number_Point_5,7.below last item,a.L0_Letter_lowercase_a,b.L0_Letter_lowercase_b,b.1.L1_Letter_lowercase_b_1," +
                "c.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1,C.L0_Letter_capital_C," +
                "(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c,I.L0_Roman_capital_I," +
                "II.L0_Roman_capital_II,II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii," +
                "ii.1.L1_Roman_lower_ii_1,iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 11)
    @Description("This test accepts added line and check recalculation")
    public void acceptAddedLine()
    {
        OpenedContract openedContract = new OpenedContract();

        // hover over line that was added
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Number_Point_B with sublevels");

        paragraphActionsPopup.clickAcceptChangesOnParagraph(AcceptTypes.INSERT).clickAcceptText();

        logger.info("Assert notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation after accepting...");

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                "3.1.L1_Number_Point_2_1,2.2.L1_Number_Point_2_2,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1," +
                "3.1.2.1.L3_Number_Point_2_2_1_1,3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1," +
                "3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1,3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4," +
                "4.L0_Number_Point_3,5.L0_Number_Point_4,6.L0_Number_Point_5,7.below last item,a.L0_Letter_lowercase_a,b.L0_Letter_lowercase_b," +
                "b.1.L1_Letter_lowercase_b_1,c.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1," +
                "C.L0_Letter_capital_C,(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c," +
                "I.L0_Roman_capital_I,II.L0_Roman_capital_II,II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i," +
                "ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 12)
    @Description("This test accepts deleted line and check recalculation")
    public void acceptDeletedLine() throws InterruptedException
    {
        Selenide.refresh();
        OpenedContract openedContract = new OpenedContract();

        $(".spinner").waitUntil(Condition.disappear, 60_000 * 2);
        Thread.sleep(3_000);
        // hover over line that was deleted
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L1_Number_Point_2_2");

        paragraphActionsPopup.clickAcceptChangesOnParagraph(AcceptTypes.DELETE).clickAcceptText();

        logger.info("Assert notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation after accepting...");

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                "3.1.L1_Number_Point_2_1,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1," +
                "3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1," +
                "3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4," +
                "6.L0_Number_Point_5,7.below last item,a.L0_Letter_lowercase_a,b.L0_Letter_lowercase_b,b.1.L1_Letter_lowercase_b_1," +
                "c.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1,C.L0_Letter_capital_C," +
                "(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c,I.L0_Roman_capital_I," +
                "II.L0_Roman_capital_II,II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii," +
                "ii.1.L1_Roman_lower_ii_1,iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    private String getWholeList()
    {
        StringBuffer jsCode = new StringBuffer("var items = [];");
        jsCode.append("$('.document-paragraph__content-text p').each(");
        jsCode.append("function(i, paragraph) {");
        jsCode.append("items.push(paragraph.innerText.replace(/\\s+/, '')); } );");
        jsCode.append("items = items.filter(Boolean);");
        jsCode.append("return items.toString();");

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
