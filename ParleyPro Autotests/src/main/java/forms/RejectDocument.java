package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class RejectDocument
{
    private SelenideElement title            = $(".modal-body-title");
    private SelenideElement commentsTextArea = $(".input textarea");
    private SelenideElement rejectButton     = $(".button.btn-common.btn.btn-primary");

    private static Logger logger = Logger.getLogger(RejectDocument.class);


    public RejectDocument(String documentName)
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Are you sure you want to reject document \"" + documentName + "\"?"));
        rejectButton.waitUntil(Condition.visible, 7_000);
    }

    public void setComments(String comment) throws InterruptedException
    {
        commentsTextArea.sendKeys(comment);

        Thread.sleep(500);
    }

    public void clickReject()
    {
        rejectButton.click();

        logger.info("REJECT button was clicked...");
    }

}
