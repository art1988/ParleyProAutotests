package forms.workflows;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class ContractRoutingWorkflow
{
    private SelenideElement nameField      = $("#wf_autoAssignment_name");
    private SelenideElement minValueField  = $("#contractValueRange");
    private SelenideElement maxValueField  = $("#contractValueRange_max");

    private SelenideElement draftToReviewParticipantField              = $("#workfowApprovalUsersAssignment_Draft-to-review");
    private SelenideElement textChangedParticipantField                = $("#workfowApprovalUsersAssignment_Text-changed");
    private SelenideElement signatureDeclinedParticipantField          = $("#workfowApprovalUsersAssignment_Signature-declined");
    private SelenideElement uploadCounterpartyDocumentParticipantField = $("#workfowApprovalUsersAssignment_Upload-Counterparty-document");

    // Fields used for Salesforce
    private SelenideElement contractRequestedParticipantField             = $("#workfowApprovalUsersAssignment_Contract-requested");
    private SelenideElement сontractCreatedViaIntegrationParticipantField = $("#workfowApprovalUsersAssignment_Contract-created-via-integration");

    private SelenideElement cancelButton = $(".button.btn-common.btn-link-pseudo.btn.btn-link");
    private SelenideElement saveButton   = $("button[type='submit']");


    private static Logger logger = Logger.getLogger(ContractRoutingWorkflow.class);

    public ContractRoutingWorkflow(boolean inEditMode)
    {
        if( inEditMode )
        {
            $(".workflows__title").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Edit Routing Workflow"));
        }
        else
        {
            $(".workflows__title").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Routing Workflow"));
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

    public void setCategory(String category)
    {
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_category\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('" + category + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_category\"]')[0].click()"); // close dropdown
    }

    public String getCategory()
    {
        String str = Selenide.executeJavaScript("return $('input[data-id=\"wf_autoAssignment_category\"]').val()");

        return str;
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

    public void setType(String type)
    {
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_type\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('" + type + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_type\"]')[0].click()"); // close dropdown
    }

    public String getType()
    {
        String str = Selenide.executeJavaScript("return $('input[data-id=\"wf_autoAssignment_type\"]').val()");

        return str;
    }

    public void setDepartment(String department)
    {
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_department\"]')[0].click()"); // open dropdown
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu:visible').eq(0).find(\"label:contains('" + department + "')\").click()"); // select
        Selenide.executeJavaScript("$('input[data-id=\"wf_autoAssignment_department\"]')[0].click()"); // close dropdown
    }

    public String getDepartment()
    {
        String str = Selenide.executeJavaScript("return $('input[data-id=\"wf_autoAssignment_department\"]').val()");

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

        logger.info("Draft to review was clicked");
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

        logger.info("Text changed was clicked");
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

        logger.info("Signature declined was clicked");
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

        logger.info("Upload Counterparty document was clicked");
    }

    public void setUploadCounterpartyDocumentParticipant(String participant)
    {
        uploadCounterpartyDocumentParticipantField.setValue(participant);
        uploadCounterpartyDocumentParticipantField.sendKeys(Keys.DOWN);
        uploadCounterpartyDocumentParticipantField.sendKeys(Keys.ENTER);
    }

    /**
     * Expand dropdown with roles for particular username and set role
     * @param username name of the user for which we want set role
     * @param role name of the role
     */
    public void setRoleForUser(String username, String role)
    {
        Selenide.executeJavaScript("$('.workflows-users-list__item:contains(\"" + username + "\")').find(\"button\").click()"); // expand dropdown

        Selenide.executeJavaScript("$('.workflows-users-list__item:contains(\"" + username + "\")').find(\"button\").next().find(\"a:contains('" + role + "')\")[0].click()"); // choose role
    }

    /**
     * Delete event by it's name
     * @param eventName
     */
    public void deleteEvent(String eventName)
    {
        Selenide.executeJavaScript("$('.workflows-autoassignment-events-event__title:contains(\"" + eventName + "\")').find(\"i\").click()");

        logger.info("Event " + eventName + " was deleted");
    }

    public void clickCancel()
    {
        cancelButton.click();

        logger.info("CANCEL button was clicked");
    }

    public void clickSave()
    {
        saveButton.click();

        logger.info("SAVE button was clicked");
    }
}
