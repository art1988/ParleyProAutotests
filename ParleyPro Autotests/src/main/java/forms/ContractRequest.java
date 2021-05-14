package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;

/**
 * Form that appears after clicking on '+ NEW REQUEST' button
 */
public class ContractRequest
{
    private SelenideElement submitRequestButton = $("._button.scheme_blue.size_lg");
    private SelenideElement cancelButton        = $("._button.scheme_gray.size_lg");

    private static Logger logger = Logger.getLogger(ContractRequest.class);

    public ContractRequest()
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);
        $(".modal-title").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Contract Request"));
        $(".upload-field__head").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Add documents"));

        submitRequestButton.shouldBe(Condition.visible).shouldBe(Condition.enabled);
        cancelButton.shouldBe(Condition.visible).shouldBe(Condition.enabled);
    }

    public void selectValueForField(String fieldName, String value)
    {
        // set id = fieldName
        Selenide.executeJavaScript("$('.row label:contains(\"" + fieldName + "\")').parent().find(\"input\").attr('id', '" + fieldName + "')");
        $("#" + fieldName).sendKeys(value);
        $("#" + fieldName).pressEnter();
    }

    /**
     * General method for setting value for select field
     * @param fieldName
     * @param value
     */
    public void setValueForSelect(String fieldName, String value)
    {
        WebElement selectElement = Selenide.executeJavaScript("return $('.input__label-title:contains(\"" + fieldName + "\")').parent().next().find(\"input\")[0]");
        $(selectElement).sendKeys(value);
    }

    /**
     * Set _one_ contract type
     * @param type
     */
    public void setContractType(String type)
    {
        $("input[data-id=\"contracttype\"]").click(); // open Contract type dropdown

        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('" + type + "')\").click()");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);
        $("input[data-id=\"contracttype\"]").click(); // click by input to collapse dropdown
    }

    public void clickCancel()
    {
        cancelButton.click();

        logger.info("CANCEL button was clicked.");
    }
}
