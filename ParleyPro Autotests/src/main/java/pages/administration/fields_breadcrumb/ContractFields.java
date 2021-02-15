package pages.administration.fields_breadcrumb;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.FieldType;
import forms.delete.DeleteField;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

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
     * @param fieldName
     * @param value
     */
    public void addValues(String fieldName, String value)
    {
        // Click by + Add values link
        Selenide.executeJavaScript("$('input[value=\"" + fieldName + "\"]').parent().parent().parent().parent().parent().find(\"i:contains('add')\").click()");

        $("input[label='Value " + valueIndex++ + "']").val(value);
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
