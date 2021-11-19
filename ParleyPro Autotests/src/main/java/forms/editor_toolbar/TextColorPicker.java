package forms.editor_toolbar;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$$;

public class TextColorPicker
{
    private static Logger logger = Logger.getLogger(TextColorPicker.class);


    public TextColorPicker()
    {
        $$(".editor-popup.modal-dialog iframe").first().shouldBe(Condition.visible);

        try { Thread.sleep(1_000); } catch (InterruptedException e) { }

        Assert.assertTrue(Selenide.executeJavaScript("return $($('.editor-popup.modal-dialog iframe').eq(0)[0].contentDocument).find('div').attr('title') === 'Colors'"));
    }

    /**
     * Select color by colorCode
     * @param colorCode may be 000000 (black), Lime, B7B7B7, etc.
     */
    public void selectColor(String colorCode)
    {
        Selenide.executeJavaScript("$($('.editor-popup.modal-dialog iframe').eq(0)[0].contentDocument).find('div table tr td a[title=\"" + colorCode + "\"]').find('span')[0].click()");

        logger.info("Color " + colorCode + " has been selected...");
    }
}
