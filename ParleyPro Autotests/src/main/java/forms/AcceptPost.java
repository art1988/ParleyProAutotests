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
    private SelenideElement acceptTextButton = $(".js-discssion-accept-confirm");


    private static Logger logger = Logger.getLogger(AcceptPost.class);

    public AcceptPost(AcceptTypes acceptType)
    {
        $(".modal-title").shouldBe(Condition.visible).shouldHave(Condition.exactText(acceptType.getTitle()));
        $(".modal-description").shouldBe(Condition.visible).shouldHave(Condition.exactText(acceptType.getMessage()));
    }

    public void clickAcceptText()
    {
        acceptTextButton.click();

        logger.info("Accept text button was clicked...");

        $(".modal-content").should(Condition.disappear);
    }
}
