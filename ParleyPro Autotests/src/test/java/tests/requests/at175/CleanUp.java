package tests.requests.at175;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.administration.Fields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    private DashboardPage dashboardPage;
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(CleanUp.class);


    @Test(priority = 1)
    public void deleteContracts()
    {
        logger.info("Removing contracts 'Test AT-175' and 'AT-175 request'...");
        dashboardPage = new DashboardPage();

        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract("Test AT-175");
        openedContract = new OpenedContract();
        openedContract.clickContractActionsMenu().clickDeleteContract().clickDelete();

        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract("AT-175 request");
        openedContract = new OpenedContract();
        openedContract.clickContractActionsMenu().clickDeleteContract().clickDelete();

        $(".contracts-list").shouldNotBe(Condition.visible); // there are no any contracts in the list
    }

    @Test(priority = 2)
    public void deleteWorkflow()
    {
        logger.info("Removing 'CN1 Workflow' workflow...");
        dashboardPage.getSideBar()
                     .clickAdministration()
                     .clickWorkflowsTab()
                     .clickActionMenu("CN1 Workflow")
                     .clickDelete()
                     .clickDelete();

        $(byText("CN1 Workflow")).shouldNotBe(Condition.visible);
    }

    @Test(priority = 3)
    public void deleteRequestField()
    {
        logger.info("Removing 'Who is CN' field...");
        Fields fieldsPage = dashboardPage.getSideBar().clickAdministration().clickFieldsTab();

        fieldsPage.clickContractFields().removeField("Who is CN").clickDelete();
        fieldsPage.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
