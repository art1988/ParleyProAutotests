package pages.administration.fields_breadcrumb;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.FieldType;
import forms.LinkedValues;
import forms.delete.DeleteField;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ContractFields
{
    private int fieldTypeID; // id that dynamically sets for field type
    private int valueIndex = 1; // Used in Value 1, Value 2 etc.

    private static Logger logger = Logger.getLogger(ContractFields.class);

    public ContractFields()
    {
        $$(".admin-fields__title").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.exactTexts("Summary", "Post-execution", "Contract Request"));
    }

    /**
     * This method allows to Create new field for chosen section
     * @param section may be "Summary", "Post-execution" or "Contract Request"
     * @param fieldName name of the field to be set
     * @param fieldType type of the field
     * @param isRequired required field checkmark
     */
    public void createNewFiled(String section, String fieldName, FieldType fieldType, boolean isRequired)
    {
        valueIndex = 1;
        // Click '+ Create new field' for chosen section
        Selenide.executeJavaScript("$('.admin-fields__title:contains(\"" + section + "\")').parent().find('.admin-fields-add__button').click()");

        // Set Field name
        $("input[label='Field name'][value='']").waitUntil(Condition.visible, 5_000).sendKeys(fieldName);

        // Set id for input field dynamically
        Selenide.executeJavaScript("$('input[label=\"Field name\"][value=\"" + fieldName + "\"]').parent().parent().parent().parent().find('.Select-control input').attr('id', 'fieldTypeID" + fieldTypeID + "')");

        // Set Field type
        $("#fieldTypeID" + fieldTypeID).setValue(fieldType.getFieldType()).sendKeys(Keys.DOWN);
        $("#fieldTypeID" + fieldTypeID).pressEnter();
        fieldTypeID++;

        // Set Required field
        if( isRequired == true )
        {
            $("input[label='Field name'][value='" + fieldName + "']").parent().parent().parent().parent().find(".checkbox.with_label").click();
        }
    }

    /**
     * Click by 'Add values' link and sets Value field.
     * Important: if you want to add multiple values one after another, then invoke this method one after another.
     * @param fieldName name of the field for which values will be added
     * @param value
     */
    public void addValues(String fieldName, String value)
    {
        // Click by + Add values link
        Selenide.executeJavaScript("$('input[value=\"" + fieldName + "\"]').parent().parent().parent().parent().parent().find(\"i:contains('add')\").click()");

        WebElement input = Selenide.executeJavaScript("return $('input[value=\"" + fieldName + "\"]').parent().parent().parent().parent().parent().find(\"input[label='Value " + valueIndex++ + "']\")[0]");
        $(input).val(value);
    }

    /**
     * Sets [value] for field by the name [fieldName] and links to value [linkToValue]
     * @param fieldName name of the field for which need to set value. Starts from Value 1
     * @param value
     * @param linkToValue
     */
    public void addValueAndLinkedValue(String fieldName, String value, String linkToValue)
    {
        $( (WebElement) Selenide.executeJavaScript("return $('input[value=\"" + fieldName + "\"]').parent().parent().parent().parent().parent().find(\"input[label='Value " + valueIndex + "']\")[0]")).val(value);

        WebElement linkToInput = Selenide.executeJavaScript("return $('input[value=\"" + fieldName + "\"]').parent().parent().parent().parent().parent().find(\"input[label='Value " + valueIndex++ + "']\").parent().parent().next().find(\"input:visible\")[0]");
        $(linkToInput).click(); // expand Link to dropdown
        Selenide.executeJavaScript("return $('.dropdown-menu:visible').find(\".checkbox__label:contains('" + linkToValue + "')\").parent().find(\".checkbox__label\").click()");
        $(linkToInput).click(); // close dropdown
    }

    /**
     * Click by 'Add values' link then click by 'Link values' link for certain field
     * @param fieldName
     */
    public LinkedValues clickLinkValues(String fieldName)
    {
        // Click by + Add values link
        Selenide.executeJavaScript("$('input[value=\"" + fieldName + "\"]').parent().parent().parent().parent().parent().find(\"i:contains('add')\").click()");
        // Click 'Link values'
        Selenide.executeJavaScript("$('input[value=\"" + fieldName + "\"]').parent().parent().parent().parent().parent().find(\".ui-link:contains('Link values')\").click()");

        return new LinkedValues();
    }

    public void clickHideValues()
    {
        Selenide.executeJavaScript("$('.admin-fields-field__values div:contains(\"Hide values\")').click()");

        logger.info("Hide values was clicked");
    }

    /**
     * Removes field by it's name by clicking garbage icon.
     * @param fieldName
     */
    public DeleteField removeField(String fieldName)
    {
        Selenide.executeJavaScript("$('input[label=\"Field name\"][value=\"" + fieldName + "\"]').parent().parent().parent().parent().parent().find(\"i\").click()");

        return new DeleteField();
    }
}
