package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Form that appears after clicking on Next button in Negotiate status.
 * If it is not classic contract => Title would be "The email will be sent to the Counterparty Chief Negotiator."
 * If it is classic contract => Title would be "The Counterparty information is missing."
 *
 * Also appears after clicking SEND INVITE button (after clicking NEXT).
 */
public class EmailWillBeSentToTheCounterparty
{
    private SelenideElement title                            = $(".modal-body-title, .modal-title");
    private SelenideElement counterpartyOrganizationField    = $("#ccn-email-form-counterparty-organization, #share-ccn-form-counterparty-organization");
    private SelenideElement counterpartyChiefNegotiatorField = $("#ccn-email-form-counterparty-chief-negotiator, #share-ccn-form-counterparty-chief-negotiator");
    private SelenideElement startButton                      = $("#ccn-email-form-submit-button, #share-ccn-form-submit-button");



    private static Logger logger = Logger.getLogger(EmailWillBeSentToTheCounterparty.class);

    public EmailWillBeSentToTheCounterparty(boolean isClassic)
    {
        $(".select__loading").should(Condition.disappear);

        if( isClassic )
        {
            title.shouldBe(Condition.visible).shouldHave(Condition.exactText("The Counterparty information is missing."));
        }
        else
        {
            title.shouldBe(Condition.visible).shouldHave(Condition.exactText("The email will be sent to the Counterparty Chief Negotiator."));
        }

        counterpartyOrganizationField.shouldBe(Condition.visible);
        counterpartyChiefNegotiatorField.shouldBe(Condition.visible);
    }

    public EmailWillBeSentToTheCounterparty setCounterpartyOrganization(String organization) throws InterruptedException
    {
        counterpartyOrganizationField.clear();
        counterpartyOrganizationField.sendKeys(organization);
        Thread.sleep(500);
        counterpartyOrganizationField.sendKeys(Keys.DOWN);
        Thread.sleep(500);
        counterpartyOrganizationField.pressEnter();
        Thread.sleep(500);

        return this;
    }

    public String getCounterpartyOrganization()
    {
        return counterpartyOrganizationField.getValue();
    }

    public EmailWillBeSentToTheCounterparty setCounterpartyChiefNegotiator(String chiefNegotiator) throws InterruptedException
    {
        counterpartyChiefNegotiatorField.clear();
        counterpartyChiefNegotiatorField.sendKeys(chiefNegotiator);
        Thread.sleep(500);
        counterpartyChiefNegotiatorField.sendKeys(Keys.DOWN);
        Thread.sleep(500);
        counterpartyChiefNegotiatorField.pressEnter();
        Thread.sleep(500);

        return this;
    }

    public String getCounterpartyChiefNegotiator()
    {
        return counterpartyChiefNegotiatorField.getValue();
    }

    /**
     * Click by blue link '+ Add counterparty users'
     */
    public EmailWillBeSentToTheCounterparty clickAddCounterpartyUsers()
    {
        $$(".modal-content span").filter(Condition.text("Add counterparty users")).first().click();

        logger.info("'+ Add counterparty users' blue link was clicked");

        return this;
    }

    /**
     * Set Additional Counterparty users
     * @param user
     * @return
     * @throws InterruptedException
     */
    public EmailWillBeSentToTheCounterparty setAdditionalCounterpartyUsers(String user) throws InterruptedException
    {
        SelenideElement input = $$(".modal-content span").filter(Condition.exactText("Additional Counterparty users"))
                .first().closest("div").find("input");

        input.sendKeys(user);
        Thread.sleep(500);
        input.pressEnter();
        Thread.sleep(500);

        return this;
    }

    /**
     * Click START button.
     * Or this button may have SEND name in case if Send Invite button was clicked.
     */
    public void clickStart()
    {
        startButton.shouldBe(Condition.enabled).click();

        logger.info("Start button was clicked");

        title.should(Condition.disappear);
    }

}
