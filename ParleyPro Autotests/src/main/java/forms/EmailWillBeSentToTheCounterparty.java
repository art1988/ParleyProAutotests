package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

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
        $(".select__loading").waitUntil(Condition.disappear, 7_000);

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

    public void setCounterpartyOrganization(String organization) throws InterruptedException
    {
        counterpartyOrganizationField.clear();
        counterpartyOrganizationField.sendKeys(organization);
        Thread.sleep(500);
        counterpartyOrganizationField.sendKeys(Keys.DOWN);
        Thread.sleep(500);
        counterpartyOrganizationField.pressEnter();
        Thread.sleep(500);
    }

    public String getCounterpartyOrganization()
    {
        return counterpartyOrganizationField.getValue();
    }

    public void setCounterpartyChiefNegotiator(String chiefNegotiator) throws InterruptedException
    {
        counterpartyChiefNegotiatorField.clear();
        counterpartyChiefNegotiatorField.sendKeys(chiefNegotiator);
        Thread.sleep(500);
        counterpartyChiefNegotiatorField.sendKeys(Keys.DOWN);
        Thread.sleep(500);
        counterpartyChiefNegotiatorField.pressEnter();
        Thread.sleep(500);
    }

    public String getCounterpartyChiefNegotiator()
    {
        return counterpartyChiefNegotiatorField.getValue();
    }

    /**
     * Click START button.
     * Or this button may have SEND name in case if Send Invite button was clicked.
     */
    public void clickStart()
    {
        startButton.click();

        logger.info("Start button was clicked");

        title.waitUntil(Condition.disappear, 25_000);
    }

}
