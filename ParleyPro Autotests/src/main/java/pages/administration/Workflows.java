package pages.administration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import pages.tooltips.NewWorkflowActionsMenu;
import pages.tooltips.WorkflowActionMenu;

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
        $(".workflows-list__row.type_header").shouldBe(Condition.visible).shouldHave(Condition.exactText("Name\nType of flow\nContract Category\nContract Type\nDepartment\nEntity\nDate"));
    }

    public NewWorkflowActionsMenu clickAddNewWorkflow()
    {
        Selenide.executeJavaScript("$('.btn.btn-light.button_type_add').click()");

        logger.info("+ New Workflow button was clicked");

        return new NewWorkflowActionsMenu();
    }

    /**
     * Invoke action menu for workflow by workflowName. Click by 3 dots button
     * @param workflowName name of the workflow for which action menu should be invoked
     * @return
     */
    public WorkflowActionMenu clickActionMenu(String workflowName)
    {
        Selenide.executeJavaScript("$('.workflows-list .workflows-list__row .type_name:contains(\"" + workflowName + "\")').parent().find(\".type_actions button\").click()");

        return new WorkflowActionMenu(workflowName);
    }
}
