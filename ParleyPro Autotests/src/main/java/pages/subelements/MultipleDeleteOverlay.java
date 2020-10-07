package pages.subelements;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Appears when user click on 'Delete multiple paragraphs' button
 * Represents from that appears  in footer (comment field, POST button etc.) and radio buttons from the left side
 * of each paragraph
 */
public class MultipleDeleteOverlay
{
    private SelenideElement postButton = $(".multiple-delete__submit");



    private static Logger logger = Logger.getLogger(MultipleDeleteOverlay.class);

    public MultipleDeleteOverlay()
    {
        // check that footer is visible
        $(".multiple-delete").shouldBe(Condition.visible);

        // check that radio buttons were appeared
        $$(".checkbox.is_rounded").shouldHave(CollectionCondition.sizeGreaterThan(2));
    }

    /**
     * Mark paragraph that contains text by selecting radio button
     * @param text
     */
    public void markParagraph(String text)
    {
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"" + text + "\")').prev().find(\"input\")[0].click()");

        logger.info("Paragraph that contains text: " + text + " has been marked");
    }

    public void clickPost()
    {
        postButton.click();

        logger.info("Post button was clicked");
    }
}
