package tests.requests.at174;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.AdministrationPage;
import pages.DashboardPage;
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
        administrationPage.clickWorkflowsTab().clickActionMenu("AT-174 Wfl").clickDelete().clickDelete();

        $$(".workflows-list__row").shouldHave(CollectionCondition.size(1));

        logger.info("Remove request field...");
        Fields fieldsTab = administrationPage.clickFieldsTab();
        fieldsTab.clickContractFields().removeField("FREQ1").clickDelete();
        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.appear, 17_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
