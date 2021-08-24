package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;

public class EmailContract
{
    private SelenideElement emailContractButton = $(".button.btn-common.btn.btn-primary");

    private static Logger logger = Logger.getLogger(EmailContract.class);


    public EmailContract()
    {
        $(".modal-title").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Email contract"));
        $(".select__loading").waitUntil(Condition.disappear, 15_000); // spinner that appears inside 'Counterparty Chief Negotiator' field

        emailContractButton.shouldBe(Condition.visible);
    }

    /**
     * Clicks by blue link '+ Add counterparty users'
     * @return
     */
    public EmailContract clickAddCounterpartyUsers()
    {
        $(".share-documents__expand").click();

        logger.info("'+ Add counterparty users' link was clicked");

        return this;
    }

    /**
     * Sets 'Additional Counterparty users' field
     * @param email
     * @return
     */
    public EmailContract setEmailOfAdditionalCounterpartyUser(String email)
    {
        WebElement input = Selenide.executeJavaScript("return $('span:contains(\"Additional Counterparty users\")').parent().parent().find(\"input\")[0]");
        $(input).sendKeys(email);
        $(input).sendKeys(Keys.ARROW_DOWN);
        $(input).pressEnter();

        logger.info(email + " was set as Additional Counterparty User...");

        return this;
    }

    public void clickEmailContract()
    {
        emailContractButton.click();

        logger.info("EMAIL CONTRACT button was clicked...");

        $(".modal-content").waitUntil(Condition.disappear, 10_000);
    }
}
