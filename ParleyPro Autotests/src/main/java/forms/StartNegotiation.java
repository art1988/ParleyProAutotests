package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class StartNegotiation
{
    private SelenideElement title       = $(".modal-body-title");
    private SelenideElement message     = $(".share-documents__message");
    private SelenideElement startButton = $(".button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(StartNegotiation.class);

    /**
     *
     * @param contractName name of the contract for which negotiation will be started
     * @param counterparty name of the counterparty. Use "" if it wasn't set
     * @param isClassic classic or not
     */
    public StartNegotiation(String contractName, String counterparty, boolean isClassic)
    {
        $(".spinner").waitUntil(Condition.disappear, 7_000);

        if( !counterparty.equals("") )
        {
            title.waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("You are about to start negotiation for contract \"" + contractName + "\" with " + counterparty + "."));
        }
        else
        {
            title.waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("You are about to start negotiation for contract \"" + contractName + "\"."));
        }

        if( isClassic )
        {
            message.shouldBe(Condition.visible).shouldHave(Condition.exactText("Your Counterparty will not be notified and no contract will be emailed at this point"));
        }
        else
        {
            message.shouldBe(Condition.visible).shouldHave(Condition.exactText("Once started, selected documents will be visible to the Counterparty"));
        }
    }

    public EmailWillBeSentToTheCounterparty clickNext(boolean isClassic)
    {
        Selenide.executeJavaScript("$('.modal-footer button:contains(\"NEXT\")').click()");

        logger.info("Next button was clicked");

        return new EmailWillBeSentToTheCounterparty(isClassic);
    }

    public void clickStart()
    {
        startButton.click();

        logger.info("START button was clicked");
    }
}
