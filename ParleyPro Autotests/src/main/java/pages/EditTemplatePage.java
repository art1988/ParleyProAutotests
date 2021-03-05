package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.editor_toolbar.Field;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents selected template page in 'Edit mode' where editor is available and active.
 */
public class EditTemplatePage
{
    private SelenideElement cancelButton  = $(".button.btn-small.btn-link-pseudo.btn.btn-link");
    private SelenideElement publishButton = $(".button.btn-small.btn.btn-primary");



    private static Logger logger = Logger.getLogger(EditTemplatePage.class);

    public EditTemplatePage()
    {
        $(".spinner").waitUntil(Condition.disappear, 20_000);

        $(".rename > span").waitUntil(Condition.visible, 20_000);
        $(".cke_inner").waitUntil(Condition.visible, 20_000);
    }

    /**
     * Click by 'Add a smart field' button and choose type
     * @param smartFieldType may be: Contract name, Contract due date, Contract category, Contract region etc.
     */
    public void addSmartField(String smartFieldType) throws InterruptedException
    {
        // click on 'Add a smart field' button
        $("#editor-toolbar a[title='Add a smart field']").waitUntil(Condition.visible, 7__000).click();

        // Choose type
        Selenide.executeJavaScript("$($('.cke_combopanel > iframe')[0].contentDocument).find(\".cke_panel_list a:contains('" + smartFieldType + "')\").click()");

        Thread.sleep(500);
    }

    public Field addField()
    {
        $("#editor-toolbar a[title='Field']").waitUntil(Condition.visible, 7__000).click();

        return new Field();
    }

    /**
     * Adds text as the first line in opened editor.
     * @param text
     */
    public void addText(String text) throws InterruptedException
    {
        StringBuffer jsCode = new StringBuffer("var names = [];");
        jsCode.append("for (var i in CKEDITOR.instances) { names.push(CKEDITOR.instances[i]) }");
        jsCode.append("var editor_instance = names[0];");
        jsCode.append("editor_instance.insertText('" + text + "')");

        Selenide.executeJavaScript(jsCode.toString());

        Thread.sleep(1_000); // 1 second delay - necessary for correct saving of text
    }

    /**
     * Gets whole text ( as plain text ) from opened editor.
     * @return
     */
    public String getText()
    {
        StringBuffer jsCode = new StringBuffer("var names = [];");
        jsCode.append("for (var i in CKEDITOR.instances) { names.push(CKEDITOR.instances[i]) }");
        jsCode.append("var editor_instance = names[0];");
        jsCode.append("return editor_instance.editable().getText();");

        return Selenide.executeJavaScript(jsCode.toString());
    }

    public void clickPublishButton()
    {
        publishButton.click();

        logger.info("PUBLISH button was clicked");

        $(".modal-content").waitUntil(Condition.disappear, 25_000);
    }

    public void clickCancelButton()
    {
        cancelButton.click();

        logger.info("CANCEL button was clicked");

        $(".modal-content").waitUntil(Condition.disappear, 10_000);
    }
}
