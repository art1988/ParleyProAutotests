package pages.subelements;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ParagraphActionsPopup
{
    private SelenideElement insertAboveButton    = $(".js-paragraph-tooltip-insert-above");
    private SelenideElement insertBelowButton    = $(".js-paragraph-tooltip-insert-below");
    private SelenideElement deleteButton         = $(".js-paragraph-tooltip-delete");
    private SelenideElement deleteMultipleButton = $(".js-paragraph-tooltip-delete-multiple");
    private SelenideElement commentButton        = $(".js-paragraph-tooltip-comment");



    private static Logger logger = Logger.getLogger(ParagraphActionsPopup.class);

    public ParagraphActionsPopup()
    {
        // Assert that there are 5 buttons
        $$(".rc-tooltip-inner button").shouldHave(CollectionCondition.size(5));
    }

    public CKEditorActive clickDelete()
    {
        deleteButton.click();

        logger.info("Delete button was clicked...");

        return new CKEditorActive();
    }

    public CKEditorActive clickAddComment()
    {
        commentButton.click();

        logger.info("Add comment button was clicked...");

        return new CKEditorActive();
    }

    public CKEditorActive clickAddParagraphAbove()
    {
        insertAboveButton.click();

        logger.info("Add paragraph above was clicked...");

        return new CKEditorActive();
    }
}
