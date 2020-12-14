package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.Assert;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents form that appears after clicking on + NEW CONTRACT button
 * or by clicking on Contract info icon
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

    public void setContractingRegion(String region)
    {
        contractingRegionField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractingRegionField.setValue(region);
    }

    public void setContractingCountry(String country)
    {
        contractingCountryField.setValue(country);
    }

    public void setContractEntity(String entity)
    {
        contractEntityField.setValue(entity);
    }

    public void setContractingDepartment(String department)
    {
        contractingDepartmentField.setValue(department);
    }

    public void setContractCategory(String category)
    {
        contractCategoryField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractCategoryField.setValue(category);
    }

    public void setContractType(String type)
    {
        Selenide.executeJavaScript("$('input[inputid=\"contractType\"]').click()");

        // Select All Types twice to reset all previous selections
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('All Types')\").click()");
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('All Types')\").click()");

        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('" + type + "')\").click()");
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

    public void clickSave()
    {
        // wait until SAVE button is enabled
        $(".button.btn.btn-common.btn-blue.btn.btn-default").waitUntil(Condition.enabled, 5_000);

        Selenide.executeJavaScript("$('.pull-right button:contains(\"SAVE\")').click()");

        logger.info("Save button was clicked");
    }
}
