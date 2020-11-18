package forms.workflows;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class ContractRoutingWorkflow
{
    private SelenideElement nameField = $("#wf_autoAssignment_name");
    private SelenideElement minValueField  = $("#contractValueRange");
    private SelenideElement maxValueField  = $("#contractValueRange_max");

    private SelenideElement draftToReviewParticipantField              = $("#workfowApprovalUsersAssignment_Draft-to-review");
    private SelenideElement textChangedParticipantField                = $("#workfowApprovalUsersAssignment_Text-changed");
    private SelenideElement signatureDeclinedParticipantField          = $("#workfowApprovalUsersAssignment_Signature-declined");
    private SelenideElement uploadCounterpartyDocumentParticipantField = $("#workfowApprovalUsersAssignment_Upload-Counterparty-document");

    // Fields used for Salesforce
    private SelenideElement contractRequestedParticipantField             = $("#workfowApprovalUsersAssignment_Contract-requested");
    private SelenideElement сontractCreatedViaIntegrationParticipantField = $("#workfowApprovalUsersAssignment_Contract-created-via-integration");



    public ContractRoutingWorkflow()
    {
        $(".workflows__title").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("New Workflow"));
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
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_category\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(0).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(0).find(\"label:contains('" + category + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_category\"]')[0].click()"); // close dropdown
    }

    public String getCategory()
    {
        String str = Selenide.executeJavaScript("return $('input[data-id=\"wf_autoAssignment_category\"]').val()");

        return str;
    }

    public void setType(String type)
    {
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_type\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(1).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(1).find(\"label:contains('" + type + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_type\"]')[0].click()"); // close dropdown
    }

    public String getType()
    {
        String str = Selenide.executeJavaScript("return $('input[data-id=\"wf_autoAssignment_type\"]').val()");

        return str;
    }

    public void setDepartment(String department)
    {
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_departnent\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(2).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').eq(2).find(\"label:contains('" + department + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_departnent\"]')[0].click()"); // close dropdown
    }

    public String getDepartment()
    {
        String str = Selenide.executeJavaScript("return $('input[data-id=\"wf_autoAssignment_departnent\"]').val()");

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
     * Click + Draft to review blue link
     */
    public void clickDraftToReview()
    {
        Selenide.executeJavaScript("$('.workflows-autoassignment-events__item div:contains(\"Draft to review\")').click()");
    }

    public void setDraftToReviewParticipant(String participant)
    {
        draftToReviewParticipantField.setValue(participant);
        draftToReviewParticipantField.sendKeys(Keys.DOWN);
        draftToReviewParticipantField.sendKeys(Keys.ENTER);
    }

    /**
     * Click + Text changed blue link
     */
    public void clickTextChanged()
    {
        Selenide.executeJavaScript("$('.workflows-autoassignment-events__item div:contains(\"Text changed\")').click()");
    }

    public void setTextChangedParticipant(String participant)
    {
        textChangedParticipantField.setValue(participant);
        textChangedParticipantField.sendKeys(Keys.DOWN);
        textChangedParticipantField.sendKeys(Keys.ENTER);
    }

    /**
     * Click + Signature declined blue link
     */
    public void clickSignatureDeclined()
    {
        Selenide.executeJavaScript("$('.workflows-autoassignment-events__item div:contains(\"Signature declined\")').click()");
    }

    public void setSignatureDeclinedParticipant(String participant)
    {
        signatureDeclinedParticipantField.setValue(participant);
        signatureDeclinedParticipantField.sendKeys(Keys.DOWN);
        signatureDeclinedParticipantField.sendKeys(Keys.ENTER);
    }

    /**
     * Click + Upload Counterparty document blue link
     */
    public void clickUploadCounterpartyDocument()
    {
        Selenide.executeJavaScript("$('.workflows-autoassignment-events__item div:contains(\"Upload Counterparty document\")').click()");
    }

    public void setUploadCounterpartyDocumentParticipant(String participant)
    {
        uploadCounterpartyDocumentParticipantField.setValue(participant);
        uploadCounterpartyDocumentParticipantField.sendKeys(Keys.DOWN);
        uploadCounterpartyDocumentParticipantField.sendKeys(Keys.ENTER);
    }
}
