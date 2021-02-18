package pages.subelements;

import com.codeborne.selenide.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Appears when user click on 'Delete multiple paragraphs' button.
 * Represents form that appears in footer (with comment field, POST button etc.) and radio buttons from the left side
 * of each paragraph.
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
     * Mark paragraph that contains text by selecting radio button. Use this method during mass deletion
     * @param text
     */
    public void markParagraph(String text)
    {
        WebElement paragraphCheckbox = Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"" + text + "\")').prev().find(\"input\")[0]");

        Actions action = new Actions(WebDriverRunner.getWebDriver());
        action.moveToElement(paragraphCheckbox).clickAndHold(paragraphCheckbox).release().build().perform();
        try
        {
            Thread.sleep(1_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        logger.info("Paragraph that contains text: " + text + " has been marked");
    }

    public void clickPost()
    {
        postButton.click();

        logger.info("Post button was clicked");
    }
}
