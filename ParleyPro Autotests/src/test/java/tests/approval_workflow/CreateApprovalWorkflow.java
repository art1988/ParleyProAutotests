package tests.approval_workflow;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import forms.workflows.ApprovalWorkflow;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AdministrationPage;
import pages.DashboardPage;
import pages.administration.Workflows;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ ScreenShotOnFailListener.class})
public class CreateApprovalWorkflow
{
    private static Logger logger = Logger.getLogger(CreateApprovalWorkflow.class);

    @Test()
    @Description("This test go to Administration, creates new approval workflow")
    public void createApprovalWorkflow()
    {
        DashboardPage dashboardPage = new DashboardPage();

        AdministrationPage administrationPage = new DashboardPage().getSideBar().clickAdministration();

        Workflows workflowsTabPage = administrationPage.clickWorkflowsTab();

        ApprovalWorkflow approvalWorkflowForm = workflowsTabPage.clickAddNewWorkflow().clickApproval(false);

        String workflowName = "Approval_WFL_AT";

        // Settings of Approval workflow
        approvalWorkflowForm.setName(workflowName);
        approvalWorkflowForm.setCategory("category2");
        approvalWorkflowForm.setEntity("entity1");
        approvalWorkflowForm.setType("type2");
        approvalWorkflowForm.setDepartment("department2");
        approvalWorkflowForm.setCurrency("GBP");
        approvalWorkflowForm.setMinValue("250");
        approvalWorkflowForm.setMaxValue("1300");

        approvalWorkflowForm.clickPriorToNegotiate();
        approvalWorkflowForm.setPriorToNegotiateParticipant("Autotest_TEAM_3 [EDITED]");
        approvalWorkflowForm.setPriorToNegotiateParticipant("Approval_User_1");

        approvalWorkflowForm.clickPriorToSign();
        approvalWorkflowForm.setPriorToSignParticipant("Team #2");
        approvalWorkflowForm.setPriorToSignParticipant("Approval_User_2");

        approvalWorkflowForm.switchTumblerApprovalOrderOfPriorToSign();
        approvalWorkflowForm.switchTumblerAllowToModifyApproversOfPriorToSign();

        // Perform drag & drop between 'Team #2' and 'Approval_User_2' so that Approval_User_2 will be first
        // and Team #2 will be second. This will be checked in test StartPreSignApproval test
        Actions actions = new Actions(WebDriverRunner.getWebDriver());
        SelenideElement team2         = $$(".workflows-approval-users-list__item-drag-handle").first(),
                        approvalUser2 = $$(".workflows-approval-users-list__item-drag-handle").last();

        actions.clickAndHold(team2).build().perform();
        actions.moveToElement(approvalUser2).build().perform();

        approvalWorkflowForm.clickSave();

        administrationPage = dashboardPage.getSideBar().clickAdministration();
        workflowsTabPage = administrationPage.clickWorkflowsTab();

        logger.info("Click Edit of just created workflow...;");

        workflowsTabPage.clickActionMenu(workflowName).clickEdit();
        approvalWorkflowForm = new ApprovalWorkflow(true);

        logger.info("Assert that just created Approval workflow has correct values...");
        Assert.assertEquals(approvalWorkflowForm.getName(), workflowName);
        Assert.assertEquals(approvalWorkflowForm.getCategory(), "category2");
        Assert.assertEquals(approvalWorkflowForm.getEntity(), "entity1");
        Assert.assertEquals(approvalWorkflowForm.getType(), "type2");
        Assert.assertEquals(approvalWorkflowForm.getDepartment(), "department2");
        Assert.assertEquals(approvalWorkflowForm.getCurrency(), "GBP");
        Assert.assertEquals(approvalWorkflowForm.getMinValue(), "250.00");
        Assert.assertEquals(approvalWorkflowForm.getMaxValue(), "1,300.00");

        String listOfParticipants = Selenide.executeJavaScript("return $('.workflows-approval-users-list__item-name').text()");
        boolean containsParticipants = listOfParticipants.contains("Autotest_TEAM_3 [EDITED] (3 members)") &&
                                       listOfParticipants.contains("Approval_User_1 (arthur.khasanov+approval1@parleypro.com)") &&
                                       listOfParticipants.contains("Approval_User_2 (arthur.khasanov+approval2@parleypro.com)") &&
                                       listOfParticipants.contains("Team #2 (2 members)");
        Assert.assertTrue(containsParticipants);

        Screenshoter.makeScreenshot();

        approvalWorkflowForm.clickCancel();
    }
}
