package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import constants.AcceptTypes;
import forms.AcceptPost;
import forms.DiscardDiscussion;
import forms.RevertToOriginal;
import forms.StartExternalDiscussion;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents right sidebar that appears after clicking by discussions icon
 */
public class OpenedDiscussion
{
    private SelenideElement closeButton             = $(".documents-discussion-panel__close");
    private SelenideElement discardDiscussionButton = $(".discussion-close-button"); // the same locator has Resolve discussion button
    private SelenideElement priorityButton          = $(".discussion-header__menu .label_priority");
    private SelenideElement termButton              = $(".discussion-header__menu .label_term");
    private SelenideElement termInput               = $(".select__input.input__input");


    private static Logger logger = Logger.getLogger(OpenedDiscussion.class);

    public OpenedDiscussion(String title)
    {
        $(".documents-pdf-discussion__container").waitUntil(Condition.visible, 40_000);
        $(".discussion2__body__scrollable-body").waitUntil(Condition.visible, 7_000);

        $(".discussion2-original").shouldBe(Condition.enabled);
        $(".discussion-header__title-name").waitUntil(Condition.visible, 7_000).shouldHave(Condition.text(title));

        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }
    }

    /**
     * Closes right panel of open discussion
     */
    public void close()
    {
        closeButton.click();

        logger.info("Close icon was clicked...");
    }

    public String getCountOfPosts()
    {
        return Selenide.executeJavaScript("return $('.documents-discussion-panel .discussion-indicator span').text()");
    }

    /**
     * Click by "Mark as high priority" button
     */
    public void clickPriorityButton()
    {
        priorityButton.click();

        logger.info("Mark as high priority button was clicked");
    }

    /**
     * Click by "Non-standard term" button and sets tag ( via hitting enter button )
     * @param tag - tag that will be added
     */
    public void setNonStandardTerm(String tag) throws InterruptedException
    {
        termButton.waitUntil(Condition.visible, 10_000)
                  .waitUntil(Condition.enabled, 10_000)
                  .click();

        logger.info("Non-standard term button was clicked");

        $(".select__loading").waitUntil(Condition.disappear, 7_000); // wait until inner spinner of input will disappear
        termInput.shouldBe(Condition.visible)
                 .shouldBe(Condition.enabled)
                 .sendKeys(tag);

        // Wait until popup with added tag is visible
        $("#react-autowhatever-1").waitUntil(Condition.visible, 5_000);
        Thread.sleep(1_000);
        $$("#react-autowhatever-1 li").first().click();
        //termInput.pressEnter(); commented due to PAR-14310
        $(".spinner").waitUntil(Condition.disappear, 10_000);
    }

    /**
     * Finds post that contains certain text and click ACCEPT button
     * @param type - type of Accept form
     * @param post - text of post which need to be accepted
     * @return
     */
    public AcceptPost clickAccept(AcceptTypes type, String post)
    {
        Selenide.executeJavaScript("$('.paragraph-discussions .discussion2-post .discussion2-post__text:contains(\"" + post + "\")').parent().next().find(\"i\").click()");

        return new AcceptPost(type);
    }

    public DiscardDiscussion clickResolveDiscussion()
    {
        discardDiscussionButton.click();

        logger.info("RESOLVE DISCUSSION was clicked...");

        return new DiscardDiscussion();
    }

    public DiscardDiscussion clickDiscardDiscussion()
    {
        discardDiscussionButton.click();

        logger.info("DISCARD DISCUSSION was clicked...");

        return new DiscardDiscussion();
    }

    public RevertToOriginal clickRevertToOriginal()
    {
        Selenide.executeJavaScript("$('.discussion2-post__foot-action.accept:contains(\"REVERT TO ORIGINAL\")').click()");

        return new RevertToOriginal();
    }

    /**
     * Click by 'Make queued' button
     * @param textInPost text of the post for which need to click. Matches by contains !
     */
    public void clickMakeQueued(String textInPost)
    {
        WebElement makeQueuedButton = Selenide.executeJavaScript("return $('.documents-discussion-panel .discussion2-post .discussion2-post__text:contains(\"" + textInPost + "\")').parent().parent().find(\".queued-post\")[0]");
        $(makeQueuedButton).shouldBe(Condition.enabled).click();

        $(".spinner").waitUntil(Condition.appear, 20_000);
        $(".spinner").waitUntil(Condition.disappear, 40_000 * 2);

        logger.info("MAKE QUEUED button was clicked for post");
    }

    /**
     * Click by 'Make external' button
     * @param textInPost text of the post for which need to click. Matches by contains !
     * @return
     */
    public StartExternalDiscussion clickMakeExternal(String textInPost)
    {
        WebElement makeExternalButton = Selenide.executeJavaScript("return $('.documents-discussion-panel .discussion2-post .discussion2-post__text:contains(\"" + textInPost + "\")').parent().parent().find(\".external-post\")[0]");
        $(makeExternalButton).shouldBe(Condition.enabled).click();

        logger.info("MAKE EXTERNAL button was clicked for post");

        return new StartExternalDiscussion("", "");
    }
}
