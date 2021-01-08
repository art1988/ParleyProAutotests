package tests.routing_workflow;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.workflows.ContractRoutingWorkflow;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Workflows;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;
import static constants.Const.*;

@Listeners({ ScreenShotOnFailListener.class})
public class CreateContractRoutingWorkflow
{
    private static Logger logger = Logger.getLogger(CreateContractRoutingWorkflow.class);

    @Test
    @Description("This test go to Administration, creates new contract routing workflow")
    public void createContractRoutingWorkflow()
    {
        Workflows workflowsTabPage = new DashboardPage().getSideBar().clickAdministration().clickWorkflowsTab();

        ContractRoutingWorkflow contractRoutingWorkflowForm = workflowsTabPage.clickAddNewWorkflow().clickContractRouting(false);

        String workflowName = "Contract_routing_WFL_AT";

        contractRoutingWorkflowForm.setName(workflowName);
        contractRoutingWorkflowForm.setCategory("category1");
        contractRoutingWorkflowForm.setType("type2");
        contractRoutingWorkflowForm.setDepartment("department1");
        contractRoutingWorkflowForm.setCurrency("JPY");
        contractRoutingWorkflowForm.setMinValue("15400");
        contractRoutingWorkflowForm.setMaxValue("32700");

        contractRoutingWorkflowForm.clickDraftToReview();
        contractRoutingWorkflowForm.setDraftToReviewParticipant( PREDEFINED_INTERNAL_USER_1.getFirstName() ); // As User #1

        contractRoutingWorkflowForm.clickTextChanged();
        contractRoutingWorkflowForm.setTextChangedParticipant( USER_MARY.getFirstName() ); // As User #2

        contractRoutingWorkflowForm.clickSignatureDeclined();
        contractRoutingWorkflowForm.setSignatureDeclinedParticipant( PREDEFINED_APPROVER_USER_1.getFirstName() ); // As User #3

        contractRoutingWorkflowForm.clickUploadCounterpartyDocument();
        contractRoutingWorkflowForm.setUploadCounterpartyDocumentParticipant( PREDEFINED_APPROVER_USER_2.getFirstName() ); // As User #4

        contractRoutingWorkflowForm.setRoleForUser( PREDEFINED_APPROVER_USER_2.getFirstName(), "Lead");
        contractRoutingWorkflowForm.clickSave();

        logger.info("Edit of just created workflow and assert that is was saved correctly...");
        workflowsTabPage.clickActionMenu(workflowName).clickEdit();
        contractRoutingWorkflowForm = new ContractRoutingWorkflow(true);
        $$(".workflows-users-list").shouldHave(CollectionCondition.size(4)); // wait until list of participants is fully loaded

        Assert.assertEquals(contractRoutingWorkflowForm.getName(), workflowName);
        Assert.assertEquals(contractRoutingWorkflowForm.getCategory(), "category1");
        Assert.assertEquals(contractRoutingWorkflowForm.getType(), "type2");
        Assert.assertEquals(contractRoutingWorkflowForm.getDepartment(), "department1");
        Assert.assertEquals(contractRoutingWorkflowForm.getCurrency(), "JPY");
        Assert.assertEquals(contractRoutingWorkflowForm.getMinValue(), "15,400");
        Assert.assertEquals(contractRoutingWorkflowForm.getMaxValue(), "32,700");

        String listOfParticipants = Selenide.executeJavaScript("return $('.workflows-users-list__item-name').text()");
        boolean containsParticipants = listOfParticipants.contains(PREDEFINED_INTERNAL_USER_1.getFirstName() + " " + PREDEFINED_INTERNAL_USER_1.getLastName()) &&
                                       listOfParticipants.contains(USER_MARY.getFirstName() + " " + USER_MARY.getLastName()) &&
                                       listOfParticipants.contains(PREDEFINED_APPROVER_USER_1.getFirstName()) &&
                                       listOfParticipants.contains(PREDEFINED_APPROVER_USER_2.getFirstName());

        Assert.assertTrue(containsParticipants);

        logger.info("Assert that User #4 has Lead role...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.workflows-users-list__item-name:contains(\"Approval_User_2\")').parent().find(\"button\").text().trim()"), "Lead");

        Screenshoter.makeScreenshot();

        contractRoutingWorkflowForm.clickCancel();
    }
}
