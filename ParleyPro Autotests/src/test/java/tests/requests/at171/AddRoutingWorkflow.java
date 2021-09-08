package tests.requests.at171;

import com.codeborne.selenide.CollectionCondition;
import constants.Const;
import forms.workflows.ContractRoutingWorkflow;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AddRoutingWorkflow
{
    @Test(priority = 1)
    public void addRoutingWorkflow()
    {
        ContractRoutingWorkflow contractRoutingWorkflow = new DashboardPage().getSideBar().clickAdministration()
                .clickWorkflowsTab().clickAddNewWorkflow().clickContractRouting(false);

        contractRoutingWorkflow.setName("Routing workflow for AT-171");
        contractRoutingWorkflow.setCurrency("USD");
        contractRoutingWorkflow.setMinValue("2314");
        contractRoutingWorkflow.clickContractRequested();
        contractRoutingWorkflow.setContractRequestedParticipant( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        contractRoutingWorkflow.addFieldAndValue("reqField", "val1");
        contractRoutingWorkflow.clickSave();

        $$(".type_name").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("Name", "Routing workflow for AT-171"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void logoutAsCN()
    {
        new DashboardPage().getSideBar().logout();
    }
}
