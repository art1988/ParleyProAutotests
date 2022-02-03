package tests.requests.at178;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.AdministrationPage;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import pages.administration.Fields;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class CleanUp
{
    private static Logger logger = Logger.getLogger(CleanUp.class);

    @Test
    public void cleanUp()
    {
        logger.info("Remove routing workflow...");
        AdministrationPage administrationPage = new DashboardPage().getSideBar().clickAdministration();
        administrationPage.clickWorkflowsTab().clickActionMenu("AT-178 Routing workflow").clickDelete().clickDelete();

        $$(".workflows-list__row").shouldHave(CollectionCondition.size(1));

        logger.info("Remove request field...");
        Fields fieldsTab = administrationPage.clickFieldsTab();
        fieldsTab.clickContractFields().removeField("REQ1").clickDelete();
        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.appear, 17_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        logger.info("Remove 'Contract request' from in-progress...");
        InProgressContractsPage inProgressContractsPage = new DashboardPage().getSideBar().clickInProgressContracts(false);

        inProgressContractsPage.selectContract("Contract request");
        new OpenedContract().clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").waitUntil(Condition.appear, 17_000).shouldHave(Condition.text("Contract Contract request has been deleted."));
    }
}
