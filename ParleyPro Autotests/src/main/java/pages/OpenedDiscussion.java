package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import constants.AcceptTypes;
import forms.AcceptPost;
import forms.DiscardDiscussion;
import forms.RevertToOriginal;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;

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
        $(".discussion2__body__scrollable-body").waitUntil(Condition.visible, 7_000);

        // First, scroll opened discussion panel to Original Text post ( because in case of multiple posts it may be hidden )
        Selenide.executeJavaScript("document.querySelector('.discussion2__body__scrollable-body').scrollTo(0,0)");

        $(".discussion2-original").waitUntil(Condition.visible, 15_000);
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
     * Click by "Non-standard term" button
     * @param tag - tag that will be added
     */
    public void clickTermButton(String tag)
    {
        termButton.click();

        logger.info("Non-standard term button was clicked");

        termInput.waitUntil(Condition.enabled, 5_000);
        termInput.setValue(tag);

        // Wait until popup with added tag is visible
        $("#react-autowhatever-1").waitUntil(Condition.visible, 5_000);
        termInput.pressEnter();
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

        $(".spinner").waitUntil(Condition.appear, 10_000);
        $(".spinner").waitUntil(Condition.disappear, 20_000);

        logger.info("MAKE QUEUED was clicked");
    }
}
