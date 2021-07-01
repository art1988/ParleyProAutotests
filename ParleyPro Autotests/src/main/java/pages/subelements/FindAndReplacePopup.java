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

        logger.info("REPLACE tab was clicked...");

        return this;
    }

    public FindAndReplacePopup findInDocument(String wordToFind)
    {
        findInDocumentInput.sendKeys(wordToFind);

        return this;
    }

    public FindAndReplacePopup replaceWith(String wordToReplace)
    {
        replaceWithInput.sendKeys(wordToReplace);

        return this;
    }

    /**
     * Returns Matches label, like 'Matches: 1', 'Matches: 23', etc
     * @return
     */
    public String getMatchesCount()
    {
        return $("div[class^=\"styles__footer\"] div").getText();
    }

    public Replace clickReviewAndReplaceButton()
    {
        reviewAndReplaceButton.waitUntil(Condition.enabled, 7_000).click();

        logger.info("REVIEW AND REPLACE button was clicked...");

        return new Replace();
    }
}
