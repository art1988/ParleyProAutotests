package tests.fields.at96_99_100;

import com.codeborne.selenide.Condition;
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
public class RemoveCreatedFields
{
    private static Logger logger = Logger.getLogger(RemoveCreatedFields.class);

    @Test(priority = 1)
    public void removeCreatedFields()
    {
        Fields fieldsPage = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsPage.clickContractFields();

        contractFields.removeField("f2").clickDelete();
        contractFields.removeField("f1").clickDelete();

        contractFields.removeField("pe_f2").clickDelete();
        contractFields.removeField("pe_f1").clickDelete();

        contractFields.removeField("cr_f2").clickDelete();
        contractFields.removeField("cr_f1").clickDelete();

        fieldsPage.clickSave();

        logger.info("Assert that fields were completely removed...");
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        Assert.assertFalse($("input[value='f2']").isDisplayed());
        Assert.assertFalse($("input[value='f1']").isDisplayed());

        Assert.assertFalse($("input[value='pe_f2']").isDisplayed());
        Assert.assertFalse($("input[value='pe_f1']").isDisplayed());

        Assert.assertFalse($("input[value='cr_f2']").isDisplayed());
        Assert.assertFalse($("input[value='cr_f1']").isDisplayed());
    }
}
