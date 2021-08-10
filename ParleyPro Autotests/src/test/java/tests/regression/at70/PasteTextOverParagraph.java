package tests.regression.at70;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.OpenedContract;
import pages.subelements.CKEditorActive;
import pages.tooltips.ParagraphActionsPopup;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class PasteTextOverParagraph
{
    private static Logger logger = Logger.getLogger(PasteTextOverParagraph.class);

    @Test
    @Description("This test paste two <p> tags as new numbered list item and checks that they become visible.")
    public void PasteTextOverParagraph() throws InterruptedException
    {
        ParagraphActionsPopup paragraphActionsPopup = new OpenedContract().hover("Simple numbered two");

        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphAbove();

        String pasteHtml = "<p>p_Text 1</p><p>P_Text2</p>";

        ckEditorActive.insertHtml(pasteHtml);
        ckEditorActive.clickPost();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that 2 paragraphs were added...");
        String textFromItemTwo = Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"2\")').text().trim()");
        textFromItemTwo = textFromItemTwo.replaceAll(" ", "");
        textFromItemTwo = textFromItemTwo.replace("\u00a0",""); // remove all NBSP symbols
        Assert.assertEquals(textFromItemTwo, "2.p_Text1\n" + "P_Text2", "Looks like paragraphs weren't pasted !");

        Screenshoter.makeScreenshot();
    }
}
