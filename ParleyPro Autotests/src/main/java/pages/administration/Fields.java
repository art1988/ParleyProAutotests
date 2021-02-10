package pages.administration;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import constants.FieldType;
import forms.delete.DeleteField;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents selected Fields tab on Administration page
 */
public class Fields
{
    private SelenideElement saveButton = $(".button.btn-common.btn-light.btn.btn-link");


    private static Logger logger = Logger.getLogger(Fields.class);

    public Fields()
    {
        $(".spinner").waitUntil(Condition.disappear, 7_000);
        try
        {
            Thread.sleep(1_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        $$(".admin-fields-navigation a").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.exactTexts("CONTRACT FIELDS", "FIELD RELATIONS", "LAYOUT"));

        $$(".admin-fields__title").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.exactTexts("Summary", "Post-execution", "Contract Request"));
    }

    public void createNewFiledForSummary(String fieldName, FieldType fieldType, boolean isRequired)
    {
        Selenide.executeJavaScript("$('.admin-fields__title:contains(\"Summary\")').parent().find('.admin-fields-add__button').click()");

        // Set Field name
        $("input[label='Field name'][value='']").waitUntil(Condition.visible, 5_000).sendKeys(fieldName);

        // Set Field type
        Selenide.executeJavaScript("$('.Select-control input').attr('id', 'fieldTypeID')");
        $("#fieldTypeID").setValue(fieldType.getFieldType()).pressEnter();

        // Set Required field
        if( isRequired == true )
        {
            $("input[label='Field name'][value='" + fieldName + "']").parent().parent().parent().parent().find(".checkbox.with_label").click();
        }
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

    public void clickSave()
    {
        saveButton.click();

        logger.info("SAVE button was clicked");
    }
}
