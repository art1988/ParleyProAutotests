package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.editor_toolbar.TableProperties;
import forms.editor_toolbar.TextColorPicker;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

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
            documentName.shouldBe(Condition.visible).shouldHave(Condition.exactText(documentTitle + "\n" + "FORMATTING"));
        }
        else
        {
            documentName.shouldBe(Condition.visible).shouldHave(Condition.exactText(documentTitle));
        }

        $(".editor-popup__body").shouldBe(Condition.visible);
        $("#editor-toolbar").shouldBe(Condition.visible);
    }

    public TextColorPicker clickByTextColor()
    {
        $("#editor-toolbar a[title='Text Color']").shouldBe(Condition.visible).click();

        logger.info("Text Color button was clicked");

        return new TextColorPicker();
    }

    public TableProperties insertTable()
    {
        $("#editor-toolbar a[title='Table']").shouldBe(Condition.visible).click();

        logger.info("Insert Table button was clicked");

        return new TableProperties();
    }

    public WebElement getEditorBodyInstance()
    {
        StringBuffer jsCode = new StringBuffer("var names = [];");
        jsCode.append("for (var i in CKEDITOR.instances) { names.push(CKEDITOR.instances[i]) }");
        jsCode.append("var editor_instance = names[0];");
        jsCode.append("return $(editor_instance.element.$)[0];");

        WebElement editorBody = Selenide.executeJavaScript(jsCode.toString());

        return editorBody;
    }

    public void clickSave()
    {
        $$(".modal-content button").filterBy(Condition.text("Save")).first().click();

        logger.info("Save button was clicked");

        $(".modal-content").should(Condition.disappear);
    }

    public void clickCancel()
    {
        $$(".modal-content button").filterBy(Condition.text("Cancel")).first().click();

        logger.info("Cancel button was clicked");
    }
}
