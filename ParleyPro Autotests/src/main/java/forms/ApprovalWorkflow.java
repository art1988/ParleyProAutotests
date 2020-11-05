package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class ApprovalWorkflow
{
    private SelenideElement nameField      = $("#workflows-approval-name");
    private SelenideElement minValueField  = $("#contractValueRange");
    private SelenideElement maxValueField  = $("input[value='MAX']");

    private SelenideElement priorToNegotiateParticipantField = $("#workfowApprovalUsers_Prior-to-Negotiate");
    private SelenideElement priorToSignParticipantField      = $("#workfowApprovalUsers_Prior-to-Sign");



    public ApprovalWorkflow()
    {
        $(".workflows__title").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Approval workflow"));
    }

    public void setName(String workflowName)
    {
        nameField.setValue(workflowName);
    }

    public void setCategory(String category)
    {
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-cate\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(0).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(0).find(\"label:contains('" + category + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-cate\"]')[0].click()"); // close dropdown
    }

    public void setType(String type)
    {
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-type\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(1).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(1).find(\"label:contains('" + type + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-cate\"]')[0].click()"); // close dropdown
    }

    public void setDepartment(String department)
    {
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-department\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(2).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(2).find(\"label:contains('" + department + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"workflows-approval-department\"]')[0].click()"); // close dropdown
    }

    public void setCurrency(String currency)
    {
        Selenide.executeJavaScript("$('.currency-input-range__group button').click()");
        Selenide.executeJavaScript("$('.currency-input-range__group button').next().find(\"a:contains('" + currency + "')\")[0].click()");
    }

    public void setMinValue(String minValue)
    {
        minValueField.setValue(minValue);
    }

    public void setMaxValue(String maxValue)
    {
        maxValueField.setValue(maxValue);
    }

    public void clickPriorToNegotiate()
    {
        Selenide.executeJavaScript("$('.workflows-approval-events__item div:contains(\"Prior to Negotiate\")').click()");
    }

    public void setPriorToNegotiateParticipant(String participant)
    {
        priorToNegotiateParticipantField.setValue(participant);
        priorToNegotiateParticipantField.sendKeys(Keys.DOWN);
        priorToNegotiateParticipantField.sendKeys(Keys.ENTER);
    }

    public void clickPriorToSign()
    {
        Selenide.executeJavaScript("$('.workflows-approval-events__item div:contains(\"Prior to Sign\")').click()");
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
    }

    public void switchTumblerAllowToModifyApproversOfPriorToSign()
    {
        Selenide.executeJavaScript("$('.workflows-approval-events-event__title:contains(\"Prior to Sign\")').next().find(\"div:contains('Allow')\").children().children().click()");
    }
}
