package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import pages.subelements.CKEditorActive;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents right sidebar that appears after clicking by blue button 'Start discussion' for uploaded PDF's
 */
public class OpenedDiscussionPDF
{
    private static Logger logger = Logger.getLogger(OpenedDiscussionPDF.class);


    public OpenedDiscussionPDF()
    {
        $(".spinner").should(Condition.disappear);
        $(".documents-pdf-discussion-header__title").shouldBe(Condition.visible).shouldHave(Condition.text("Discussion"));
    }

    public CKEditorActive clickAddComment()
    {
        $(".documents-pdf-discussion-footer__input-icon").shouldBe(Condition.visible, Condition.enabled).click();

        return new CKEditorActive();
    }

    public void clickMakeExternal(String textInPost)
    {
        WebElement makeExternalButton = Selenide.executeJavaScript("return $('.documents-pdf-discussion-post .documents-pdf-discussion-post__comment:contains(\"" + textInPost + "\")').closest('.documents-pdf-discussion-post').find('span:contains(\"MAKE EXTERNAL\")')[0]");
        $(makeExternalButton).shouldBe(Condition.enabled).click();

        logger.info("MAKE EXTERNAL button was clicked for post");
    }

    public void closePanel()
    {
        $(".documents-pdf-discussion__close").click();
    }
}
