package pages.administration;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.administration.fields_breadcrumb.FieldsRelations;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents selected Fields tab on Administration page
 */
public class Fields
{
    private SelenideElement contractFields = $(".admin-fields-navigation__item a[href='#/admin/fields']");
    private SelenideElement fieldRelations = $(".admin-fields-navigation__item a[href='#/admin/fields/relations']");

    private SelenideElement saveButton = $(".button.btn-common.btn-light.btn.btn-link");
    private SelenideElement nextButton = $(".button.btn-common.btn.btn-primary");


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
    }

    public ContractFields clickContractFields()
    {
        logger.info("CONTRACT FIELDS was clicked");

        contractFields.click();

        return new ContractFields();
    }

    public FieldsRelations clickFieldsRelations()
    {
        logger.info("FIELD RELATIONS was clicked");

        fieldRelations.click();

        return new FieldsRelations();
    }

    public void clickSave()
    {
        saveButton.click();

        logger.info("SAVE button was clicked");
    }

    public Fields clickNext()
    {
        nextButton.click();

        logger.info("NEXT button was clicked");

        return this;
    }
}
