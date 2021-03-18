package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents form that appears after clicking on 'Link values' link
 */
public class LinkedValues
{
    private SelenideElement associatedField = $(".modal-body input");
    private SelenideElement linkButton      = $(".modal-footer .button.btn-common.btn.btn-primary");

    private static Logger logger = Logger.getLogger(LinkedValues.class);

    public LinkedValues()
    {
        $(".modal-body-title").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Linked values"));
        $(".modal-body-description").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("The field values will be displayed depending on the selection in the associated field."));
    }

    public void selectAssociatedField(String field) throws InterruptedException
    {
        associatedField.setValue(field);
        Thread.sleep(500);

        associatedField.sendKeys(Keys.ENTER);
        Thread.sleep(500);

        logger.info("Associated field " + field + " was selected");
    }

    public void clickLink()
    {
        linkButton.click();

        logger.info("LINK button was clicked");

        $(".modal-content").waitUntil(Condition.disappear, 7_000);
    }
}
