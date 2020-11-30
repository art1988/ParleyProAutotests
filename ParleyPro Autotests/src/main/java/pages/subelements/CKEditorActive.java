package pages.subelements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

public class CKEditorActive
{
    private SelenideElement editorArea = $(".editor-area");
    private SelenideElement postButton = $("#create-discussion__submit");


    private static Logger logger = Logger.getLogger(CKEditorActive.class);

    public CKEditorActive()
    {
        $(".spinner").waitUntil(Condition.disappear, 10_000);
        editorArea.waitUntil(Condition.visible, 5_000).shouldBe(Condition.visible);

        Waiter.smartWaitUntilVisible("$('.create-discussion__comment .editor-area')");
    }

    /**
     * Sets text inside active CKEDITOR
     * @param text
     */
    public void setText(String text) throws InterruptedException
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
    }

    /**
     * Sets comment inside active CKEDITOR
     * @param comment
     */
    public void setComment(String comment) throws InterruptedException
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
    }

    /**
     * Click by Internal radio button
     */
    public void selectInternal()
    {
        Selenide.executeJavaScript("$('.create-discussion__states label:contains(\"Internal\")').parent().find(\"input\").click()");

        logger.info("Internal radio button was selected...");
    }

    /**
     * Click by Queued radio button
     */
    public void selectQueued()
    {
        Selenide.executeJavaScript("$('.create-discussion__states label:contains(\"Que\")').parent().find(\"input\").click()");

        logger.info("Queued radio button was selected...");
    }

    /**
     * Click by External radio button
     */
    public void selectExternal()
    {
        Selenide.executeJavaScript("$('.create-discussion__states label:contains(\"Exter\")').parent().find(\"input\").click()");

        logger.info("External radio button was selected...");
    }

    public void clickPost()
    {
        postButton.waitUntil(Condition.enabled, 6_000).click();

        logger.info("POST button was clicked...");
    }
}
