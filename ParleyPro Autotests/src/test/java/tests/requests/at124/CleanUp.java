package tests.requests.at124;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CleanUp
{
    private static Logger logger = Logger.getLogger(CleanUp.class);

    @Test(priority = 1)
    public void removeWorkflow()
    {
        new DashboardPage().getSideBar()
                           .clickAdministration()
                           .clickWorkflowsTab()
                           .clickActionMenu("requests")
                           .clickDelete()
                           .clickDelete();

        try
        {
            Thread.sleep(1_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        logger.info("Assert that there is no contract routing workflow anymore...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.workflows-list__row').length === 1"));
    }

    @Test(priority = 2)
    public void removeFields()
    {
        Fields fieldsPage = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsPage.clickContractFields();

        contractFields.removeField("Contracting region", "Contract Request").clickDelete();
        contractFields.removeField("Contracting country", "Contract Request").clickDelete();
        contractFields.removeField("Contract entity", "Contract Request").clickDelete();
        contractFields.removeField("Contracting department", "Contract Request").clickDelete();
        contractFields.removeField("Contract category", "Contract Request").clickDelete();
        contractFields.removeField("Contract type", "Contract Request").clickDelete();
        contractFields.removeField("Contract value", "Contract Request").clickDelete();

        fieldsPage.clickSave();

        logger.info("Assert that fields were completely removed...");
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields__title:contains(\"Contract Request\")').parent().find(\".admin-fields-staticfield.js-item\").length === 0"),
                "Looks like that not all fields were removed from Contract Request !!!");
    }
}
