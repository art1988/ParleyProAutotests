package forms;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import constants.AcceptTypes;
import org.apache.log4j.Logger;
import org.testng.Assert;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

/**
 * Class represents form that appears after clicking of Accept button of opened discussion
 */
public class AcceptPost
{
    private AcceptTypes acceptType;
    private SelenideElement acceptTextButton = $(".js-discssion-accept-confirm");


    private static Logger logger = Logger.getLogger(AcceptPost.class);

    public AcceptPost(AcceptTypes acceptType)
    {
        this.acceptType = acceptType;

        Waiter.smartWaitUntilVisible("$('.modal-title')");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.modal-title').text()"), acceptType.getTitle());

        Waiter.smartWaitUntilVisible("$('.modal-description')");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.modal-description').text()"), acceptType.getMessage());
    }

    public void clickAcceptText()
    {
        acceptTextButton.click();

        logger.info("Accept text button was clicked...");
    }
}
