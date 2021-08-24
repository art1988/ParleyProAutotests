package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Popup that appears after clicking of envelope button in contract header.
 */
public class AboutToEmailContract
{
    private SelenideElement cancelButton            = $("#email-contract-cancel-button");
    private SelenideElement downloadToPreviewButton = $("#email-contract-preview-button");
    private SelenideElement nextButton              = $("#email-contract-next-button");

    private static Logger logger = Logger.getLogger(AboutToEmailContract.class);


    public AboutToEmailContract(String contractName, String counterpartyOrganization)
    {
        $(".modal-content .modal-title").waitUntil(Condition.visible, 15_000)
                .shouldHave(Condition.exactText("You are about to email contract \"" + contractName + "\" to " + counterpartyOrganization + "."));
        $(".modal-content .modal-description.warning").shouldHave(Condition.exactText("All queued posts will be converted to external automatically"));

        cancelButton.shouldBe(Condition.visible);
        downloadToPreviewButton.shouldBe(Condition.visible);
        nextButton.shouldBe(Condition.visible);
    }

    public EmailContract clickNext()
    {
        nextButton.click();

        logger.info("NEXT button was clicked...");

        return new EmailContract();
    }
}
