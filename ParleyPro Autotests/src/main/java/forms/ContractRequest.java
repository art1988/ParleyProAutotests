package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

/**
 * Form that appears after clicking on '+ NEW REQUEST' button or by editing of existing Contract Request.
 */
public class ContractRequest
{
    private SelenideElement submitRequestButton = $("._button.scheme_blue.size_lg"); // the same for 'Update Request'
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

    /**
     * Use this in case of edit mode.
     * @param editMode just marker
     */
    public ContractRequest(boolean editMode)
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);
        $(".modal-title").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Edit Contract Request"));
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

        $(selectElement).sendKeys(Keys.BACK_SPACE); // to clear previous value
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

    /**
     * Set contract currency
     * @param currency may be USD, EUR, GBP, etc.
     */
    public void setContractCurrency(String currency)
    {
        Selenide.executeJavaScript("$('.currency-input.input button').click()"); // Open currency dropdown
        Selenide.executeJavaScript("$('.currency-input.input button').next().find(\"a:contains('" + currency + "')\")[0].click()"); // Set currency

        logger.info("The following currency was selected: " + currency);
    }

    public void setContractValue(String value)
    {
        $("#contractvalue").setValue(value);
    }

    public void uploadDocuments(File[] filesToUpload)
    {
        // 1. make <input> visible
        Selenide.executeJavaScript("$('.upload__body input').css(\"height\",\"auto\")");
        Selenide.executeJavaScript("$('.upload__body input').css(\"visibility\",\"visible\")");
        Selenide.executeJavaScript("$('.upload__body input').css(\"display\",\"block\")");

        // 2. trying to upload...
        SelenideElement uploadExecutedDocumentsButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

        uploadExecutedDocumentsButton.uploadFile(filesToUpload);
    }

    public void clickCancel()
    {
        cancelButton.click();

        logger.info("CANCEL button was clicked.");
    }

    public void clickSubmitRequest()
    {
        submitRequestButton.click();

        logger.info("SUBMIT REQUEST button was clicked.");
    }

    public void clickUpdateRequest()
    {
        submitRequestButton.click();

        logger.info("UPDATE REQUEST button was clicked.");
    }
}
