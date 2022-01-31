package tests.requests.at219;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import model.User;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AdministrationPage;
import pages.DashboardPage;
import pages.administration.Fields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    private static Logger logger = Logger.getLogger(CleanUp.class);


    @Test
    public void cleanUp()
    {
        logger.info("Remove routing workflow...");
        AdministrationPage administrationPage = new DashboardPage().getSideBar().clickAdministration();
        administrationPage.clickWorkflowsTab().clickActionMenu("AT-219_Routing_Workflow").clickDelete().clickDelete();

        $$(".workflows-list__row").shouldHave(CollectionCondition.size(1));

        logger.info("Remove request field...");
        Fields fieldsTab = administrationPage.clickFieldsTab();
        fieldsTab.clickContractFields().removeField("ReqField_AT219_Trigger").clickDelete();
        fieldsTab.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));

        logger.info("Remove user USER_AT219_Requester...");
        administrationPage.clickManageUsersTab().clickActionMenu("USER_AT219_Requester").clickDelete(new User("USER_AT219_Requester fn", ""))
    }
}
