package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class StartNegotiation
{
    private SelenideElement title = $(".modal-body-title");



    private static Logger logger = Logger.getLogger(StartNegotiation.class);

    public StartNegotiation(String contractName, boolean isClassic)
    {
        title.waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("You are about to start negotiation for contract \"" + contractName + "\"."));

        if( isClassic )
        {
            $(".share-documents__message").shouldBe(Condition.visible).shouldHave(Condition.exactText("Your Counterparty will not be notified and no contract will be emailed at this point"));
        }
        else
        {
            $(".share-documents__message").shouldBe(Condition.visible).shouldHave(Condition.exactText("Once started, selected documents will be visible to the Counterparty"));
        }
    }

    public EmailWillBeSentToTheCounterparty clickNext(boolean isClassic)
    {
        Selenide.executeJavaScript("$('.modal-footer button:contains(\"NEXT\")').click()");

        logger.info("Next button was clicked");

        return new EmailWillBeSentToTheCounterparty(isClassic);
    }
}
