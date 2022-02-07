package tests.approval_workflow.at201;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.workflows.ApprovalWorkflow;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class TryToSaveWkflowWithEmptyRequestApprover
{
    private String workflowName = "Empty_Request_Approver";
    private static Logger logger = Logger.getLogger(TryToSaveWkflowWithEmptyRequestApprover.class);


    @Test(priority = 1)
    public void tryToSaveWkflowWithEmptyRequestApprover()
    {
        ApprovalWorkflow approvalWorkflow =  new DashboardPage().getSideBar()
                                                                .clickAdministration()
                                                                .clickWorkflowsTab()
                                                                .clickAddNewWorkflow()
                                                                .clickApproval(false);

        approvalWorkflow.setName(workflowName);
        approvalWorkflow.clickContractRequest();
        approvalWorkflow.clickSaveExpectingError();

        logger.info("Making sure that error was shown...");
        $(".workflows-approval__error").shouldBe(Condition.visible).shouldHave(Condition.text("Request approval must contain at least 1 user"));
        $(".modal-content").shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();

        logger.info("Adding participant for Contract Request...");
        approvalWorkflow.setContractRequestParticipant(Const.PREDEFINED_INTERNAL_USER_1.getEmail());
        approvalWorkflow.clickSave();

        logger.info("Making sure that Approval Workflow was saved...");
        $$(byText(workflowName)).shouldHave(CollectionCondition.size(1)).first().shouldBe(Condition.visible);
    }

    @Test(priority = 2)
    public void cleanUp()
    {
        new DashboardPage().getSideBar()
                           .clickAdministration()
                           .clickWorkflowsTab()
                           .clickActionMenu(workflowName)
                           .clickDelete().clickDelete();

        logger.info("Assert that there is no workflow in the list...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.workflows-list__row').length === 1"));
    }
}
