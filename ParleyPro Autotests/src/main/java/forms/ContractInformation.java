package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents form that appears after clicking on + NEW CONTRACT button
 * or by clicking on Contract info icon.
 */
public class ContractInformation
{
    private SelenideElement contractTitleField         = $("input[inputid='contractTitle']");
    private SelenideElement contractValueField         = $("#contractValue");
    private SelenideElement contractingRegionField     = $("#contractingRegion");
    private SelenideElement contractingCountryField    = $("#contractingCountry");
    private SelenideElement contractEntityField        = $("#contractEntity");
    private SelenideElement contractingDepartmentField = $("#ContractingDepartment");
    private SelenideElement contractCategoryField      = $("#contractCategory");
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
        $(".spinner").waitUntil(Condition.disappear, 10_000);

        $(".contract-edit__title").shouldHave(Condition.exactText("Contract Info"));
    }

    private boolean isInit()
    {
        $(".contract-create__title").waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("Contract information"));

        // wait until spinner disappears - that will indicate that form is fully loaded
        $(".spinner").waitUntil(Condition.disappear, 7_000);

        return ( contractTitleField.isDisplayed() );
    }

    public void setContractTitle(String title)
    {
        contractTitleField.setValue(title);
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
        contractingCountryField.setValue(country);
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
        contractingDepartmentField.setValue(department);
    }

    public String getContractingDepartment()
    {
        return contractingDepartmentField.getValue();
    }

    public void setContractCategory(String category)
    {
        contractCategoryField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractCategoryField.setValue(category);
    }

    public String getContractCategory()
    {
        return contractCategoryField.getValue();
    }

    public void setContractType(String type)
    {
        Selenide.executeJavaScript("$('input[inputid=\"contractType\"]').click()");

        // Select All Types twice to reset all previous selections
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('All Types')\").click()");
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('All Types')\").click()");

        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('" + type + "')\").click()");
    }

    public String getContractType()
    {
        return Selenide.executeJavaScript("return $('input[inputid=\"contractType\"]').val()");
    }

    public void checkClassicNegotiationModeCheckbox()
    {
        Selenide.executeJavaScript("$('#classicNegotiationMode').parent('label').click()");

        logger.info("Classic negotiation mode checkbox was clicked");
    }

    public String getChiefNegotiator()
    {
        String chiefNegotiator = Selenide.executeJavaScript("return $('#ChiefNegotiator').val()");

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
