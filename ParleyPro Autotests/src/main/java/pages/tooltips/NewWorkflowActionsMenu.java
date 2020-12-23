package pages.tooltips;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.workflows.ApprovalWorkflow;
import forms.workflows.ContractRoutingWorkflow;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Popup that appears after clicking on +NEW WORKFLOW button
 */
public class NewWorkflowActionsMenu
{
    private Logger logger = Logger.getLogger(NewWorkflowActionsMenu.class);

    public NewWorkflowActionsMenu()
    {
        $(".dropdown-menu.dropdown-menu-right").waitUntil(Condition.visible, 6_000);
        $$(".dropdown-menu.dropdown-menu-right li").shouldHaveSize(2).shouldHave(CollectionCondition.exactTexts("Contract routing", "Approval"));
    }

    public ApprovalWorkflow clickApproval(boolean inEditMode)
    {
        Selenide.executeJavaScript("$('.dropdown-menu.dropdown-menu-right a:contains(\"Approval\")')[0].click()");

        logger.info("Approval was clicked...");

        return new ApprovalWorkflow(inEditMode);
    }

    public ContractRoutingWorkflow clickContractRouting(boolean inEditMode)
    {
        Selenide.executeJavaScript("$('.dropdown-menu.dropdown-menu-right a:contains(\"Contract routing\")')[0].click()");

        logger.info("Contract routing was clicked...");

        return new ContractRoutingWorkflow(inEditMode);
    }
}
