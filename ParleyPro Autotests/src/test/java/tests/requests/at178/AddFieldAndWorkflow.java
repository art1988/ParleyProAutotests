package tests.requests.at178;

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

        contractFields.createNewFiled("Contract Request", "REQ1", FieldType.SELECT, false);

        contractFields.addValues("REQ1", "Yes");
        contractFields.addValues("REQ1", "No");
        fields.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test(priority = 2)
    public void addRoutingWorkflow()
    {
        ContractRoutingWorkflow contractRoutingWorkflow = new DashboardPage().getSideBar().clickAdministration()
                .clickWorkflowsTab().clickAddNewWorkflow().clickContractRouting(false);

        contractRoutingWorkflow.setName("AT-178 Routing workflow");
        contractRoutingWorkflow.setCurrency("USD");
        contractRoutingWorkflow.setMinValue("2314");

        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant( Const.PREDEFINED_USER_CN_ROLE.getEmail() );

        contractRoutingWorkflow.clickTextChanged();
        contractRoutingWorkflow.setTextChangedParticipant( Const.USER_MARY.getEmail() );

        contractRoutingWorkflow.addFieldAndValue("REQ1", "Yes");
        contractRoutingWorkflow.clickSave();

        $$(".type_name").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("Name", "AT-178 Routing workflow"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void logout()
    {
        logger.info("Logout as my team CN...");
        new DashboardPage().getSideBar().logout();
    }
}
