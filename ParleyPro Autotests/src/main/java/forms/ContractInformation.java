package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import constants.FieldType;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents form that appears after clicking on + NEW CONTRACT button
 * or by clicking on Contract info icon.
 * May also invoked by clicking 'Amend contract' via action menu.
 */
public class ContractInformation
{
    private SelenideElement contractTitleField         = $("#contractTitle");
    private SelenideElement contractValueField         = $("#contractValue");
    private SelenideElement cpOrganizationField        = $("#counterpartyOrganization");
    private SelenideElement cpChiefNegotiatorField     = $("#counterpartyChiefNegotiator");
    private SelenideElement contractingRegionField     = $("#contractingRegion");
    private SelenideElement contractingCountryField    = $("#contractingCountry");
    private SelenideElement contractEntityField        = $("#contractEntity");
    private SelenideElement contractingDepartmentField = $("#ContractingDepartment");
    private SelenideElement contractCategoryField      = $(".modal-content #contractCategory");
    private SelenideElement tagsField                  = $("#tags");
    private SelenideElement notesField                 = $("#notes");

    private SelenideElement cancelButton = $(".btn.btn-common.btn-link.btn-link-pseudo");
    private SelenideElement saveButton   = $(".button.btn.btn-common.btn-blue.btn.btn-default");


    private static Logger logger = Logger.getLogger(ContractInformation.class);

    public ContractInformation()
    {
        Assert.assertTrue(isInit());
    }

    /**
     * Use this constructor if ContractInformation was opened via Contract Info button
     * @param openedViaContractInfo just marker to indicate that it was opened via Contract Info button
     */
    public ContractInformation(boolean openedViaContractInfo)
    {
        $(".spinner").waitUntil(Condition.disappear, 30_000); // General spinner
        $(".Select-loading").waitUntil(Condition.disappear, 15_000); // this spinner appears inside 'Counterparty organization' input

        try
        {
            Thread.sleep(1_500);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        $(".contract-edit__title, .documents-contract-edit__title").shouldHave(Condition.exactText("Contract Info"));
    }

    private boolean isInit()
    {
        $(".contract-create__title").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("Contract information"));

        // wait until spinner disappears - that will indicate that form is fully loaded
        $(".modal-content .spinner").waitUntil(Condition.disappear, 25_000);

        return ( contractTitleField.isDisplayed() );
    }

    public void setContractTitle(String title)
    {
        contractTitleField.sendKeys(title);
    }

    public String getContractTitle()
    {
        return contractTitleField.getValue();
    }

    /**
     * Always sets current date. TODO: re-implement later for any value
     */
    public void setDueDate()
    {
        Selenide.executeJavaScript("$('#dueDate').click()");
        Selenide.executeJavaScript("$('.react-datepicker__day--today').click()");
    }

    public String getDueDate()
    {
        return $("#dueDate").getValue();
    }

    /**
     * Set contract currency
     * @param currency may be USD, EUR, GBP, etc.
     */
    public void setContractCurrency(String currency)
    {
        Selenide.executeJavaScript("$('.currency-input.input button').click()"); // Open currency dropdown
        Selenide.executeJavaScript("$('.currency-input.input button').next().find(\"a:contains('" + currency + "')\")[0].click()"); // Set currency

        logger.info("The following currency was selected: " + currency);
    }

    public void setContractValue(String value)
    {
        contractValueField.setValue(value);
    }

    public String getContractValue()
    {
        return contractValueField.getValue();
    }

    /**
     * Set Contract radio button
     * @param contract may be "Buy", "Sell" or "Other"
     */
    public void setContractRadioButton(String contract)
    {
        Selenide.executeJavaScript("$('.radio-group__label:contains(\"Contract\")').next().find(\"span:contains('" + contract + "')\").click()");
    }

    public String getContractRadioButtonSelection()
    {
        return Selenide.executeJavaScript("return $('.radio-group__label:contains(\"Contract\")').next().find(\"input[checked]\").next().text()");
    }

    /**
     * Set My company template radio button
     * @param flag true = Yes, false = No
     */
    public void setMyCompanyTemplate(boolean flag)
    {
        if( flag == true )
        {
            Selenide.executeJavaScript("$('.radio-group__label:contains(\"My company template\")').next().find(\"span:contains('Yes')\").click()");
        }
        else
        {
            Selenide.executeJavaScript("$('.radio-group__label:contains(\"My company template\")').next().find(\"span:contains('No')\").click()");
        }
    }

    /**
     * Get selected value of My company template
     * @return may be "Yes" or "No"
     */
    public String getMyCompanyTemplateRadioButtonSelection()
    {
        return Selenide.executeJavaScript("return $('.radio-group__label:contains(\"My company template\")').next().find(\"input[checked]\").next().text()");
    }

    public void setCounterpartyOrganization(String cpOrganization)
    {
        cpOrganizationField.sendKeys(cpOrganization);

        // wait until spinner for Counterparty organization will disappear
        $(".Select-loading").waitUntil(Condition.disappear, 7_000);

        cpOrganizationField.pressEnter();

        // wait until spinner for CCN field will disappear
        $(".select__loading").waitUntil(Condition.disappear, 7_000);
    }

    public String getCounterpartyOrganization()
    {
        $(".Select-loading").waitUntil(Condition.disappear, 15_000);

        return Selenide.executeJavaScript("return $('#counterpartyOrganization').parent().prev().text()");
    }

    public void setCounterpartyChiefNegotiator(String cpChiefNegotiator)
    {
        cpChiefNegotiatorField.sendKeys(cpChiefNegotiator);
    }

    public void setContractingRegion(String region)
    {
        contractingRegionField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractingRegionField.setValue(region);
    }

    public String getContractingRegion()
    {
        return contractingRegionField.getValue();
    }

    public void setContractingCountry(String country)
    {
        // expand dropdown by clicking triangle
        Selenide.executeJavaScript("$('#contractingCountry').parent().parent().find(\".select__arrow\")[0].click()");

        // Set value
        Selenide.executeJavaScript("$('#contractingCountry').parent().parent().find(\"#react-autowhatever-1\").find(\"span:contains('" + country + "')\").click()");
    }

    public String getContractingCountry()
    {
        return contractingCountryField.getValue();
    }

    public void setContractEntity(String entity)
    {
        contractEntityField.setValue(entity);
    }

    public String getContractEntity()
    {
        return contractEntityField.getValue();
    }

    public void setContractingDepartment(String department)
    {
        contractingDepartmentField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractingDepartmentField.sendKeys(department);
    }

    public String getContractingDepartment()
    {
        return contractingDepartmentField.getValue();
    }

    public void setContractCategory(String category)
    {
        contractCategoryField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractCategoryField.sendKeys(category);

        // Spinner may appear in case if more fields were added for certain category, so let's wait until it disappear
        $(".spinner").waitUntil(Condition.disappear, 10_000);
    }

    public String getContractCategory()
    {
        return contractCategoryField.getValue();
    }

    /**
     * Set _one_ contract type
     * @param type
     */
    public void setContractType(String type)
    {
        $("input[data-id=\"contractType\"]").click(); // open Contract type dropdown

        // Select All Types twice to reset all previous selections
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('All Types')\").click()");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 15_000);
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('All Types')\").click()");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 15_000);

        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('" + type + "')\").click()");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 15_000);
        $("input[data-id=\"contractType\"]").click(); // click by input to collapse dropdown
    }

    /**
     * Sets _multiple_ contract types
     * @param type
     */
    public void setContractType(String[] type)
    {
        $("input[data-id=\"contractType\"]").click(); // open Contract type dropdown

        // Select All Types twice to reset all previous selections
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('All Types')\").click()");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('All Types')\").click()");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);

        for( int i = 0; i < type.length; i++ )
        {
            Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('" + type[i] + "')\").click()");
            $(".modal-content .spinner").waitUntil(Condition.disappear, 7_000);
        }

        $("input[data-id=\"contractType\"]").click(); // click by input to collapse dropdown
    }

    public String getContractType()
    {
        return Selenide.executeJavaScript("return $('input[data-label=\"Contract type\"]').val()");
    }

    public void checkClassicNegotiationModeCheckbox()
    {
        Selenide.executeJavaScript("$('#classicNegotiationMode').parent('label').click()");

        logger.info("Classic negotiation mode checkbox was clicked");
    }

    public String getChiefNegotiator()
    {
        String chiefNegotiator = Selenide.executeJavaScript("return $('#ChiefNegotiator').parent().prev().text()");

        return chiefNegotiator;
    }

    public void setTag(String tag) throws InterruptedException
    {
        tagsField.setValue(tag);
        Thread.sleep(500); // necessary sleep to preserve order during adding of multiple tags

        tagsField.pressEnter();
        Thread.sleep(500); // the same
    }

    /**
     * Get tags.
     * @return ElementsCollection object.
     */
    public ElementsCollection getTags()
    {
        return $$(".tags-input__tag");
    }

    public void setNotes(String notes)
    {
        notesField.setValue(notes);
    }

    public String getNotes()
    {
        return notesField.getText();
    }

    /**
     * General method of setting values for custom field
     * @param fieldName
     * @param fieldType
     * @param value
     */
    // TODO: depending on fieldType need to choose appropriate html tag -> implement in the future
    public void setValueForCustomField(String fieldName, FieldType fieldType, String value)
    {
        String id = "";

        if( fieldType.equals(FieldType.TEXT_AREA) )
        {
            id = Selenide.executeJavaScript("return $('.input__label:contains(\"" + fieldName + "\")').parent().find('textarea').attr('inputid')");
            $("textarea[inputid=\"" + id + "\"]").sendKeys(value);
        }
        else
        {
            id = Selenide.executeJavaScript("return $('.input__label:contains(\"" + fieldName + "\")').parent().find('input').attr('id')");
            $("#" + id).sendKeys(value);
            $("#" + id).pressEnter(); // to collapse dropdown
        }
    }

    /**
     * Get value from arbitrary Custom Field
     * @param customFieldName name of custom field value of which we want to obtain
     * @return
     */
    public String getValueFromCustomField(String customFieldName)
    {
        return Selenide.executeJavaScript("return $('label:contains(\"" + customFieldName + "\")').parent().find(\"input:visible\").val()");
    }

    public void clickCancel()
    {
        cancelButton.waitUntil(Condition.enabled, 5_000).click();

        logger.info("CANCEL button was clicked");

        // wait until Contract information form disappear
        $(".modal-content").waitUntil(Condition.disappear, 7_000);
    }

    public void clickSave()
    {
        // wait until SAVE button is enabled
        saveButton.waitUntil(Condition.enabled, 5_000).click();

        logger.info("SAVE button was clicked");

        // wait until Contract information form disappear
        $(".modal-content").waitUntil(Condition.disappear, 7_000);
    }
}
