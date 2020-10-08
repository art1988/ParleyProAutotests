package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

public class RevertToOriginal
{
    private SelenideElement title = $(".modal-body-title");



    private static Logger logger = Logger.getLogger(RevertToOriginal.class);

    public RevertToOriginal()
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Are you sure you want to close this discussion?"));

        Assert.assertEquals(Selenide.executeJavaScript("return $('.modal-body-description').text()"),
                "The contents of the document won't change because no text changes have been requested as a result of this discussion");
    }

    public void clickCloseDiscussion()
    {
        Selenide.executeJavaScript("$('.discussion2-close-confirm__body .btn-primary').click()");

        logger.info("Close discussion button was clicked...");
    }
}
