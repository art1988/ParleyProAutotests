package tests.numbered_lists.at25;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.AcceptTypes;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import pages.tooltips.ParagraphActionsPopup;
import tests.LoginBase;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
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
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation after adding...");

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                "3.1.L1_Number_Point_2_1,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1," +
                "3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1," +
                "3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4," +
                "6.L0_Number_Point_5,7.below last item,a." + addedItem + ",b.L0_Letter_lowercase_a,c.L0_Letter_lowercase_b," +
                "c.1.L1_Letter_lowercase_b_1,d.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_B_1," +
                "C.L0_Letter_capital_C,(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c," +
                "I.L0_Roman_capital_I,II.L0_Roman_capital_II,II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i," +
                "ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

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

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                "3.1.L1_Number_Point_2_1,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1," +
                "3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1," +
                "3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4," +
                "6.L0_Number_Point_5,7.below last item,a.L0_Letter_lowercase_ABOVE_a,b.L0_Letter_lowercase_a,c.L0_Letter_lowercase_b," +
                "c.1.L1_Letter_lowercase_b_1,d.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1." + addedItem + "," +
                "B.2.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,b.1.L1_Letter_braces_b_1,(c)L0_Letter_braces_c," +
                "I.L0_Roman_capital_I,II.L0_Roman_capital_II,II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii," +
                "ii.1.L1_Roman_lower_ii_1,iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();

        Selenide.refresh();
        Thread.sleep(1_000);

        new OpenedContract();
    }

    @Test(priority = 3)
    @Description("This test removes item before sublevel of braced list ( (a) (b) etc ) and check recalculation")
    public void removeBeforeSublevelOfBracedList() throws InterruptedException
    {
        Thread.sleep(1_000);
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

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                "3.1.L1_Number_Point_2_1,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1," +
                "3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1," +
                "3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4," +
                "6.L0_Number_Point_5,7.below last item,a.L0_Letter_lowercase_ABOVE_a,b.L0_Letter_lowercase_a,c.L0_Letter_lowercase_b," +
                "c.1.L1_Letter_lowercase_b_1,d.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_ABOVE_B_1," +
                "B.2.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,a.1.L1_Letter_braces_b_1," +
                "(b)L0_Letter_braces_c,I.L0_Roman_capital_I,II.L0_Roman_capital_II,II.1.L1_Roman_capital_II_1,III.L0_Roman_capital_III," +
                "i.L0_Roman_lower_i,ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

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

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                "3.1.L1_Number_Point_2_1,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1," +
                "3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1," +
                "3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4," +
                "6.L0_Number_Point_5,7.below last item,a.L0_Letter_lowercase_ABOVE_a,b.L0_Letter_lowercase_a,c.L0_Letter_lowercase_b," +
                "c.1.L1_Letter_lowercase_b_1,d.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_ABOVE_B_1," +
                "B.2.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,a.1.L1_Letter_braces_b_1,(b)L0_Letter_braces_c," +
                "I.L0_Roman_capital_II,I.1.L1_Roman_capital_II_1,II.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1," +
                "iii.L0_Roman_lower_iii,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

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
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation after adding...");

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                "3.1.L1_Number_Point_2_1,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1," +
                "3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1," +
                "3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4," +
                "6.L0_Number_Point_5,7.below last item,a.L0_Letter_lowercase_ABOVE_a,b.L0_Letter_lowercase_a,c.L0_Letter_lowercase_b," +
                "c.1.L1_Letter_lowercase_b_1,d.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_ABOVE_B_1," +
                "B.2.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,a.1.L1_Letter_braces_b_1," +
                "(b)L0_Letter_braces_c,I.L0_Roman_capital_II,I.1.L1_Roman_capital_II_1,II.L0_Roman_capital_III,i.L0_Roman_lower_i," +
                "ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,iii.L0_Roman_lower_iii,iv." + addedItem + ",•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

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
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        openedContract.hover(addedItem).clickAcceptChangesOnParagraph(AcceptTypes.INSERT).clickAcceptText();

        logger.info("Assert insert notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                "3.1.L1_Number_Point_2_1,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1," +
                "3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1," +
                "3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4,6.L0_Number_Point_5," +
                "7.below last item,a.L0_Letter_lowercase_ABOVE_a,b.L0_Letter_lowercase_a,c.L0_Letter_lowercase_b,c.1.L1_Letter_lowercase_b_1," +
                "d.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_ABOVE_B_1,B.2.L1_Letter_capital_B_1," +
                "C.L0_Letter_capital_C,(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,a.1.L1_Letter_braces_b_1,(b)L0_Letter_braces_c,I.L0_Roman_capital_II," +
                "I.1.L1_Roman_capital_II_1,II.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,ii.2." + addedItem + "," +
                "iii.L0_Roman_lower_iii,iv.last_added_roman_lowered,•L0_Bullet_1,•L0_Bullet_2,oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 7)
    @Description("This test insert item in Level 1 of bullet list and check recalculation")
    public void insertNewBulletAtLevel1() throws InterruptedException
    {
        Selenide.refresh();
        OpenedContract openedContract = new OpenedContract(true);

        $(".spinner").waitUntil(Condition.disappear, 60_000 * 2);
        Thread.sleep(3_000);
        // scroll to bottom of page
        Selenide.executeJavaScript("document.querySelector('.documents__list').scrollTo(0,document.body.scrollHeight)");
        Thread.sleep(2_000);

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Bullet_2");

        String addedItem = "L0_Bullet_added_new";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphBelow();
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                "3.1.L1_Number_Point_2_1,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1," +
                "3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1," +
                "3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4," +
                "6.L0_Number_Point_5,7.below last item,a.L0_Letter_lowercase_ABOVE_a,b.L0_Letter_lowercase_a,c.L0_Letter_lowercase_b," +
                "c.1.L1_Letter_lowercase_b_1,d.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B," +
                "B.1.L1_Letter_capital_ABOVE_B_1,B.2.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a," +
                "(b)L0_Letter_braces_b,a.1.L1_Letter_braces_b_1,(b)L0_Letter_braces_c,I.L0_Roman_capital_II,I.1.L1_Roman_capital_II_1," +
                "II.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,ii.2.L1_Roman_lower_sublevel," +
                "iii.L0_Roman_lower_iii,iv.last_added_roman_lowered,•L0_Bullet_1,•L0_Bullet_2,•" + addedItem + ",oL1_Bullet_2_1,•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 8)
    @Description("This test insert item in Level 2 of bullet list, accept and check recalculation")
    public void insertNewBulletAtLevel2AndAccept() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract(true);

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L1_Bullet_2_1");

        String addedItem = "L1_Bullet_newSublevel";
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphBelow();
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        openedContract.hover(addedItem).clickAcceptChangesOnParagraph(AcceptTypes.INSERT).clickAcceptText();

        logger.info("Assert insert notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                "3.1.L1_Number_Point_2_1,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1," +
                "3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1," +
                "3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4," +
                "6.L0_Number_Point_5,7.below last item,a.L0_Letter_lowercase_ABOVE_a,b.L0_Letter_lowercase_a,c.L0_Letter_lowercase_b," +
                "c.1.L1_Letter_lowercase_b_1,d.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_ABOVE_B_1," +
                "B.2.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,a.1.L1_Letter_braces_b_1," +
                "(b)L0_Letter_braces_c,I.L0_Roman_capital_II,I.1.L1_Roman_capital_II_1,II.L0_Roman_capital_III,i.L0_Roman_lower_i," +
                "ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,ii.2.L1_Roman_lower_sublevel,iii.L0_Roman_lower_iii,iv.last_added_roman_lowered," +
                "•L0_Bullet_1,•L0_Bullet_2,•L0_Bullet_added_new,oL1_Bullet_2_1,o" + addedItem + ",•L0_Bullet_3");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 9)
    @Description("This test deletes bullet item in level 1 and check recalculation")
    public void deleteBulletAtLevel1()
    {
        OpenedContract openedContract = new OpenedContract(true);

        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Bullet_2");

        paragraphActionsPopup.clickDelete().clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        Assert.assertEquals(getWholeList(),"1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                    "3.1.L1_Number_Point_2_1,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1," +
                    "3.1.2.1.1.L4_Number_Point_2_2_1_1_1,3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1," +
                    "3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4,4.L0_Number_Point_3,5.L0_Number_Point_4,6.L0_Number_Point_5," +
                    "7.below last item,a.L0_Letter_lowercase_ABOVE_a,b.L0_Letter_lowercase_a,c.L0_Letter_lowercase_b,c.1.L1_Letter_lowercase_b_1,d.L0_Letter_lowercase_c," +
                    "A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_ABOVE_B_1,B.2.L1_Letter_capital_B_1,C.L0_Letter_capital_C,(a)L0_Letter_braces_a," +
                    "(b)L0_Letter_braces_b,a.1.L1_Letter_braces_b_1,(b)L0_Letter_braces_c,I.L0_Roman_capital_II,I.1.L1_Roman_capital_II_1,II.L0_Roman_capital_III," +
                    "i.L0_Roman_lower_i,ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,ii.2.L1_Roman_lower_sublevel,iii.L0_Roman_lower_iii,iv.last_added_roman_lowered," +
                    "•L0_Bullet_1,•L0_Bullet_2,•L0_Bullet_added_new,oL1_Bullet_2_1,oL1_Bullet_newSublevel,•L0_Bullet_3");


        Screenshoter.makeScreenshot();
    }

    @Test(priority = 10)
    @Description("This test deletes bullet item in level 2, accept and check recalculation")
    public void deleteBulletAtLevel2AndAccept()
    {
        OpenedContract openedContract = new OpenedContract(true);

        String itemToDelete = "L1_Bullet_2_1";
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover(itemToDelete);

        paragraphActionsPopup.clickDelete().clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        openedContract.hover(itemToDelete).clickAcceptChangesOnParagraph(AcceptTypes.DELETE).clickAcceptText();

        logger.info("Assert delete notification...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        Assert.assertEquals(getWholeList(), "1.above first item,1.L0_Number_Point_1,2.L0_Number_Point_2,3.L0_Number_Point_B with sublevels," +
                    "3.1.L1_Number_Point_2_1,3.1.1.L2_Number_Point_C in sublevel,3.1.2.L2_Number_Point_2_2_1,3.1.2.1.L3_Number_Point_2_2_1_1,3.1.2.1.1.L4_Number_Point_2_2_1_1_1," +
                    "3.1.2.1.1.1.L5_Number_Point_2_2_1_1_1_1,3.1.2.1.1.1.1.L6_Number_Point_2_2_1_1_1_1_1,3.1.3.L2_Number_Point_2_2_2,3.2.L1_Number_Point_2_3,3.3.L1_Number_Point_2_4," +
                    "4.L0_Number_Point_3,5.L0_Number_Point_4,6.L0_Number_Point_5,7.below last item,a.L0_Letter_lowercase_ABOVE_a,b.L0_Letter_lowercase_a,c.L0_Letter_lowercase_b," +
                    "c.1.L1_Letter_lowercase_b_1,d.L0_Letter_lowercase_c,A.L0_Letter_capital_A,B.L0_Letter_capital_B,B.1.L1_Letter_capital_ABOVE_B_1,B.2.L1_Letter_capital_B_1," +
                    "C.L0_Letter_capital_C,(a)L0_Letter_braces_a,(b)L0_Letter_braces_b,a.1.L1_Letter_braces_b_1,(b)L0_Letter_braces_c,I.L0_Roman_capital_II,I.1.L1_Roman_capital_II_1," +
                    "II.L0_Roman_capital_III,i.L0_Roman_lower_i,ii.L0_Roman_lower_ii,ii.1.L1_Roman_lower_ii_1,ii.2.L1_Roman_lower_sublevel,iii.L0_Roman_lower_iii," +
                    "iv.last_added_roman_lowered,•L0_Bullet_1,•L0_Bullet_2,•L0_Bullet_added_new,oL1_Bullet_newSublevel,•L0_Bullet_3");

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
}
