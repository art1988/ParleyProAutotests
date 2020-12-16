package pages.subelements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;

/**
 * Right side panel that appears for templates and consist of different fields
 */
public class FieldsPanel
{
    private SelenideElement saveButton = $(".button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(FieldsPanel.class);

    public FieldsPanel()
    {
        $(".spinner").waitUntil(Condition.disappear, 7_000);

        $(".documents-placeholders__title").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Fields"));

        $(".documents-placeholders__description").waitUntil(Condition.visible, 7_000).shouldHave(
         Condition.exactText("Review and fill in your fields. Parley Pro will automatically update all places in the document where " +
                            "a specific field was added.\nBy clicking on each field, you can navigate to the location in the document."));
    }

    public String getContractName()
    {
        return Selenide.executeJavaScript("return $('.input__label:contains(\"Contract name\")').parent().find(\"textarea\").text()");
    }

    public String getContractDueDate()
    {
        return Selenide.executeJavaScript("return $('.input__label:contains(\"Contract due date\")').parent().find(\"textarea\").text()");
    }

    public String getContractCategory()
    {
        return Selenide.executeJavaScript("return $('.input__label:contains(\"Contract category\")').parent().find(\"textarea\").text()");
    }

    public String getContractRegion()
    {
        return Selenide.executeJavaScript("return $('.input__label:contains(\"Contract region\")').parent().find(\"textarea\").text()");
    }

    /**
     * Set custom field
     * @param customFieldName
     * @param value
     */
    public void setValueForCustomField(String customFieldName, String value) throws InterruptedException
    {
        WebElement textAreaWE = Selenide.executeJavaScript("return $('.input__label:contains(\"" + customFieldName + "\")').parent().find(\"textarea\")[0]");
        $(textAreaWE).sendKeys(value);

        Thread.sleep(1_000); // Sleep 1 sec so that document view is also updated
    }

    public void clickSaveButton()
    {
        saveButton.click();

        logger.info("SAVE button was clicked...");
    }
}
