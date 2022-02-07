package tests.requests.at175;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.workflows.ContractRoutingWorkflow;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class PreSetup
{
    @Test(priority = 1)
    public void addRequestField()
    {
        Fields fields = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fields.clickContractFields();

        contractFields.createNewFiled("Contract Request", "Who is CN", FieldType.SELECT, false);

        contractFields.addValues("Who is CN", "CN1");
        contractFields.addValues("Who is CN", "CN2");
        fields.clickSave();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test(priority = 2)
    public void addContractRoutingWorkflow()
    {
        ContractRoutingWorkflow contractRoutingWorkflow = new DashboardPage().getSideBar().clickAdministration()
                .clickWorkflowsTab().clickAddNewWorkflow().clickContractRouting(false);

        contractRoutingWorkflow.setName("CN1 Workflow");

        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant( Const.PREDEFINED_USER_CN_ROLE.getEmail() );

        contractRoutingWorkflow.addFieldAndValue("Who is CN", "CN1");
        contractRoutingWorkflow.clickSave();

        $$(".type_name").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("Name", "CN1 Workflow"));
    }

    @Test(priority = 3)
    public void logoutAsCN()
    {
        new DashboardPage().getSideBar().logout();
    }
}
