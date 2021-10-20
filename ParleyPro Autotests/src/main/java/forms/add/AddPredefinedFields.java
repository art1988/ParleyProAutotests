package forms.add;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents form 'Add predefined fields' that appears after clicking by 'Add predefined fields' button on Fields page
 */
public class AddPredefinedFields
{
    private SelenideElement addButton = $(".modal-footer .button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(AddPredefinedFields.class);

    public AddPredefinedFields()
    {
        $(".modal-body-title").waitUntil(Condition.visible, 30_000).shouldHave(Condition.exactText("Add predefined fields"));
        $(".modal-body-description").waitUntil(Condition.visible, 30_000).shouldHave(Condition.exactText("Select the fields to be added"));
    }

    /**
     * Check or uncheck field.
     * @param fieldName name of the field to be checked/unchecked.
     * Should be one of the following: 'Contracting region', 'Contracting country', 'Contract entity',
     * 'Contracting department', 'Contract category' or 'Contract type'
     */
    public AddPredefinedFields selectField(String fieldName)
    {
        $$(".checkbox__label").filter(Condition.exactText(fieldName)).get(0).click();

        logger.info(fieldName + " was checked");

        return this;
    }

    public void clickAdd()
    {
        addButton.click();

        logger.info("ADD button was clicked...");

        $(".modal-content").waitUntil(Condition.disappear, 10_000);
    }
}
