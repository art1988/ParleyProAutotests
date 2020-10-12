package pages.tooltips;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.SelenideElement;
import constants.AcceptTypes;
import forms.AcceptPost;
import org.apache.log4j.Logger;
import pages.subelements.CKEditorActive;
import pages.subelements.MultipleDeleteOverlay;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ParagraphActionsPopup
{
    private SelenideElement insertAboveButton    = $(".js-paragraph-tooltip-insert-above");
    private SelenideElement insertBelowButton    = $(".js-paragraph-tooltip-insert-below");
    private SelenideElement deleteButton         = $(".js-paragraph-tooltip-delete");
    private SelenideElement deleteMultipleButton = $(".js-paragraph-tooltip-delete-multiple");
    private SelenideElement commentButton        = $(".js-paragraph-tooltip-comment");
    private SelenideElement acceptButton         = $(".js-paragraph-tooltip-accept");



    private static Logger logger = Logger.getLogger(ParagraphActionsPopup.class);

    public ParagraphActionsPopup()
    {
        // Assert that there are 3, 4 or 5 buttons. Depends on paragraph or logged user
        $$(".rc-tooltip-inner button").shouldHave(CollectionCondition.sizeGreaterThanOrEqual(3));
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

    public CKEditorActive clickAddParagraphBelow()
    {
        insertBelowButton.click();

        logger.info("Add paragraph below was clicked...");

        return new CKEditorActive();
    }

    public MultipleDeleteOverlay clickDeleteMultipleParagraphs()
    {
        deleteMultipleButton.click();

        logger.info("Delete multiple paragraphs was clicked...");

        return new MultipleDeleteOverlay();
    }

    public AcceptPost clickAcceptChangesOnParagraph(AcceptTypes acceptType)
    {
        acceptButton.click();

        logger.info("Accept changes on paragraph was clicked...");

        return new AcceptPost(acceptType);
    }
}
