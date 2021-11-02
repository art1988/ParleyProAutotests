package tests.requests.at184;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import constants.FieldType;
import forms.workflows.ContractRoutingWorkflow;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AddFieldAndWorkflow
{
    private static Logger logger = Logger.getLogger(AddFieldAndWorkflow.class);

    @Test(priority = 1)
    public void addRequestField()
    {
        Fields fields = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fields.clickContractFields();

        contractFields.createNewFiled("Contract Request", "ReqField_AT184", FieldType.SELECT, false);

        contractFields.addValues("ReqField_AT184", "val1");
        contractFields.addValues("ReqField_AT184", "val2");
        contractFields.addValues("ReqField_AT184", "val3");
        fields.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test(priority = 2)
    public void addRoutingWorkflow()
    {
        ContractRoutingWorkflow contractRoutingWorkflow = new DashboardPage().getSideBar().clickAdministration()
                .clickWorkflowsTab().clickAddNewWorkflow().clickContractRouting(false);

        contractRoutingWorkflow.setName("AT-184 Routing workflow");
        contractRoutingWorkflow.setCurrency("USD");
        contractRoutingWorkflow.setMinValue("5001");

        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant( Const.PREDEFINED_USER_CN_ROLE.getEmail() );

        contractRoutingWorkflow.addFieldAndValue("ReqField_AT184", "val3");
        contractRoutingWorkflow.clickSave();

        $$(".type_name").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("Name", "AT-184 Routing workflow"));

        Screenshoter.makeScreenshot();
    }
}
