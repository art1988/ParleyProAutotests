package pages.administration.fields_breadcrumb;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.add.AddNewParentField;
import forms.add.AddRelatedField;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FieldsRelations
{
    private SelenideElement addNewParentFieldRequest = $("#add-new-parent-field_type_request");
    private SelenideElement addNewParentFieldSummary = $("#add-new-parent-field_type_contract");

    private static Logger logger = Logger.getLogger(FieldsRelations.class);

    public FieldsRelations()
    {
        $$(".admin-fields__title").shouldHave(CollectionCondition.size(2))
                .shouldHave(CollectionCondition.exactTexts("Summary", "Contract Request"));
    }

    public AddNewParentField addNewParentFieldForRequest()
    {
        addNewParentFieldRequest.click();

        logger.info("Add new parent field link was clicked");

        return new AddNewParentField();
    }

    /**
     * Click by 'Add related field' link for given field name
     * @param fieldName may be 'Contract category', 'Contract type' or any other custom field that was added before
     */
    public AddRelatedField addRelatedField(String fieldName)
    {
        Selenide.executeJavaScript("$('.admin-fields-section div:contains(\"" + fieldName + "\")').last().find(\"i:contains('add')\").click()");

        logger.info("+ Add related field was clicked for: " + fieldName);

        return new AddRelatedField();
    }

    /**
     * Removes field by clicking '-' icon inside "Fields related to" section
     * @param fieldName name of the field to be removed
     */
    public void removeField(String fieldName)
    {
        Selenide.executeJavaScript("$('.admin-fields-relations__field-related:contains(\"" + fieldName + "\")').find(\"i\").click()");

        logger.info("Field " + fieldName + " was removed...");
    }
}
