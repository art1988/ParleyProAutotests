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

    public StartNegotiation(String contractName)
    {
        title.waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("You are about to start negotiation for contract \"" + contractName + "\"."));

        $(".share-documents__message").shouldBe(Condition.visible).shouldHave(Condition.text("Once started, selected documents will be visible to the Counterparty"));
    }

    public EmailWillBeSentToTheCounterparty clickNext()
    {
        Selenide.executeJavaScript("$('.modal-footer button:contains(\"NEXT\")').click()");

        logger.info("Next button was clicked");

        return new EmailWillBeSentToTheCounterparty();
    }
}
