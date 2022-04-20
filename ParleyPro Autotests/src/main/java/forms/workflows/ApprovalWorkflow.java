package forms.workflows;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class ApprovalWorkflow
{
    private SelenideElement nameField      = $("#workflows-approval-name");
    private SelenideElement minValueField  = $("#contractValueRange");
    private SelenideElement maxValueField  = $("#contractValueRange_max");

    private SelenideElement contractRequestParticipantField  = $("#workfowApprovalUsers_Contract-Request");
    private SelenideElement priorToNegotiateParticipantField = $("#workfowApprovalUsers_Prior-to-Negotiate");
    private SelenideElement priorToSignParticipantField      = $("#workfowApprovalUsers_Prior-to-Sign");

    private SelenideElement cancelButton = $(".button.btn-common.btn-link-pseudo.btn.btn-link");
    private SelenideElement saveButton   = $("button[type='submit']");


    private static Logger logger = Logger.getLogger(ApprovalWorkflow.class);

    public ApprovalWorkflow(boolean inEditMode)
    {
        if ( inEditMode )
        {
            $(".workflows__title").shouldBe(Condition.visible).shouldHave(Condition.exactText("Edit Approval Workflow"));
        }
        else
        {
            $(".workflows__title").shouldBe(Condition.visible).shouldHave(Condition.exactText("Approval workflow"));
        }
    }

    public void setName(String workflowName)
    {
        nameField.setValue(workflowName);
    }

    public String getName()
    {
        return nameField.getValue();
    }

    public void setEntity(String entity)
    {
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_entity\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('" + entity + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_entity\"]')[0].click()"); // close dropdown
    }

    public String getEntity()
    {
        return Selenide.executeJavaScript("return $('input[data-id=\"wf_autoAssignment_entity\"]').val()");
    }

    public void setCategory(String category)
    {
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-cate\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('" + category + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-cate\"]')[0].click()"); // close dropdown
    }

    public String getCategory()
    {
        String str = Selenide.executeJavaScript("return $('input[data-id=\"workflows-approval-cate\"]').val()");

        return str;
    }

    public void setType(String type)
    {
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-type\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('" + type + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-type\"]')[0].click()"); // close dropdown
    }

    public String getType()
    {
        String str = Selenide.executeJavaScript("return $('input[data-id=\"workflows-approval-type\"]').val()");

        return str;
    }

    public void setDepartment(String department)
    {
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-department\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('" + department + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-department\"]')[0].click()"); // close dropdown
    }

    public String getDepartment()
    {
        String str = Selenide.executeJavaScript("return $('input[data-id=\"workflows-approval-department\"]').val()");

        return str;
    }

    public void setCurrency(String currency)
    {
        Selenide.executeJavaScript("$('.currency-input-range__group button').click()");
        Selenide.executeJavaScript("$('.currency-input-range__group button').next().find(\"a:contains('" + currency + "')\")[0].click()");
    }

    public String getCurrency()
    {
        String str = Selenide.executeJavaScript("return $('.currency-input-range__group button').text().trim()");

        return str;
    }

    public void setMinValue(String minValue)
    {
        minValueField.setValue(minValue);
    }

    public String getMinValue()
    {
        return minValueField.getValue();
    }

    public void setMaxValue(String maxValue)
    {
        maxValueField.setValue(maxValue);
    }

    public String getMaxValue()
    {
        return maxValueField.getValue();
    }

    /**
     * Click by  +Add field  link (bottom left corner) and set value
     * @param fieldName
     * @param value
     */
    public void addFieldAndValue(String fieldName, String value)
    {
        $(".workflows-approval-fields__add").click();

        $(".modal-body .select__arrow").click(); // expand Field dropdown
        Selenide.executeJavaScript("$('.react-autosuggest__section-container span:contains(\"" + fieldName + "\")').click()"); // choose field

        $("input[data-label='Value']").click(); // expand Value dropdown
        Selenide.executeJavaScript("$('.workflows-approval-fields .dropdown-menu .checkbox__label:contains(\"" + value + "\")').click() "); // choose value
        $("input[data-label='Value']").click(); // click again by Value field to collapse dropdown
    }

    /**
     * Click + Contract Request blue link
     */
    public void clickContractRequest()
    {
        Selenide.executeJavaScript("$('.workflows-approval-events__item div:contains(\"Contract Request\")').click()");

        logger.info("Contract Request was clicked");
    }

    public void setContractRequestParticipant(String participant)
    {
        contractRequestParticipantField.sendKeys(participant);
        contractRequestParticipantField.sendKeys(Keys.DOWN);
        contractRequestParticipantField.sendKeys(Keys.ENTER);
    }

    /**
     * Click + Prior to Negotiate blue link
     */
    public void clickPriorToNegotiate()
    {
        Selenide.executeJavaScript("$('.workflows-approval-events__item div:contains(\"Prior to Negotiate\")').click()");

        logger.info("Prior to Negotiate was clicked");
    }

    public void setPriorToNegotiateParticipant(String participant)
    {
        priorToNegotiateParticipantField.sendKeys(participant);
        priorToNegotiateParticipantField.sendKeys(Keys.DOWN);
        priorToNegotiateParticipantField.sendKeys(Keys.ENTER);
    }

    /**
     * Click + Prior to Sign blue link
     */
    public void clickPriorToSign()
    {
        Selenide.executeJavaScript("$('.workflows-approval-events__item div:contains(\"Prior to Sign\")').click()");

        logger.info("Prior to Sign was clicked");
    }

    public void setPriorToSignParticipant(String participant)
    {
        priorToSignParticipantField.setValue(participant);
        priorToSignParticipantField.sendKeys(Keys.DOWN);
        priorToSignParticipantField.sendKeys(Keys.ENTER);
    }

    public void switchTumblerApprovalOrderOfContractRequest()
    {
        Selenide.executeJavaScript("$('.workflows-approval-events-event__title:contains(\"Contract Request\")').next().find(\"div:contains('Set')\").children().children().click()");
        try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

        logger.info("'Set approval order' of 'Contract Request' was switched");
    }

    public void switchTumblerApprovalOrderOfPriorToSign()
    {
        Selenide.executeJavaScript("$('.workflows-approval-events-event__title:contains(\"Prior to Sign\")').next().find(\"div:contains('Set')\").children().children().click()");
        try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

        logger.info("'Set approval order' of 'Prior to Sign' was switched");
    }

    public void switchTumblerAllowToModifyApproversOfPriorToSign()
    {
        Selenide.executeJavaScript("$('.workflows-approval-events-event__title:contains(\"Prior to Sign\")').next().find(\"div:contains('Allow')\").children().children().click()");

        logger.info("'Allow to modify approvers' of 'Prior to Sign' was switched");
    }

    /**
     * Delete condition by it's name
     * @param conditionName
     */
    public void deleteCondition(String conditionName)
    {
        Selenide.executeJavaScript("$('.workflows-approval-events-event__title:contains(\"" + conditionName + "\")').find(\"i\").click()");

        logger.info("Condition " + conditionName + " was deleted");
    }

    public void clickSave()
    {
        saveButton.click();

        logger.info("SAVE button was clicked");

        $(".modal-content").waitUntil(Condition.disappear, 10_000);
    }

    /**
     * Use this if you know in advance that error will happen and modal will not close
     */
    public void clickSaveExpectingError()
    {
        saveButton.click();

        logger.info("SAVE button was clicked");
    }

    public void clickCancel()
    {
        cancelButton.click();

        logger.info("CANCEL button was clicked");
    }
}
