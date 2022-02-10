package forms.add;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.or;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents form that appears after clicking on '+ Add related field' link
 */
public class AddRelatedField
{
    private SelenideElement makeRelatedButton = $("._button.scheme_blue, .modal-footer .button.btn-common.btn.btn-primary");

    private static Logger logger = Logger.getLogger(AddRelatedField.class);

    public AddRelatedField()
    {
        $(".modal-body-title").waitUntil(Condition.visible, 30_000)
                .shouldHave(Condition.exactText("Related field"));

        $(".modal-body-description").waitUntil(Condition.visible, 30_000)
                .shouldHave(or("message on popup", Condition.exactText("Select the Custom field and related field."),
                                                         Condition.exactText("Select the categories and related field."),
                                                         Condition.exactText("Select the types and related field.")));
    }

    /**
     * Selects value from dropdown for chosen field ( left side dropdown )
     * @param fieldName name of field
     * @param value value to be set
     */
    public AddRelatedField selectValueForField(String fieldName, String value)
    {
        // Set id dynamically for input
        Selenide.executeJavaScript("$('.input__label-title:contains(\"" + fieldName + "\")').parent().parent().find(\"input\").attr('id', 'inp')");

        $("#inp").sendKeys(value);
        $("#inp").sendKeys(Keys.DOWN);
        $("#inp").pressEnter();

        return this;
    }

    /**
     * Selects checkbox under Fields dropdown ( right side dropdown )
     * @param fieldToSelect name of field to be checked
     */
    public AddRelatedField selectRelatedField(String fieldToSelect)
    {
        if( $(".modal-body input[data-label='Fields']").isDisplayed() )
        {
            $("input[data-label='Fields']").click(); // expand dropdown
            $$(".multi-select__option span").find(exactText(fieldToSelect)).click(); // select value
            $("input[data-label='Fields']").click(); // collapse dropdown
        }
        else
        {
            // set id
            Selenide.executeJavaScript("$('.input__label-title:contains(\"Related field\")').parent().parent().find(\"input\").attr('id', 'relatedFieldId')");

            $("#relatedFieldId").click(); // expand dropdown
            $$(".multi-select__option span").find(exactText(fieldToSelect)).click(); // select value
            $("#relatedFieldId").click(); // collapse dropdown
        }

        return this;
    }

    public void clickMakeRelated()
    {
        makeRelatedButton.click();

        logger.info("MAKE RELATED button was clicked");

        $(".modal-content").waitUntil(Condition.disappear, 30_000);
    }
}
