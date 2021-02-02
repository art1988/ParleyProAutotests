package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents form that appears after clicking on SEND INVITE button
 */
public class SendInvitation
{
    private SelenideElement nextButton = $(".button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(SendInvitation.class);

    public SendInvitation(String contractName)
    {
        $(".modal-body-title").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("You are about to send invitation to negotiate for contract \"" + contractName + "\"."));

        $(".share-documents__message").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Once sent, selected documents will be visible to the Counterparty"));
    }

    public EmailWillBeSentToTheCounterparty clickNext(boolean isClassic)
    {
        nextButton.click();

        logger.info("NEXT button was clicked...");

        return new EmailWillBeSentToTheCounterparty(isClassic);
    }
}
