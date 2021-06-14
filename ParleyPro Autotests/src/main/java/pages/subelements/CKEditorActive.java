package pages.subelements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.StartExternalDiscussion;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CKEditorActive
{
    private SelenideElement postButton = $("#create-discussion__submit, #create-post__submit");


    private static Logger logger = Logger.getLogger(CKEditorActive.class);

    public CKEditorActive()
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        $$(".editor-area").first().waitUntil(Condition.visible, 15_000).waitUntil(Condition.enabled, 15_000);
        $$(".editor-area").last().waitUntil(Condition.visible, 15_000).waitUntil(Condition.enabled, 15_000);
    }

    /**
     * Sets text inside active CKEDITOR via insertText() method
     * @param text
     */
    public CKEditorActive setText(String text) throws InterruptedException
    {
        Waiter.smartWaitUntilVisible("$('.editor-area').eq(1)");

        Thread.sleep(1_000);

        StringBuffer jsCode = new StringBuffer("var names = [];");
        jsCode.append("for (var i in CKEDITOR.instances) { names.push(CKEDITOR.instances[i]) }");
        jsCode.append("var editor_instance = names[0];");
        jsCode.append("var comment_instance = names[1];");
        jsCode.append("editor_instance.insertText('" + text + "')");

        Selenide.executeJavaScript(jsCode.toString());

        Thread.sleep(1_000); // 1 second delay - necessary for correct saving of text

        return this;
    }

    /**
     * Sets html text inside active CKEDITOR via insertHtml() method
     * @param htmlText
     */
    public CKEditorActive insertHtml(String htmlText) throws InterruptedException
    {
        Waiter.smartWaitUntilVisible("$('.editor-area').eq(1)");

        Thread.sleep(1_000);

        StringBuffer jsCode = new StringBuffer("var names = [];");
        jsCode.append("for (var i in CKEDITOR.instances) { names.push(CKEDITOR.instances[i]) }");
        jsCode.append("var editor_instance = names[0];");
        jsCode.append("var comment_instance = names[1];");
        jsCode.append("editor_instance.insertHtml('" + htmlText + "')");

        Selenide.executeJavaScript(jsCode.toString());

        Thread.sleep(1_000); // 1 second delay - necessary for correct saving of text

        return this;
    }

    /**
     * Sets comment inside active CKEDITOR
     * @param comment
     */
    public CKEditorActive setComment(String comment) throws InterruptedException
    {
        Waiter.smartWaitUntilVisible("$('.editor-area').eq(1)");

        Thread.sleep(1_000);

        StringBuffer jsCode = new StringBuffer("var names = [];");
        jsCode.append("for (var i in CKEDITOR.instances) { names.push(CKEDITOR.instances[i]) }");
        jsCode.append("var editor_instance = names[0];");
        jsCode.append("var comment_instance = names[1];");
        jsCode.append("comment_instance.insertText('" + comment + "')");

        Selenide.executeJavaScript(jsCode.toString());

        Thread.sleep(1_000); // 1 second delay - necessary for correct saving of comment

        return this;
    }

    /**
     * Selects all text inside opened editor and presses DEL key to delete text.
     * @return
     * @throws InterruptedException
     */
    public CKEditorActive deleteAllText() throws InterruptedException
    {
        Waiter.smartWaitUntilVisible("$('.editor-area').eq(1)");

        Thread.sleep(1_000);

        StringBuffer jsCode = new StringBuffer("var names = [];");
        jsCode.append("for (var i in CKEDITOR.instances) { names.push(CKEDITOR.instances[i]) }");
        jsCode.append("var editor_instance = names[0];");
        jsCode.append("editor_instance.document.$.execCommand( 'SelectAll', false, null );"); // select all text

        Selenide.executeJavaScript(jsCode.toString());
        Thread.sleep(500);

        jsCode = new StringBuffer("var names = [];");
        jsCode.append("for (var i in CKEDITOR.instances) { names.push(CKEDITOR.instances[i]) }");
        jsCode.append("var editor_instance = names[0];");
        jsCode.append("return $(editor_instance.element.$).next().find(\"div[contenteditable='true']\")[0]"); // get exact <div> of editable area

        WebElement editorArea = Selenide.executeJavaScript(jsCode.toString());

        $(editorArea).sendKeys(Keys.DELETE);

        Thread.sleep(1_000);

        return this;
    }

    /**
     * More delicate method that allows to send array of specific keys inside active editor_instance of active CKEditor.
     * For example, need to place cursor at the beginning of a line, then press DEL key x3 times, type "abc" character by character and so on.
     * @param keys the array of Keys that will be send
     * @return
     * @throws InterruptedException
     */
    public CKEditorActive sendSpecificKeys( CharSequence keys[] ) throws InterruptedException
    {
        Waiter.smartWaitUntilVisible("$('.editor-area').eq(1)");

        Thread.sleep(1_000);

        StringBuffer jsCode = new StringBuffer("var names = [];");
        jsCode.append("for (var i in CKEDITOR.instances) { names.push(CKEDITOR.instances[i]) }");
        jsCode.append("var editor_instance = names[0];");
        jsCode.append("return $(editor_instance.element.$).next().find(\"div[contenteditable='true']\")[0]"); // get exact <div> of editable area

        WebElement editorArea = Selenide.executeJavaScript(jsCode.toString());

        for( int keyIndex = 0; keyIndex < keys.length; keyIndex++ )
        {
            $(editorArea).sendKeys( keys[keyIndex] ); Thread.sleep(1_000);
        }

        Thread.sleep(1_000);

        return this;
    }

    /**
     * Click by Internal radio button
     */
    public CKEditorActive selectInternal()
    {
        Selenide.executeJavaScript("$('.create-discussion__states input[id^=\"INT\"]').next().click()");

        logger.info("Internal radio button was selected...");

        return this;
    }

    /**
     * Click by Queued radio button.
     * Important: make sure that this call is the last one in call chain (before POST).
     * Because in case of deleteAllText() this may behave incorrectly.
     */
    public CKEditorActive selectQueued()
    {
        Selenide.executeJavaScript("$('input[id^=\"QUE\"]').next().click()");

        logger.info("Queued radio button was selected...");

        return this;
    }

    /**
     * Click by External radio button
     */
    public CKEditorActive selectExternal()
    {
        Selenide.executeJavaScript("$('.create-discussion__states input[id^=\"EXT\"]').next().click()");

        logger.info("External radio button was selected...");

        return this;
    }

    /**
     * Use this method to post Internal or Queued discussion
     */
    public void clickPost()
    {
        postButton.waitUntil(Condition.enabled, 6_000).click();

        logger.info("POST button was clicked...");
    }

    /**
     * Use this method to post External discussion
     * @param paragraphText text of the paragraph for which external post was invoked. In case if paragraph is too long, text will be truncated with ... at the end
     * @param cpOrganization name of counterparty organization
     * @return
     */
    public StartExternalDiscussion clickPost(String paragraphText, String cpOrganization)
    {
        postButton.waitUntil(Condition.enabled, 6_000).click();

        logger.info("POST button was clicked...");

        return new StartExternalDiscussion(paragraphText, cpOrganization);
    }
}
