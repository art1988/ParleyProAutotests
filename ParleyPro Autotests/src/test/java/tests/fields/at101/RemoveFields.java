package tests.fields.at101;

import com.codeborne.selenide.Condition;
import org.apache.commons.io.FileUtils;
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
public class RemoveFields
{
    private static Logger logger = Logger.getLogger(RemoveFields.class);

    @Test
    public void removeFields()
    {
        Fields fieldsPage = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsPage.clickContractFields();

        contractFields.removeField("linked2").clickDelete();
        contractFields.removeField("ff1").clickDelete();

        contractFields.removeField("ff2").clickDelete();
        contractFields.removeField("linked1").clickDelete();

        fieldsPage.clickSave();

        logger.info("Assert that fields were completely removed...");
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        Assert.assertFalse($("input[value='linked2']").isDisplayed());
        Assert.assertFalse($("input[value='ff1']").isDisplayed());

        Assert.assertFalse($("input[value='ff2']").isDisplayed());
        Assert.assertFalse($("input[value='linked1']").isDisplayed());
    }
}
