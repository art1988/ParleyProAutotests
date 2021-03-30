package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Form that appears after clicking on READY FOR SIGNATURE button
 */
public class ReadyToSign
{
    private SelenideElement okButton = $(".button.btn-common.btn.btn-primary");

    private static Logger logger = Logger.getLogger(ReadyToSign.class);

    public ReadyToSign()
    {
        $(".modal-body-title").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("You are confirming that you are ready to sign this contract."));
        $(".modal-body-description").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Your counterparty will recieve an email notification and initiate the signature process"));
    }

    public void clickOk()
    {
        okButton.click();

        logger.info("OK button was clicked...");

        $(".modal-content").should(Condition.disappear);
    }
}
