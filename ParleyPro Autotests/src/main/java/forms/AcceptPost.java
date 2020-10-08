package forms;

import com.codeborne.selenide.Condition;
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
    private AcceptTypes     acceptType;


    private static Logger logger = Logger.getLogger(AcceptPost.class);

    public AcceptPost(AcceptTypes acceptType)
    {
        this.acceptType = acceptType;

        // TODO: need to change locators after fixing of https://parley.atlassian.net/browse/PAR-12360
        Waiter.smartWaitUntilVisible("$('.modal-title, .modal-body-title')");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.modal-title, .modal-body-title').text()"), acceptType.getTitle());

        Waiter.smartWaitUntilVisible("$('.modal-body-description, .modal-description')");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.modal-body-description, .modal-description').text()"), acceptType.getMessage());
    }

    public void clickAcceptText()
    {
        // TODO: need to change locators after fixing of https://parley.atlassian.net/browse/PAR-12360
        Selenide.executeJavaScript("$('.discussion2-close-confirm__body .btn-primary, ._button__text:contains(\"Accept text\")').click()");

        logger.info("Accept text button was clicked...");
    }
}
