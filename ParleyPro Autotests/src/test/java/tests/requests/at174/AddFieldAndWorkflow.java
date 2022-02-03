package tests.requests.at174;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.workflows.ContractRoutingWorkflow;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class AddFieldAndWorkflow
{
    @Test(priority = 1)
    public void addRequestField()
    {
        Fields fields = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fields.clickContractFields();

        contractFields.createNewFiled("Contract Request", "FREQ1", FieldType.SELECT, false);

        contractFields.addValues("FREQ1", "Yes");
        contractFields.addValues("FREQ1", "No");
        fields.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test(priority = 2)
    public void addRoutingWorkflow()
    {
        ContractRoutingWorkflow contractRoutingWorkflow = new DashboardPage().getSideBar().clickAdministration()
                .clickWorkflowsTab().clickAddNewWorkflow().clickContractRouting(false);

        contractRoutingWorkflow.setName("AT-174 Wfl");
        contractRoutingWorkflow.setCurrency("USD");
        contractRoutingWorkflow.setMinValue("5001");

        contractRoutingWorkflow.clickDraftToReview();
        contractRoutingWorkflow.setDraftToReviewParticipant( Const.USER_GREG.getEmail() );

        contractRoutingWorkflow.clickTextChanged();
        contractRoutingWorkflow.setTextChangedParticipant( Const.USER_MARY.getEmail() );

        contractRoutingWorkflow.clickSignatureDeclined();
        contractRoutingWorkflow.setSignatureDeclinedParticipant( Const.USER_FELIX.getEmail() );

        contractRoutingWorkflow.clickUploadCounterpartyDocument();
        contractRoutingWorkflow.setUploadCounterpartyDocumentParticipant( Const.PREDEFINED_INTERNAL_USER_1.getEmail() );

        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant( Const.PREDEFINED_USER_CN_ROLE.getEmail() );

        contractRoutingWorkflow.addFieldAndValue("FREQ1", "Yes");
        contractRoutingWorkflow.clickSave();

        $$(".type_name").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("Name", "AT-174 Wfl"));

        Screenshoter.makeScreenshot();
    }
}
