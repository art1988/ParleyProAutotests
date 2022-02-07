package tests.numbered_lists.at53;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.AcceptTypes;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import pages.tooltips.ParagraphActionsPopup;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

public class RecalculationChecks
{
    private String[] itemsToAdd = { "Added_item-L0_Number_Point_111",
                                    "RightBeforeL2-L0_Number_Point_222",
                                    "AtTheEndL2-L1_Letter_capital_AAA",
                                    "NewBulletItem_L2"
                                  };

    private static Logger logger = Logger.getLogger(RecalculationChecks.class);

    @Test(priority = 1)
    @Description("This test adds new item above the very first at Level 1 and checks recalculation")
    public void addAboveFirst() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Num_Point_1");

        String addedItem = itemsToAdd[0];
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphAbove();
        Thread.sleep(1_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation of list after adding...");
        Assert.assertEquals(getWholeList(), "1." + addedItem + ",2.L0_Num_Point_1,3.L0_Num_Point_2," +
                "A.L1_Letter_capital_A,B.L1_Letter_capital_B,\uF0A7L2_Bullet_1,\uF0A7L2_Bullet_2,\uF0A7L2_Bullet_3," +
                "C.L1_Letter_capital_C,D.L1_Letter_capital_D,4.L0_Num_Point_3,5.L0_Num_Point_4");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test adds item in level 1 right before level 2 list and check recalculations")
    public void addRightBeforeLevel2() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L0_Num_Point_2");

        String addedItem = itemsToAdd[1];
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphBelow();
        Thread.sleep(1_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation of list after adding...");
        Assert.assertEquals(getWholeList(), "1.Added_item-L0_Number_Point_111,2.L0_Num_Point_1,3.L0_Num_Point_2," +
                "4." + addedItem + ",A.L1_Letter_capital_A,B.L1_Letter_capital_B,\uF0A7L2_Bullet_1,\uF0A7L2_Bullet_2,\uF0A7L2_Bullet_3," +
                "C.L1_Letter_capital_C,D.L1_Letter_capital_D,5.L0_Num_Point_3,6.L0_Num_Point_4");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test removes line in level 2 and check recalculations")
    public void removeItemInLevel2()
    {
        new OpenedContract().hover("L1_Letter_capital_B")
                            .clickDelete()
                            .clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert recalculation after deletion...");
        Assert.assertEquals(getWholeList(), "1.Added_item-L0_Number_Point_111,2.L0_Num_Point_1,3.L0_Num_Point_2," +
                "4.RightBeforeL2-L0_Number_Point_222,A.L1_Letter_capital_A,B.L1_Letter_capital_B,\uF0A7L2_Bullet_1,\uF0A7L2_Bullet_2,\uF0A7L2_Bullet_3," +
                "B.L1_Letter_capital_C,C.L1_Letter_capital_D,5.L0_Num_Point_3,6.L0_Num_Point_4");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    @Description("This test adds item in level 2 at the end and check recalculations")
    public void addItemInLevel2AtTheEnd() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L1_Letter_capital_D");

        String addedItem = itemsToAdd[2];
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphBelow();
        Thread.sleep(1_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        Assert.assertEquals(getWholeList(), "1.Added_item-L0_Number_Point_111,2.L0_Num_Point_1,3.L0_Num_Point_2," +
                "4.RightBeforeL2-L0_Number_Point_222,A.L1_Letter_capital_A,B.L1_Letter_capital_B,\uF0A7L2_Bullet_1,\uF0A7L2_Bullet_2,\uF0A7L2_Bullet_3," +
                "B.L1_Letter_capital_C,C.L1_Letter_capital_D,D." + addedItem + ",5.L0_Num_Point_3,6.L0_Num_Point_4");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 5)
    @Description("This test adds item in level 3 and check recalculations")
    public void addItemInLevel3() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("L2_Bullet_2");

        String addedItem = itemsToAdd[3];
        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphBelow();
        Thread.sleep(1_000);
        ckEditorActive.setText(addedItem);
        ckEditorActive.clickPost();

        logger.info("Assert that notification popup was shown...");
        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        Assert.assertEquals(getWholeList(), "1.Added_item-L0_Number_Point_111,2.L0_Num_Point_1,3.L0_Num_Point_2," +
                "4.RightBeforeL2-L0_Number_Point_222,A.L1_Letter_capital_A,B.L1_Letter_capital_B,\uF0A7L2_Bullet_1,\uF0A7L2_Bullet_2,\uF0A7" + addedItem + ",\uF0A7L2_Bullet_3," +
                "B.L1_Letter_capital_C,C.L1_Letter_capital_D,D.AtTheEndL2-L1_Letter_capital_AAA,5.L0_Num_Point_3,6.L0_Num_Point_4");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 6)
    @Description("This test accepts all changes and check recalculations")
    public void acceptAllChangesAndCheckRecalculations() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        logger.info("Accepting all that were inserted...");
        for( int i = 0; i < itemsToAdd.length; i++ )
        {
            ParagraphActionsPopup paragraphActionsPopup = openedContract.hover(itemsToAdd[i]);
            paragraphActionsPopup.clickAcceptChangesOnParagraph(AcceptTypes.INSERT).clickAcceptText();

            logger.info("Assert notification...");
            $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText(" post has been successfully created."));
            $(".notification-stack").should(Condition.disappear);
        }

        Thread.sleep(3_000);

        logger.info("And finally accept deleted...");
        ParagraphActionsPopup paragraphActionsPopup = openedContract.hover("capital_B");
        paragraphActionsPopup.clickAcceptChangesOnParagraph(AcceptTypes.DELETE).clickAcceptText();

        logger.info("Assert notification...");
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText(" post has been successfully created."));
        $(".notification-stack").should(Condition.disappear);

        Assert.assertEquals(getWholeList(), "1." + itemsToAdd[0] + ",2.L0_Num_Point_1,3.L0_Num_Point_2,4." + itemsToAdd[1] + "," +
                "A.L1_Letter_capital_A,\uF0A7L2_Bullet_1,\uF0A7L2_Bullet_2,\uF0A7" + itemsToAdd[3] + ",\uF0A7L2_Bullet_3," +
                "B.L1_Letter_capital_C,C.L1_Letter_capital_D,D." + itemsToAdd[2] + ",5.L0_Num_Point_3,6.L0_Num_Point_4");

        Screenshoter.makeScreenshot();
    }

    /**
     * Helper method that retrieves numbered list as single String
     * @return
     */
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
