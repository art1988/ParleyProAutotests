package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.ApproveRequest;
import forms.delete.DeleteContract;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents Contract Info page with 'Summary' and 'Post-execution' tabs.
 * 'Post-execution' tab is active by default.
 * Or this panel may be opened after selection of request (right side panel).
 */
public class ContractInfo
{
    private SelenideElement contractValueField         = $("#contractValue");
    private SelenideElement cpOrganizationField        = $("#counterpartyOrganization");
    private SelenideElement cpChiefNegotiatorField     = $("#counterpartyChiefNegotiator");
    private SelenideElement contractingRegionField     = $("#contractingRegion");
    private SelenideElement contractingCountryField    = $("#contractingCountry");
    private SelenideElement contractEntityField        = $("#contractEntity");
    private SelenideElement contractingDepartmentField = $("#ContractingDepartment");
    private SelenideElement contractCategoryField      = $("#contractCategory");


    private static Logger logger = Logger.getLogger(ContractInfo.class);

    public ContractInfo()
    {
        $(".spinner").waitUntil(Condition.disappear, 35_000);
        $(".documents-contract-edit__body .spinner").waitUntil(Condition.disappear, 35_000);

        $(".documents-contract-edit__title").waitUntil(Condition.visible, 35_000).shouldHave(Condition.exactText("Contract Info"));
        $$(".tab-menu .tab-menu__item").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("Summary", "Post-execution"));
        $(".contract-execute-form").waitUntil(Condition.visible, 35_000);
    }

    public ContractInfo(boolean isRequest)
    {
        $(".spinner").waitUntil(Condition.disappear, 20_000);
        $(".documents-contract-edit__body .spinner").waitUntil(Condition.disappear, 20_000);
        $(".modal__scrollable-body .spinner").waitUntil(Condition.disappear, 20_000);

        $(".documents-contract-edit__title").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("Contract Info"));
    }

    /**
     * Always sets today
     */
    public void setSignatureDate()
    {
        Selenide.executeJavaScript("$('.input__label span:contains(\"Signature date\")').parent().parent().find(\"input\").click()");

        // Set today - just only for demo
        Selenide.executeJavaScript("$('.react-datepicker__day--today').click()");
    }

    public void setSignatureDate(String date)
    {
        WebElement signatureDateInput = Selenide.executeJavaScript("return $('label:contains(\"Signature date\")').parent().find(\"input\")[0]");

        signatureDateInput.sendKeys(date);
        $(signatureDateInput).pressEnter(); // to close date picker popup

        logger.info("The following Signature date was set: " + date);
    }

    public String getSignatureDate()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Signature date\")').parent().find(\".js-duedate\").val()");
    }

    /**
     * Always sets today
     */
    public void setEffectiveDate()
    {
        Selenide.executeJavaScript("$('.input__label span:contains(\"Effective date\")').parent().parent().find(\"input\").click()");

        // Set today - just only for demo
        Selenide.executeJavaScript("$('.react-datepicker__day--today').click()");
    }

    public void setEffectiveDate(String date)
    {
        WebElement effectiveDateInput = Selenide.executeJavaScript("return $('label:contains(\"Effective date\")').parent().find(\"input\")[0]");

        effectiveDateInput.sendKeys(date);
        $(effectiveDateInput).pressEnter(); // to close date picker popup

        logger.info("The following Effective date was set: " + date);
    }

    public String getEffectiveDate()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Effective date\")').parent().find(\".js-duedate\").val()");
    }

    public void setExpirationDate(String date)
    {
        WebElement expirationDateInput = Selenide.executeJavaScript("return $('label:contains(\"Expiration date\")').parent().find(\"input\")[0]");

        expirationDateInput.sendKeys(date);
        $(expirationDateInput).pressEnter(); // to close date picker popup

        logger.info("The following Expiration date was set: " + date);
    }

    public void setInitialTerm(String initialTermVal)
    {
        WebElement initialTermInput = Selenide.executeJavaScript("return $('label:contains(\"Initial term\")').parent().find(\"input\")[0]");

        $(initialTermInput).sendKeys(initialTermVal);
    }

    public String getInitialTerm()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Initial term\")').parent().find(\"input\").val()");
    }

    /**
     * May return Months, Days, Years.
     * @return
     */
    public String getInitialTermDuration()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Initial term\")').parent().find(\"button\").text().trim()");
    }

    /**
     * Click by Auto-Renewal tumbler.
     */
    public void clickAutoRenewalTumbler()
    {
        $(".tumbler-wrapper .tumbler").click();

        logger.info("Auto-Renewal tumbler was clicked... Current state is: " + getAutoRenewalState());
    }

    public void setContractValue(String value)
    {
        contractValueField.sendKeys(value);
    }

    /**
     * Get state of Auto-Renewal tumbler.
     * @return true - if Auto-Renewal tumbler is checked, false otherwise
     */
    public boolean getAutoRenewalState()
    {
        return Selenide.executeJavaScript("return $('.tumbler-wrapper').find(\"div\").hasClass('checked')");
    }

    public void setSubsequentTermMonths(String val)
    {
        WebElement input = Selenide.executeJavaScript("return $('label:contains(\"Subsequent term (months)\")').parent().find(\"input\")[0]");
        $(input).sendKeys(val);
    }

    public String getSubsequentTermMonths()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Subsequent term (months)\")').parent().find(\"input\").val()");
    }

    public String getRenewal()
    {
        return Selenide.executeJavaScript("return $('label').filter(function() { return $(this).text() === 'Renewal'; }).parent().find(\"input\").val()");
    }

    /**
     * Sets Subsequent term Notification. Click by this field to activate dropdown, then click by checkbox.
     * @param val may be '1 day', '3 days', ..., '3 months', '6 months'
     */
    public void setSubsequentTermNotification(String val)
    {
        $("input[data-id='subsequent-term-input']").click(); // click by field to activate dropdown

        // select value
        Selenide.executeJavaScript("$('input[data-id=\"subsequent-term-input\"]').closest('.multi-select').find(\".dropdown-menu\").find(\"span:contains('" + val + "')\").click()");

        $("input[data-id='subsequent-term-input']").click(); // click by field again to collapse dropdown
    }

    public String getSubsequentTermNotification()
    {
        return Selenide.executeJavaScript("return $('input[data-id=\"subsequent-term-input\"]').val()");
    }

    /**
     * Set 'Renewal email to' field
     * @param email
     */
    public void setRenewalEmailTo(String email)
    {
        WebElement input = Selenide.executeJavaScript("return $('label:contains(\"Renewal email to\")').parent().find(\"input\")[0]");
        $(input).sendKeys(email);
        $(input).pressEnter();
    }

    /**
     *
     * @return emails in one string like "you@example.commy@example.com"
     */
    public String getRenewalEmailTo()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Renewal email to\")').parent().find(\".tags-input__tag\").text()");
    }

    /**
     * Set 'Notice of non-renewal' field
     * @param days may be '1 day', '3 days', ..., '3 months', '6 months'
     */
    public void setNoticeOfNonRenewal(String days)
    {
        WebElement input = Selenide.executeJavaScript("return $('label:contains(\"Notice of non-renewal\")').parent().find(\"input\")[0]");
        $(input).sendKeys(days);
        $(input).pressEnter();
    }

    public String getNoticeOfNonRenewal()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Notice of non-renewal\")').parent().find(\"input\").val()");
    }

    public String getNotice()
    {
        return Selenide.executeJavaScript("return $('label').filter(function() { return $(this).text() === 'Notice'; }).parent().find(\"input\").val()");
    }

    /**
     * Sets Notice of non-renewal Notification. Click by this field to activate dropdown, then click by checkbox.
     * @param val
     */
    public void setNoticeOfNonRenewalNotification(String val)
    {
        $("input[data-id='cancellation-notice-input']").click(); // click by field to activate dropdown

        // select value
        Selenide.executeJavaScript("$('input[data-id=\"cancellation-notice-input\"]').closest('.multi-select').find(\".dropdown-menu\").find(\"span:contains('" + val + "')\").click()");

        $("input[data-id='cancellation-notice-input']").click(); // click by field again to collapse dropdown
    }

    public String getNoticeOfNonRenewalNotification()
    {
        return Selenide.executeJavaScript("return $('input[data-id=\"cancellation-notice-input\"]').val()");
    }

    /**
     * Set 'Notice email to' field
     * @param email
     */
    public void setNoticeEmailTo(String email)
    {
        WebElement input = Selenide.executeJavaScript("return $('label:contains(\"Notice email to\")').parent().find(\"input\")[0]");
        $(input).sendKeys(email);
        $(input).pressEnter();
    }

    /**
     *
     * @return emails in one string like "you@example.commy@example.com"
     */
    public String getNoticeEmailTo()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Notice email to\")').parent().find(\".tags-input__tag\").text()");
    }

    public String getExpirationDate()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Expiration date\")').parent().find(\"input\").val()");
    }

    public String getExpirationDateNotification()
    {
        return Selenide.executeJavaScript("return $('input[data-id=\"expiration-date-input\"]').val()");
    }

    /**
     *
     * @return emails in one string like "you@example.commy@example.com"
     */
    public String getExpirationEmailTo()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Expiration email to\")').parent().find(\".tags-input__tag\").text()");
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

    /**
     * Get text from Notes textarea
     * @return
     */
    public String getNotes()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Notes\")').parent().find(\"textarea\").text()");
    }

    public void setCounterpartyOrganization(String cpOrganization)
    {
        cpOrganizationField.sendKeys(cpOrganization);

        // wait until spinner for Counterparty organization will disappear
        $(".new-select__indicator").waitUntil(Condition.disappear, 17_000);

        cpOrganizationField.pressEnter();

        // wait until spinner for CCN field will disappear
        $(".select__loading").waitUntil(Condition.disappear, 17_000);
    }

    public void setCounterpartyChiefNegotiator(String cpChiefNegotiator)
    {
        cpChiefNegotiatorField.sendKeys(cpChiefNegotiator);
        cpChiefNegotiatorField.pressEnter();
    }

    public void setContractingRegion(String region)
    {
        contractingRegionField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractingRegionField.sendKeys(region);
        contractingRegionField.pressEnter();
    }

    public void setContractingCountry(String country)
    {
        contractingCountryField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractingCountryField.sendKeys(country);
        contractingCountryField.pressEnter();
    }

    public void setContractEntity(String entity)
    {
        contractEntityField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractEntityField.sendKeys(entity);
        contractEntityField.pressEnter();
    }

    public void setContractingDepartment(String department)
    {
        contractingDepartmentField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractingDepartmentField.sendKeys(department);
        contractingDepartmentField.pressEnter();
    }

    public void setContractCategory(String category)
    {
        contractCategoryField.sendKeys(Keys.BACK_SPACE); // clear field by pressing BACK_SPACE
        contractCategoryField.sendKeys(category);
        contractCategoryField.pressEnter();

        // Spinner may appear in case if more fields were added for certain category, so let's wait until it disappear
        $(".spinner").waitUntil(Condition.disappear, 10_000);
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
        $(".spinner").waitUntil(Condition.disappear, 15_000);
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('All Types')\").click()");
        $(".spinner").waitUntil(Condition.disappear, 15_000);

        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('" + type + "')\").click()");
        $(".spinner").waitUntil(Condition.disappear, 15_000);
        Selenide.executeJavaScript("$('input[data-id=\"contractType\"]').click()"); // click by input to collapse dropdown
    }

    public void clickSave()
    {
        Selenide.executeJavaScript("$('.contract-execute-form button:contains(\"SAVE\")').click()");

        // in case if it is Request...
        if( $(".button.btn.btn-common.btn-blue.btn.btn-default").isDisplayed() )
        {
            $(".button.btn.btn-common.btn-blue.btn.btn-default").click();
        }

        logger.info("Save button was clicked");
    }

    /**
     * Click by APPROVE button in case of _request_
     */
    public ApproveRequest clickApproveRequest()
    {
        $$(".modal-footer button").findBy(Condition.text("APPROVE")).click();

        logger.info("APPROVE button was clicked");

        return new ApproveRequest();
    }

    /**
     * Click by actions menu with 3 dots and choose Delete contract
     */
    public DeleteContract deleteContract(String contractName)
    {
        Selenide.executeJavaScript("$(\".actions-menu button\").click()");

        Selenide.executeJavaScript("$(\".dropdown-menu.dropdown-menu-right a:contains('Delete')\")[0].click()");

        return new DeleteContract(contractName);
    }
}
