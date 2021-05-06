package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.editor_toolbar.TableProperties;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;

/**
 * This class represents opened CKEditor (WYSIWYG editor with buttons).
 * Can be invoked via EDIT DOCUMENT button on Draft stage or via Format -> Online menu option
 */
public class EditDocumentPage
{
    private SelenideElement documentName = $(".editor-popup__name");


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
        WebElement saveButton = Selenide.executeJavaScript("return $('.modal-content button span:contains(\"Save\")')[0]");
        $(saveButton).click();

        logger.info("Save button was clicked");
    }

    public void clickCancel()
    {
        WebElement cancelButton = Selenide.executeJavaScript("return $('.modal-content button span:contains(\"Cancel\")')[0]");
        $(cancelButton).click();

        logger.info("Cancel button was clicked");
    }
}
