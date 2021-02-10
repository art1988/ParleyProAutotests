package forms.delete;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents pop up that shows after clicking on delete icon of attached document
 */
public class DeleteAttachment
{
    private SelenideElement deleteButton = $(".button.btn-common.btn.btn-danger");


    private static Logger logger = Logger.getLogger(DeleteAttachment.class);

    public DeleteAttachment(String attachmentName)
    {
        $(".modal-body-title").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Are you sure to delete “" + attachmentName + "”?"));
        $(".modal-body-description").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("The file will be removed from the contract"));
    }

    public void clickDelete()
    {
        deleteButton.click();

        logger.info("DELETE button was clicked...");
    }
}
