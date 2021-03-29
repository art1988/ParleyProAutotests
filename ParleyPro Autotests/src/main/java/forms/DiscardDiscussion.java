package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

/**
 * Confirmation form that appears after clicking by DISCARD DISCUSSION button
 * or by RESOLVE DISCUSSION button
 */
public class DiscardDiscussion
{
    private SelenideElement title         = $(".modal-body-title");
    private SelenideElement discardButton = $(".js-close-discussion-confirm"); // the same css locator has RESOLVE DISCUSSION button


    private static Logger logger = Logger.getLogger(DiscardDiscussion.class);

    public DiscardDiscussion()
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Are you sure you want to close this discussion?"));

        Waiter.smartWaitUntilVisible("$('.js-close-discussion-confirm')");

        try
        {
            Thread.sleep(1_000); // explicit wait for test stability
        }
        catch (InterruptedException e)
        {
            logger.error(e.getMessage(), e);
        }
    }

    public void clickDiscardDiscussion()
    {
        discardButton.click();

        logger.info("Discard discussion was clicked");
    }

    public void clickResolveDiscussion()
    {
        discardButton.click();

        logger.info("Resolve discussion was clicked");
    }
}
