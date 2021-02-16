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
    private SelenideElement addNewParentField = $("#add-new-parent-field");

    private static Logger logger = Logger.getLogger(FieldsRelations.class);

    public FieldsRelations()
    {
        $$(".admin-fields__title").shouldHave(CollectionCondition.size(2))
                .shouldHave(CollectionCondition.exactTexts("Summary", "Contract Request"));
    }

    public AddNewParentField addNewParentField()
    {
        addNewParentField.click();

        logger.info("Add new parent field link was clicked");

        return new AddNewParentField();
    }

    /**
     * Click by 'Add related field' link for given field name
     * @param fieldName
     */
    public AddRelatedField addRelatedField(String fieldName)
    {
        Selenide.executeJavaScript("$('.admin-fields-section div:contains(\"" + fieldName + "\")').find(\"i\").click()");

        logger.info("+ Add related field was clicked for: " + fieldName);

        return new AddRelatedField();
    }
}
