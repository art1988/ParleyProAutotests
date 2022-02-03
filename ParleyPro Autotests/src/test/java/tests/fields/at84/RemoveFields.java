package tests.fields.at84;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;

import static com.codeborne.selenide.Selenide.$;


public class RemoveFields
{
    private static Logger logger = Logger.getLogger(RemoveFields.class);

    @Test(priority = 1)
    public void removeCreatedFields()
    {
        Fields fieldsPage = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsPage.clickContractFields();

        contractFields.removeField("Field1").clickDelete();
        contractFields.removeField("Field2").clickDelete();
        contractFields.removeField("Field3").clickDelete();

        fieldsPage.clickSave();

        logger.info("Assert that fields were completely removed...");
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        Assert.assertFalse($("input[value='Field1']").isDisplayed(), "Looks like that Field1 wasn't deleted !!!");
        Assert.assertFalse($("input[value='Field2']").isDisplayed(), "Looks like that Field2 wasn't deleted !!!");
        Assert.assertFalse($("input[value='Field3']").isDisplayed(), "Looks like that Field3 wasn't deleted !!!");
    }
}
