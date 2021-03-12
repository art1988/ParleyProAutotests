package pages.administration;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.administration.fields_breadcrumb.FieldsRelations;
import pages.administration.fields_breadcrumb.Layout;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents selected Fields tab on Administration page
 */
public class Fields
{
    private SelenideElement contractFields = $(".admin-fields-navigation__item a[href='#/admin/fields']");
    private SelenideElement fieldRelations = $(".admin-fields-navigation__item a[href='#/admin/fields/relations']");
    private SelenideElement layout         = $(".admin-fields-navigation__item a[href='#/admin/fields/layout']");

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
        contractFields.click();

        logger.info("CONTRACT FIELDS was clicked");

        return new ContractFields();
    }

    public FieldsRelations clickFieldsRelations()
    {
        fieldRelations.click();

        logger.info("FIELD RELATIONS was clicked");

        return new FieldsRelations();
    }

    public Layout clickLayout()
    {
        layout.click();

        logger.info("LAYOUT was clicked");

        return new Layout();
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
