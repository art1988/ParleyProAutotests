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

    private SelenideElement priorToNegotiateParticipantField = $("#workfowApprovalUsers_Prior-to-Negotiate");
    private SelenideElement priorToSignParticipantField      = $("#workfowApprovalUsers_Prior-to-Sign");

    private SelenideElement cancelButton = $(".button.btn-common.btn-link-pseudo.btn.btn-link");
    private SelenideElement saveButton   = $("button[type='submit']");


    private static Logger logger = Logger.getLogger(ApprovalWorkflow.class);

    public ApprovalWorkflow()
    {
        $(".workflows__title").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Approval workflow"));
    }

    public void setName(String workflowName)
    {
        nameField.setValue(workflowName);
    }

    public String getName()
    {
        return nameField.getValue();
    }

    public void setCategory(String category)
    {
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-cate\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(0).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(0).find(\"label:contains('" + category + "')\").click()"); // select
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
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(1).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(1).find(\"label:contains('" + type + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-cate\"]')[0].click()"); // close dropdown
    }

    public String getType()
    {
        String str = Selenide.executeJavaScript("return $('input[data-id=\"workflows-approval-type\"]').val()");

        return str;
    }

    public void setDepartment(String department)
    {
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-department\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(2).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(2).find(\"label:contains('" + department + "')\").click()"); // select
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
     * Click + Prior to Negotiate blue link
     */
    public void clickPriorToNegotiate()
    {
        Selenide.executeJavaScript("$('.workflows-approval-events__item div:contains(\"Prior to Negotiate\")').click()");

        logger.info("Prior to Negotiate was clicked");
    }

    public void setPriorToNegotiateParticipant(String participant)
    {
        priorToNegotiateParticipantField.setValue(participant);
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

    public void switchTumblerApprovalOrderOfPriorToSign()
    {
        Selenide.executeJavaScript("$('.workflows-approval-events-event__title:contains(\"Prior to Sign\")').next().find(\"div:contains('Set')\").children().children().click()");

        logger.info("'Set approval order' of 'Prior to Sign' was switched");
    }

    public void switchTumblerAllowToModifyApproversOfPriorToSign()
    {
        Selenide.executeJavaScript("$('.workflows-approval-events-event__title:contains(\"Prior to Sign\")').next().find(\"div:contains('Allow')\").children().children().click()");

        logger.info("'Allow to modify approvers' of 'Prior to Sign' was switched");
    }

    public void clickSave()
    {
        saveButton.click();

        logger.info("Save button was clicked");
    }

    public void clickCancel()
    {
        cancelButton.click();

        logger.info("Cancel button was clicked");
    }
}