package forms;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Form that appears after clicking MESSAGE button that appear on user's tooltip.
 */
public class SendMessage
{
    private Logger logger = Logger.getLogger(SendMessage.class);


    public SendMessage(String sendTo)
    {
        $(".modal-body-title").shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Send message to " + sendTo + "."));
    }

    public SendMessage setMessage(String message)
    {
        $(".modal-content textarea").shouldBe(Condition.visible, Condition.enabled).sendKeys(message);

        logger.info("Message was set...");

        return this;
    }

    public void clickSend()
    {
        $(".button.btn-common.btn.btn-primary").shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("SEND button was clicked...");

        $(".modal-content").should(Condition.disappear);
    }
}
