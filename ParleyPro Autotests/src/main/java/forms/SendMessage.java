package forms;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Form that appears after clicking by MESSAGE button.
 */
public class SendMessage
{
    private Logger logger = Logger.getLogger(SendMessage.class);


    public SendMessage(String sendTo)
    {
        $(".modal-body-title").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Send message to " + sendTo + "."));
    }

    public SendMessage setMessage(String message)
    {
        $(".modal-content textarea").sendKeys(message);

        logger.info("Message was set...");

        return this;
    }

    public void clickSend()
    {
        $(".button.btn-common.btn.btn-primary").click();

        logger.info("SEND button was clicked...");
    }
}
