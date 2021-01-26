package tests.regression.at70;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
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
    public void PasteTextOverParagraph() throws InterruptedException
    {
        ParagraphActionsPopup paragraphActionsPopup = new OpenedContract().hover("Simple numbered two");

        CKEditorActive ckEditorActive = paragraphActionsPopup.clickAddParagraphAbove();

        String pasteHtml = "<p>p_Text 1</p><p>P_Text2</p>";

        ckEditorActive.insertHtml(pasteHtml);
        ckEditorActive.clickPost();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Internal discussion"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        // TODO: uncomment after fixing of PAR-13340
        logger.info("Assert that text was added...");
        //Assert.assertFalse(Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"2\")').text().trim() === '2.'"));

        Screenshoter.makeScreenshot();
    }
}
