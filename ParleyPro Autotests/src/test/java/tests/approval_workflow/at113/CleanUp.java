package tests.approval_workflow.at113;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import model.User;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
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
        // del User
        new DashboardPage().getSideBar()
                           .clickAdministration()
                           .clickManageUsersTab()
                           .clickActionMenu("U1")
                           .clickDelete(new User("U1", "", "arthur.khasanov+u1@parleypro.com", ""))
                           .clickDelete();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("User U1 deleted successfully"));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        logger.info("Making sure that user U1 was deleted...");
        String allEmails = Selenide.executeJavaScript("return $('.usermanagement__userlist_content_row_email').text()");
        Assert.assertFalse(allEmails.contains("arthur.khasanov+u1@parleypro.com"), "User U1 is still in the list !!!");

        // del Workflow
        new DashboardPage().getSideBar()
                           .clickAdministration()
                           .clickWorkflowsTab()
                           .clickActionMenu("W1")
                           .clickDelete()
                           .clickDelete();

        logger.info("Assert that approval workflow W1 is not in the list...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.workflows-list__row').length === 1"),
                "Workflow W1 is still in the list !!!");

        // del Field
        Fields fieldsTab = new DashboardPage().getSideBar()
                                              .clickAdministration()
                                              .clickFieldsTab();

        fieldsTab.clickContractFields()
                 .removeField("Approve T1")
                 .clickDelete();

        fieldsTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        Assert.assertFalse($("input[value='Approve T1']").isDisplayed(), "Approve T1 field is still in the list !!!");

        // del Team
        new DashboardPage().getSideBar()
                           .clickAdministration()
                           .clickTeamsTab()
                           .clickActionMenu("T1")
                           .clickDelete()
                           .clickDelete();

        logger.info("Assert that team T1 is not in the list...");
        $$(".teams-list .teams-list__row").shouldHave(CollectionCondition.size(0));
    }
}
