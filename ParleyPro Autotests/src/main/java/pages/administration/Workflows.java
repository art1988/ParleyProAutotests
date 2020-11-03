package pages.administration;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import pages.tooltips.WorkflowActionsMenu;

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

    public WorkflowActionsMenu clickAddNewWorkflow()
    {
        Selenide.executeJavaScript("$('.btn.btn-light.button_type_add').click()");

        logger.info("+ New Workflow button was clicked");

        return new WorkflowActionsMenu();
    }
}
