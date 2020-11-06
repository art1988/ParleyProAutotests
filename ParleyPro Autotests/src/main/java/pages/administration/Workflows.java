package pages.administration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.ApprovalWorkflow;
import org.apache.log4j.Logger;
import pages.tooltips.EditWorkflowActionsMenu;
import pages.tooltips.NewWorkflowActionsMenu;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents selected workflows tab
 */
public class Workflows
{
    private SelenideElement addNewWorkflowButton = $(".btn.btn-light.button_type_add");


    private static Logger logger = Logger.getLogger(Workflows.class);

    public Workflows()
    {
        $(".workflows-list__row.type_header").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Name\nType of flow\nContract Category\nContract Type\nDepartment\nDate"));
    }

    public NewWorkflowActionsMenu clickAddNewWorkflow()
    {
        Selenide.executeJavaScript("$('.btn.btn-light.button_type_add').click()");

        logger.info("+ New Workflow button was clicked");

        return new NewWorkflowActionsMenu();
    }

    /**
     * Click 3 dots button for the given workflow name
     * @param workflowName
     * @return
     */
    public EditWorkflowActionsMenu editApprovalWorkflow(String workflowName)
    {
        Selenide.executeJavaScript("$('.workflows-list .workflows-list__row .type_name:contains(\"" + workflowName + "\")').parent().find(\".type_actions button\").click()");

        return new EditWorkflowActionsMenu();
    }
}
