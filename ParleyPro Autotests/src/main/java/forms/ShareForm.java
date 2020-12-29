package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents form that appears after clicking on SHARE button
 */
public class ShareForm
{
    private SelenideElement addParticipantField = $("#manage-users__select");

    private SelenideElement sendButton = $("._button.scheme_blue.size_lg.manage-users-new__submit");
    private SelenideElement doneButton = $(".manage-users-added__foot button");


    private static Logger logger = Logger.getLogger(ShareForm.class);

    public ShareForm(String documentName)
    {
        $(".spinner").waitUntil(Condition.disappear, 7_000);

        $(".modal-title").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Add internal users and assign roles for internal review of document “" + documentName + "”."));

        $(".manage-users-user").waitUntil(Condition.visible, 7_000);
    }

    public ShareForm addParticipant(String nameOrEmail)
    {
        addParticipantField.setValue(nameOrEmail);

        try
        {
            Thread.sleep(1_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        addParticipantField.sendKeys(Keys.ARROW_DOWN);
        addParticipantField.pressEnter();

        return this; // we are on the same form
    }

    public void clickSend()
    {
        sendButton.click();

        logger.info("SEND button was clicked...");
    }

    public void clickDone()
    {
        doneButton.click();

        logger.info("DONE button was clicked...");
    }
}
