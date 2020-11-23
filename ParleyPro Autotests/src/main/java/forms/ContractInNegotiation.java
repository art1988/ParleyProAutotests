package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Popup that appears after uploading of counterparty documents
 */
public class ContractInNegotiation
{
    private SelenideElement title    = $(".modal-body-title");
    private SelenideElement body     = $(".modal-body-description");
    private SelenideElement okButton = $(".button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(ContractInNegotiation.class);

    public ContractInNegotiation(String contractName)
    {
        title.waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Contract \"" + contractName + "\" is now in Negotiation but is not visible to your Countertparty"));
        body.waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("At this time, no notifications will be sent to the Counterparty"));
    }

    public void clickOk()
    {
        okButton.click();

        logger.info("OK button was clicked");
    }
}
