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
    private SelenideElement contractTitleField         = $(".modal-dialog #contractTitle");
    private SelenideElement contractValueField         = $(".modal-dialog #contractValue");
    private SelenideElement cpOrganizationField        = $(".modal-dialog #counterpartyOrganization");
    private SelenideElement cpChiefNegotiatorField     = $(".modal-dialog #counterpartyChiefNegotiator");
    private SelenideElement contractingRegionField     = $(".modal-dialog #contractingRegion");
    private SelenideElement contractingCountryField    = $(".modal-dialog #contractingCountry");
    private SelenideElement contractEntityField        = $(".modal-dialog #contractEntity");
    private SelenideElement contractingDepartmentField = $(".modal-dialog #ContractingDepartment");
    private SelenideElement contractCategoryField      = $(".modal-content #contractCategory");
    private SelenideElement tagsField                  = $(".modal-dialog #tags");
    private SelenideElement notesField                 = $(".modal-dialog #notes");

    private SelenideElement cancelButton = $(".modal-content .btn.btn-common.btn-link.btn-link-pseudo");
    private SelenideElement saveButton   = $(".modal-content .button.btn.btn-common.btn-blue.btn.btn-default, .modal-footer button[type='submit']");


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
        $(".new-select__indicator").waitUntil(Condition.disappear, 15_000); // this spinner appears inside 'Counterparty organization' input

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
        $(".contract-create__title").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract information"));

        // wait until spinner disappears - that will indicate that form is fully loaded
        $(".modal-content .spinner").waitUntil(Condition.disappear, 25_000);

        return ( contractValueField.isDisplayed() );
    }

    public void setContractTitle(String title)
    {
        Selenide.executeJavaScript("$('#contractTitle').val('')");

        contractTitleField.sendKeys(title);
    }

    public String getContractTitle()
    {
        return contractTitleField.getValue();
    }

    /**
     * Always sets current date.
     */
    public void setDueDate()
    {
        Selenide.executeJavaScript("$('#dueDate').click()");
        Selenide.executeJavaScript("$('.react-datepicker__day--today').click()");
    }

    /**
     * Sets custom due date
     * @param date
     */
    public void setDueDate(String date)
    {
        $("#dueDate").clear();
        $("#dueDate").sendKeys(date);
        $("#dueDate").pressEnter();
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
        contractValueField.clear();
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

    /**
     * Get selected value of 'Contract' radio button
     * @return may return "Buy", "Sell" or "Other"
     */
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

    /**
     * Click by 'Make private' checkbox
     */
    public void setContractVisibility()
    {
        $("#privateMode1").parent().click();

        logger.info("Make private checkbox was checked...");
    }

    /**
     * Return state of 'Contract visibility' checkbox
     * @return true = checked, false = non-checked
     */
    public boolean getContractVisibility()
    {
        return Selenide.executeJavaScript("return document.querySelector('#privateMode1').checked");
    }

    public void setCounterpartyOrganization(String cpOrganization)
    {
        cpOrganizationField.sendKeys(cpOrganization);

        // wait until spinner for Counterparty organization will disappear
        //$(".new-select__indicator").waitUntil(Condition.disappear, 17_000);
        $(".new-select__menu-notice").should(Condition.disappear);

        cpOrganizationField.pressEnter();

        // wait until spinner for CCN field will disappear
        $(".select__loading").waitUntil(Condition.disappear, 17_000);
    }

    public String getCounterpartyOrganization()
    {
        $(".new-select__indicator").waitUntil(Condition.disappear, 15_000);

        return Selenide.executeJavaScript("return $('#counterpartyOrganization').closest('div[class*=\"container\"]').text()");
    }

    public void setCounterpartyChiefNegotiator(String cpChiefNegotiator)
    {
        cpChiefNegotiatorField.sendKeys(cpChiefNegotiator);

        try { Thread.sleep(1_000); } catch (InterruptedException e) { e.printStackTrace(); }
        cpChiefNegotiatorField.pressEnter();
    }

    public String getCounterpartyChiefNegotiator()
    {
        return $(".modal-dialog #counterpartyChiefNegotiator").closest(".new-select__value-container").find(".new-select__single-value").getText();
    }

    public void setContractingRegion(String region)
    {
        contractingRegionField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractingRegionField.sendKeys(region);
        contractingRegionField.pressEnter();
    }

    public String getContractingRegion()
    {
        return Selenide.executeJavaScript("return $('#contractingRegion').closest('div[class*=\"container\"]').text()");
    }

    public void setContractingCountry(String country)
    {
        contractingCountryField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractingCountryField.sendKeys(country);
        contractingCountryField.pressEnter();
    }

    public String getContractingCountry()
    {
        return Selenide.executeJavaScript("return $('#contractingCountry').closest('div[class*=\"container\"]').text()");
    }

    public void setContractEntity(String entity)
    {
        contractEntityField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractEntityField.sendKeys(entity);
        contractEntityField.pressEnter();
    }

    public String getContractEntity()
    {
        return Selenide.executeJavaScript("return $('#contractEntity').closest('div[class*=\"container\"]').text()");
    }

    public void setContractingDepartment(String department)
    {
        contractingDepartmentField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractingDepartmentField.sendKeys(department);
        contractingDepartmentField.pressEnter();
    }

    public String getContractingDepartment()
    {
        return Selenide.executeJavaScript("return $('#ContractingDepartment').closest('div[class*=\"container\"]').text()");
    }

    /**
     * Deletes previous selected Contract Category via BACK_SPACE and sets new value by typing.
     * @param category
     */
    public void setContractCategory(String category)
    {
        contractCategoryField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractCategoryField.sendKeys(category);
        contractCategoryField.pressEnter();

        // Spinner may appear in case if more fields were added for certain category, so let's wait until it disappear
        $(".spinner").waitUntil(Condition.disappear, 60_000);
    }

    /**
     * Reselect value of Contract category. Doesn't click BACK_SPACE to clear the field.
     * @param category
     */
    public void chooseNewContractCategory(String category)
    {
        contractCategoryField.sendKeys(category);
        contractCategoryField.pressEnter();

        // Spinner may appear in case if more fields were added for certain category, so let's wait until it disappear
        $(".spinner").waitUntil(Condition.disappear, 60_000);
    }

    public String getContractCategory()
    {
        return Selenide.executeJavaScript("return $('#contractCategory').closest('div[class*=\"container\"]').text()");
    }

    /**
     * Set _one_ contract type
     * @param type
     */
    public void setContractType(String type)
    {
        $(".modal-dialog input[data-id=\"contractType\"]").click(); // open Contract type dropdown

        // Select All Types twice to reset all previous selections
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('All Types')\").click()");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 15_000);
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('All Types')\").click()");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 15_000);

        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('" + type + "')\").click()");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 15_000);
        Selenide.executeJavaScript("$('input[data-id=\"contractType\"]').click()"); // click by input to collapse dropdown
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
        $(".modal-content .spinner").waitUntil(Condition.disappear, 17_000);
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('All Types')\").click()");
        $(".modal-content .spinner").waitUntil(Condition.disappear, 17_000);

        for( int i = 0; i < type.length; i++ )
        {
            Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('" + type[i] + "')\").click()");
            $(".modal-content .spinner").waitUntil(Condition.disappear, 17_000);
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

    /**
     * Click by blue link '+ Add contract' under 'Linked contracts'
     */
    public ContractInformation clickByAddContractLinkForLinkedContract()
    {
        $(".modal-content .linked-contracts").find("span[class='valign-middle']").click();

        logger.info("'+ Add contract' link was clicked...");

        return this;
    }

    public void setRelationType(String relationType) throws InterruptedException
    {
        SelenideElement input = $$(".modal-content .linked-contracts").filter(Condition.text("Relation type"))
                                                                                .first().parent().parent().find("input");
        input.sendKeys(relationType);
        Thread.sleep(1_000);
        input.pressEnter();
        Thread.sleep(1_000);

        logger.info("Relation type was set as: " + relationType);
    }

    public void setRelatedContract(String relatedContract)
    {
        SelenideElement input = $(".modal-content #linked-contracts-new");

        input.sendKeys(relatedContract);
        // wait until spinner inside this field will disappear
        $(".new-select__indicator").should(Condition.appear);
        $(".new-select__indicator").should(Condition.disappear);

        input.pressEnter();

        logger.info("Related contract was set as: " + relatedContract);
    }

    /**
     * Click by blue checkmark to accept linked contract
     */
    public void linkedContractAccept()
    {
        $(".modal-content .linked-contracts button[class*='styles__accept']").click();

        logger.info("Accept linked contract button was clicked...");
    }

    /**
     * Click by 'x' button to remove linked contract.
     * @param linkedContractName name of the linked contract that need to be removed
     * @return UnlinkContract popup
     */
    public UnlinkContract removeLinkedContract(String linkedContractName)
    {
        $$(".modal-content .linked-contracts .ui-link").filter(Condition.exactText(linkedContractName))
                .first().parent().find("button[class*='styles__remove']").click();

        logger.info("The following linked contract was removed: " + linkedContractName);

        return new UnlinkContract();
    }

    public String getChiefNegotiator()
    {
        return $("#ChiefNegotiator").closest(".new-select__value-container").find(".new-select__single-value").getText();
    }

    /**
     * Sets a tag. To add multiple tags call setTag multiple times.
     * @param tag
     * @throws InterruptedException
     */
    public void setTag(String tag) throws InterruptedException
    {
        tagsField.setValue(tag);
        Thread.sleep(500); // necessary sleep to preserve order during adding of multiple tags

        tagsField.pressEnter();
        Thread.sleep(500); // the same

        logger.info("Tag " + tag + " was added...");
    }

    /**
     * Get tags.
     * @return ElementsCollection object.
     */
    public ElementsCollection getTags()
    {
        return $$(".contract-create.modal-dialog .tags-input__tag, .tags-input__tag").filterBy(Condition.visible);
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
     * General method of setting values for custom field of any type.
     * Important: id's are comes from field names => names of the custom fields must be without spaces !
     * @param fieldName
     * @param fieldType
     * @param value any text value;
     *              <br/>
     *              For checkboxes  - any value (first invoke will set as checked, second invoke will set as unchecked)
     *              <br/>
     *              For radiobutton - value to set
     */
    public void setValueForCustomField(String fieldName, FieldType fieldType, String value) throws InterruptedException
    {
        String id = "";

        if( fieldType.equals(FieldType.TEXT_AREA) )
        {
            id = Selenide.executeJavaScript("return $('.input__label:contains(\"" + fieldName + "\")').parent().find('textarea').attr('inputid')");

            SelenideElement textArea = $("textarea[inputid=\"" + id + "\"]");

            // clear text area field by hitting Ctrl+A + DEL
            textArea.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));

            // (Ctrl+A+DEL) adds strange square symbol □ Since Chrome v.98 => del this symbol too
            for( int i = 0; i < 5; i++ ) textArea.sendKeys(Keys.BACK_SPACE);

            textArea.sendKeys(value);
        }
        else if( fieldType.equals(FieldType.TEXT) )
        {
            id = Selenide.executeJavaScript("return $('.input__label:contains(\"" + fieldName + "\")').parent().find('input').attr('data-id')");
            Selenide.executeJavaScript("$('#" + id + "').val('')");
            $("#" + id).sendKeys(value);
        }
        else if( fieldType.equals(FieldType.CHECKBOX) )
        {
            id = Selenide.executeJavaScript("return $('.checkbox__label:contains(\"" + fieldName + "\")').parent().find('input').attr('id')");
            $("#" + id).parent().click();
            logger.info("Checkbox " + fieldName + " was clicked...");
        }
        else if( fieldType.equals(FieldType.MULTI_SELECT) )
        {
            id = Selenide.executeJavaScript("return $('.input__label:contains(\"" + fieldName + "\")').parent().find('input').attr('data-id')");
            $("input[data-id=\"" + id + "\"]").click(); // expand dropdown
            Selenide.executeJavaScript("$('input[data-id=\"" + id + "\"]').closest(\".multi-select\").find(\".dropdown-menu .checkbox__label:contains('" + value + "')\").click()"); // select value from dropdown
            $("input[data-id=\"" + id + "\"]").click(); // collapse dropdown
        }
        else if( fieldType.equals(FieldType.RADIO_BUTTON) )
        {
            Selenide.executeJavaScript("$('.radio-group__label-text:contains(\"" + fieldName + "\")').closest(\".radio-group\").find(\".radio-group-option__label:contains('" + value + "')\").click()");
            logger.info("Radio button " + value + " was clicked...");
        }
        else if( fieldType.equals(FieldType.NUMERIC) )
        {
            id = Selenide.executeJavaScript("return $('.input__label:contains(\"" + fieldName + "\")').parent().find('input').attr('data-id')");
            SelenideElement numericField = $("#" + id);
            for( int i = 0; i < 20; i++ ) numericField.sendKeys(Keys.BACK_SPACE);
            numericField.sendKeys(value);
        }
        else if( fieldType.equals(FieldType.DECIMAL) )
        {
            id = Selenide.executeJavaScript("return $('.input__label:contains(\"" + fieldName + "\")').parent().find('input').attr('data-id')");
            $("#" + id).sendKeys(value);
        }
        else
        {
            id = Selenide.executeJavaScript("return $('.input__label:contains(\"" + fieldName + "\")').parent().find('input').attr('id')");
            $("#" + id).sendKeys(value);
            $("#" + id).pressEnter(); // to collapse dropdown
        }

        Thread.sleep(500);
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
        cancelButton.waitUntil(Condition.enabled, 30_000).click();

        logger.info("CANCEL button was clicked");

        // wait until Contract information form disappear
        $(".modal-content").waitUntil(Condition.disappear, 60_000);
    }

    public void clickSave()
    {
        // wait until SAVE button is enabled
        saveButton.waitUntil(Condition.enabled, 30_000).click();

        logger.info("SAVE button was clicked");

        // wait until Contract information form disappear
        $(".modal-content").waitUntil(Condition.disappear, 60_000);
    }

    /**
     * Use this method to click SAVE after Amendment.
     * Separate method because save has different selector.
     */
    public void clickSaveFromAmendment()
    {
        $$(".modal-content button").filter(Condition.text("SAVE")).first().click();

        logger.info("SAVE button was clicked");

        // wait until Contract information form disappear
        $(".modal-content").waitUntil(Condition.disappear, 60_000);
    }
}
