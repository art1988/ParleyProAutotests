package pages.subelements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import forms.Replace;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class FindAndReplacePopup
{
    private SelenideElement findTab                = $(".js-document-search-find-tab");
    private SelenideElement replaceTab             = $(".js-document-search-replace-tab");

    private SelenideElement findInDocumentInput    = $(".js-document-search-find-input");
    private SelenideElement replaceWithInput       = $(".js-document-search-replace-input");

    private SelenideElement reviewAndReplaceButton = $(".js-document-search-replace-btn");


    private static Logger logger = Logger.getLogger(FindAndReplacePopup.class);

    public FindAndReplacePopup()
    {
        findTab.parent().parent().waitUntil(Condition.visible, 15_000); // outer div

        findTab.waitUntil(Condition.visible, 10_000);
        replaceTab.waitUntil(Condition.visible, 10_000);
    }

    public FindAndReplacePopup clickReplaceTab()
    {
        replaceTab.click();

        findInDocumentInput.shouldBe(Condition.enabled).shouldBe(Condition.visible);
        replaceWithInput.shouldBe(Condition.enabled).shouldBe(Condition.visible);

        logger.info("REPLACE tab was clicked...");

        return this;
    }

    public FindAndReplacePopup findInDocument(String wordToFind)
    {
        try { Thread.sleep(1_000); } catch (InterruptedException e) { e.printStackTrace(); }
        findInDocumentInput.sendKeys(wordToFind);
        try { Thread.sleep(1_000); } catch (InterruptedException e) { e.printStackTrace(); }

        return this;
    }

    public FindAndReplacePopup replaceWith(String wordToReplace)
    {
        replaceWithInput.sendKeys(wordToReplace);
        try { Thread.sleep(1_000); } catch (InterruptedException e) { e.printStackTrace(); }

        return this;
    }

    public Replace clickReviewAndReplaceButton()
    {
        reviewAndReplaceButton.shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("REVIEW AND REPLACE button was clicked...");

        return new Replace();
    }
}
