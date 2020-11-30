package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Form that appears after clicking on Next button in Negotiate status.
 * If it is not classic contract => Title would be "The email will be sent to the Counterparty Chief Negotiator."
 * If it is classic contract => Title would be "The Counterparty information is missing."
 */
public class EmailWillBeSentToTheCounterparty
{
    private SelenideElement title                            = $(".modal-body-title");
    private SelenideElement counterpartyOrganizationField    = $("#ccn-email-form-counterparty-organization");
    private SelenideElement counterpartyChiefNegotiatorField = $("#ccn-email-form-counterparty-chief-negotiator");
    private SelenideElement startButton                      = $("#ccn-email-form-submit-button");



    private static Logger logger = Logger.getLogger(EmailWillBeSentToTheCounterparty.class);

    public EmailWillBeSentToTheCounterparty(boolean isClassic)
    {
        if( isClassic )
        {
            title.waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("The Counterparty information is missing."));
        }
        else
        {
            title.waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("The email will be sent to the Counterparty Chief Negotiator."));
        }

        counterpartyOrganizationField.waitUntil(Condition.visible, 6_000);
        counterpartyChiefNegotiatorField.waitUntil(Condition.visible, 6_000);
    }

    public void setCounterpartyOrganization(String organization)
    {
        counterpartyOrganizationField.clear();
        counterpartyOrganizationField.setValue(organization);
    }

    public void setCounterpartyChiefNegotiator(String chiefNegotiator)
    {
        counterpartyChiefNegotiatorField.clear();
        counterpartyChiefNegotiatorField.setValue(chiefNegotiator);
    }

    public void clickStart()
    {
        startButton.click();

        logger.info("Start button was clicked");
    }

}
