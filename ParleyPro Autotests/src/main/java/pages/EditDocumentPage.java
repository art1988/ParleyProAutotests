package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import forms.editor_toolbar.TableProperties;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * This class represents opened CKEditor (WYSIWYG editor with buttons).
 * Can be invoked via EDIT DOCUMENT button on Draft stage or via Format -> Online menu option
 */
public class EditDocumentPage
{
    private SelenideElement documentName = $(".editor-popup__name");

    private SelenideElement cancelButton = $(".button.btn-small.btn-link-pseudo.btn.btn-link");
    private SelenideElement saveButton   = $(".button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(EditDocumentPage.class);

    public EditDocumentPage(String documentTitle, boolean shouldHaveFormattingLabel)
    {
        if( shouldHaveFormattingLabel )
        {
            documentName.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText(documentTitle + "\n" + "FORMATTING"));
        }
        else
        {
            documentName.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText(documentTitle));
        }

        $(".editor-popup__body").waitUntil(Condition.visible, 7_000);
        $("#editor-toolbar").waitUntil(Condition.visible, 7_000);
    }

    public TableProperties insertTable()
    {
        $("#editor-toolbar a[title='Table']").waitUntil(Condition.visible, 7__000).click();

        logger.info("Insert Table");

        return new TableProperties();
    }

    public void clickSave()
    {
        saveButton.click();

        logger.info("Save button was clicked");
    }

    public void clickCancel()
    {
        cancelButton.click();

        logger.info("Cancel button was clicked");
    }
}
