package tests;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import forms.ApprovalWorkflow;
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

    @Test(priority = 1)
    @Description("This test go to Administration, creates new approval workflow")
    public void createApprovalWorkflow()
    {
        DashboardPage dashboardPage = new DashboardPage();

        AdministrationPage administrationPage = dashboardPage.getSideBar().clickAdministration();

        Workflows workflowsTabPage = administrationPage.clickWorkflowsTab();

        ApprovalWorkflow approvalWorkflowForm = workflowsTabPage.clickAddNewWorkflow().clickApproval();

        String workflowName = "Approval_WFL_AT";

        approvalWorkflowForm.setName(workflowName);
        approvalWorkflowForm.setCategory("category2");
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

        // Perform drag & drop between 'Team #2' and 'Approval_User_2'
        Actions actions = new Actions(WebDriverRunner.getWebDriver());
        SelenideElement firstItem  = $$(".workflows-approval-users-list__item-drag-handle").first(),
                        secondItem = $$(".workflows-approval-users-list__item-drag-handle").last();

        actions.clickAndHold(firstItem).build().perform();
        actions.moveToElement(secondItem).build().perform();

        approvalWorkflowForm.clickSave();

        administrationPage = dashboardPage.getSideBar().clickAdministration();
        workflowsTabPage = administrationPage.clickWorkflowsTab();

        workflowsTabPage.editApprovalWorkflow(workflowName).clickEdit();
        approvalWorkflowForm = new ApprovalWorkflow();

        logger.info("Assert that just created Approval workflow has correct values...");
        Assert.assertEquals(approvalWorkflowForm.getName(), workflowName);
        Assert.assertEquals(approvalWorkflowForm.getCategory(), "category2");
        Assert.assertEquals(approvalWorkflowForm.getType(), "type2");
        Assert.assertEquals(approvalWorkflowForm.getDepartment(), "department2");
        Assert.assertEquals(approvalWorkflowForm.getCurrency(), "GBP");
        Assert.assertEquals(approvalWorkflowForm.getMinValue(), "250.00");
        Assert.assertEquals(approvalWorkflowForm.getMaxValue(), "1,300.00");

        String listOfParticipants = Selenide.executeJavaScript("return $('.workflows-approval-users-list__item-name').text()");
        Assert.assertEquals(listOfParticipants, "Autotest_TEAM_3 [EDITED] (3 members)Approval_User_1 (arthur.khasanov+approval1@parleypro.com)Approval_User_2 (arthur.khasanov+approval2@parleypro.com)Team #2 (2 members)");

        Screenshoter.makeScreenshot();
    }
}
