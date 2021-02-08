package pages.administration;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import constants.FieldType;
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

        $$(".admin-fields-navigation a").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.exactTexts("CONTRACT FIELDS", "FIELD RELATIONS", "LAYOUT"));

        $$(".admin-fields__title").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.exactTexts("Summary", "Post-execution", "Contract Request"));
    }

    public void createNewFiledForSummary(String fieldName, FieldType fieldType, boolean isRequired) throws InterruptedException
    {
        Selenide.executeJavaScript("$('.admin-fields__title:contains(\"Summary\")').parent().find('.admin-fields-add__button').click()");

        $("input[label='Field name'][value='']").waitUntil(Condition.visible, 5_000).sendKeys(fieldName);
        //$(".Select-multi-value-wrapper").
    }

    public void clickSave()
    {
        saveButton.click();

        logger.info("SAVE button was clicked");
    }
}
