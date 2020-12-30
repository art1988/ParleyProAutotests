package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents form that appears after clicking on 'Resend invite' svg icon in Share form
 */
public class ResendInviteForm
{
    private ShareForm parentForm;
    private SelenideElement messageTextArea = $(".item.active .textarea.input__input");
    private SelenideElement sendButton      = $(".item.active ._button.scheme_blue.size_lg.manage-users-new__submit");


    private static Logger logger = Logger.getLogger(ResendInviteForm.class);

    public ResendInviteForm(ShareForm parentForm)
    {
        this.parentForm = parentForm;

        $(".modal-confirm-message__title").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("Resend invite"));

        sendButton.waitUntil(Condition.visible, 5_000);
    }

    public ShareForm clickSend()
    {
        sendButton.click();

        logger.info("SEND button was clicked...");

        return parentForm;
    }
}
