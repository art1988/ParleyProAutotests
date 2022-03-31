package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents form that appears after clicking on 'Resend invite' svg icon on Share form or via team's tooltip
 */
public class ResendInviteForm
{
    private ShareForm parentForm;
    private SelenideElement messageTextArea = $(".item.active .textarea.input__input, .modal-body-title");
    private SelenideElement sendButton      = $(".item.active ._button.scheme_blue.size_lg.manage-users-new__submit");


    private static Logger logger = Logger.getLogger(ResendInviteForm.class);

    public ResendInviteForm(ShareForm parentForm)
    {
        this.parentForm = parentForm;

        $(".modal-confirm-message__title").shouldBe(Condition.visible).shouldHave(Condition.exactText("Resend invite"));
    }

    public ShareForm clickSend()
    {
        sendButton.shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("SEND button was clicked...");

        return parentForm;
    }

    /**
     * RESEND button is available on team's tooltip
     */
    public void clickResend()
    {
        $$(".modal-footer button").filterBy(Condition.exactText("RESEND")).first().shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("RESEND button was clicked...");
    }
}
