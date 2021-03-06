package pages.subelements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.ClauseLibrary;
import forms.StartExternalDiscussion;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CKEditorActive
{
    private SelenideElement postButton   = $("#create-discussion__submit, #create-post__submit, .js-dicussion-post-submit, .documents-pdf-discussion-footer__form-footer-buttons button[type='submit']");
    private SelenideElement cancelButton = $("button[class*='create-discussion__cancel']");
    private SelenideElement clauseButton = $(".create-discussion__clause");


    private static Logger logger = Logger.getLogger(CKEditorActive.class);

    public CKEditorActive()
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        $$(".editor-area").first().waitUntil(Condition.visible, 15_000).waitUntil(Condition.enabled, 15_000);
        $$(".editor-area").last().waitUntil(Condition.visible, 15_000).waitUntil(Condition.enabled, 15_000);

        try
        {
            Thread.sleep(1_000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Sets text inside active CKEDITOR via insertText() method
     * @param text
     */
    public CKEditorActive setText(String text) throws InterruptedException
    {
        Waiter.smartWaitUntilVisible("$('.editor-area').eq(1)");
        Waiter.smartWaitUntilVisible("$('.editor-area').eq(0)");

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
     * Get separate WebElement of comment field of active CKEditor.
     * May be useful to simulate typing, like typing '@' symbol to invoke popup for mentioning.
     * @return
     */
    public WebElement getCommentInstance()
    {
        StringBuffer jsCode = new StringBuffer("var names = [];");
        jsCode.append("for (var i in CKEDITOR.instances) { names.push(CKEDITOR.instances[i]) }");
        jsCode.append("var comment_instance = names[1];");
        jsCode.append("return $(comment_instance.element.$).next().find(\"div[contenteditable='true']\")[0];");

        WebElement commentInputField = Selenide.executeJavaScript(jsCode.toString());

        return commentInputField;
    }

    /**
     * Use this method _only_ for setting comments for uploaded PDF documents
     * @param comment
     * @return
     * @throws InterruptedException
     */
    public CKEditorActive setCommentForPDFDiscussion(String comment) throws InterruptedException
    {
        $(".editor-area").shouldBe(Condition.visible);

        Thread.sleep(1_000);

        StringBuffer jsCode = new StringBuffer("var names = [];");
        jsCode.append("for (var i in CKEDITOR.instances) { names.push(CKEDITOR.instances[i]) }");
        jsCode.append("var editor_instance = names[0];");
        jsCode.append("var comment_instance = names[1];");
        jsCode.append("editor_instance.focusManager.focus();");
        jsCode.append("editor_instance.insertText('" + comment + "')");

        Selenide.executeJavaScript(jsCode.toString());

        Thread.sleep(1_000); // 1-second delay - necessary for correct saving of text

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
            $(editorArea).sendKeys( keys[keyIndex] ); Thread.sleep(500);
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
        Selenide.executeJavaScript("$('.post-state-radio input[id^=\"INT\"]').next().click()"); // for PDF's docs

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
        Selenide.executeJavaScript("$('.post-state-radio input[id^=\"EXT\"]').next().click()"); // for PDF's docs

        logger.info("External radio button was selected...");

        return this;
    }

    /**
     * Use this method to post Internal or Queued discussion
     */
    public void clickPost()
    {
        postButton.waitUntil(Condition.enabled, 20_000).click();

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
        postButton.waitUntil(Condition.enabled, 20_000).click();

        logger.info("POST button was clicked...");

        return new StartExternalDiscussion(paragraphText, cpOrganization);
    }

    /**
     * Clicks by (?) button
     */
    public ClauseLibrary clickClauseButton()
    {
        clauseButton.shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("Clause button (?) has been clicked...");

        return new ClauseLibrary();
    }

    /**
     * Click by gray 'X' button to close editor
     */
    public void clickCancel()
    {
        cancelButton.click();

        logger.info("Cancel button was clicked...");
    }
}
