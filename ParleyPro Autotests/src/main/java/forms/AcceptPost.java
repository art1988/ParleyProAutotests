package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import constants.AcceptTypes;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Class represents form that appears after clicking of Accept button of opened discussion
 */
public class AcceptPost
{
    private AcceptTypes     acceptType;
    private SelenideElement title            = $(".modal-body-title");
    private SelenideElement acceptTextButton = $(".discussion2-close-confirm__body .btn-primary");


    private static Logger logger = Logger.getLogger(AcceptPost.class);

    public AcceptPost(AcceptTypes acceptType)
    {
        this.acceptType = acceptType;

        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText(acceptType.getTitle()));
        $(".modal-body-description").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText(acceptType.getMessage()));
    }

    public void clickAcceptText()
    {
        acceptTextButton.click();

        logger.info("Accept text button was clicked...");
    }
}
