package tests.requests.at185;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AdministrationPage;
import pages.DashboardPage;
import pages.administration.Fields;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class CleanUp
{
    @Test
    public void cleanUp()
    {
        // removing workflows...
        AdministrationPage administrationPage = new DashboardPage().getSideBar().clickAdministration();
        administrationPage.clickWorkflowsTab().clickActionMenu("AT-185 approval_workflow").clickDelete().clickDelete();
        administrationPage.clickWorkflowsTab().clickActionMenu("AT-185 routing_workflow").clickDelete().clickDelete();

        $$(".workflows-list__row").shouldHave(CollectionCondition.size(1));

        // removing fields...
        Fields fieldsTab = administrationPage.clickFieldsTab();
        fieldsTab.clickContractFields().removeField("ReqField_1_AT185_routing", "Contract Request").clickDelete();
        fieldsTab.clickContractFields().removeField("ReqField_2_AT185_approval", "Contract Request").clickDelete();
        fieldsTab.clickContractFields().removeField("Counterparty organization", "Contract Request").clickDelete();
        fieldsTab.clickContractFields().removeField("Counterparty Chief Negotiator", "Contract Request").clickDelete();
        fieldsTab.clickSave();
        $(".notification-stack").shouldHave(Condition.exactText("Contract fields have been saved."));

        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields__title:contains(\"Contract Request\")').parent().find('.js-item').length === 0"),
                "Looks like that not all fields were removed from Contract Request !!!");
    }
}
