package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.delete.DeleteContract;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents Contract Info page with 'Summary' and 'Post-execution' tabs.
 * 'Post-execution' tab is active by default.
 */
public class ContractInfo
{


    private static Logger logger = Logger.getLogger(ContractInfo.class);

    public ContractInfo()
    {
        $(".documents-contract-edit__title").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Contract Info"));
        $(".tab-menu__item.selected_yes").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Post-execution"));
        $(".contract-execute-form").waitUntil(Condition.visible, 10_000);
        $(".spinner").waitUntil(Condition.disappear, 10_000);
    }

    public void setSignatureDate()
    {
        Selenide.executeJavaScript("$('.input__label span:contains(\"Signature date\")').parent().parent().find(\"input\").click()");

        // Set today - just only for demo
        Selenide.executeJavaScript("$('.react-datepicker__day--today').click()");
    }

    public String getSignatureDate()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Signature date\")').parent().find(\".js-duedate\").val()");
    }

    public void setEffectiveDate()
    {
        Selenide.executeJavaScript("$('.input__label span:contains(\"Effective date\")').parent().parent().find(\"input\").click()");

        // Set today - just only for demo
        Selenide.executeJavaScript("$('.react-datepicker__day--today').click()");
    }

    public String getEffectiveDate()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Effective date\")').parent().find(\".js-duedate\").val()");
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
     * Get state of Auto-Renewal tumbler.
     * @return true - if Auto-Renewal tumbler is checked, false otherwise
     */
    public boolean getAutoRenewalState()
    {
        return Selenide.executeJavaScript("return $('.tumbler-wrapper').find(\"div\").hasClass('checked')");
    }

    public String getSubsequentTermMonths()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Subsequent term (months)\")').parent().find(\"input\").val()");
    }

    public String getRenewal()
    {
        return Selenide.executeJavaScript("return $('label').filter(function() { return $(this).text() === 'Renewal'; }).parent().find(\"input\").val()");
    }

    public String getSubsequentTermNotification()
    {
        return Selenide.executeJavaScript("return $('input[inputid=\"subsequent-term-input\"]').val()");
    }

    /**
     *
     * @return emails in one string like "you@example.commy@example.com"
     */
    public String getRenewalEmailTo()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Renewal email to\")').parent().find(\".tags-input__tag\").text()");
    }

    public String getNoticeOfNonRenewal()
    {
        return Selenide.executeJavaScript("return $('label:contains(\"Notice of non-renewal\")').parent().find(\"input\").val()");
    }

    public String getNotice()
    {
        return Selenide.executeJavaScript("return $('label').filter(function() { return $(this).text() === 'Notice'; }).parent().find(\"input\").val()");
    }

    public String getNoticeNonRenewalNotification()
    {
        return Selenide.executeJavaScript("return $('input[inputid=\"cancellation-notice-input\"]').val()");
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
        return Selenide.executeJavaScript("return $('input[inputid=\"expiration-date-input\"]').val()");
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

    public void clickSave()
    {
        Selenide.executeJavaScript("$('.contract-execute-form button:contains(\"SAVE\")').click()");

        logger.info("Save button was clicked");
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
