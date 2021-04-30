package tests.fields.at120;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.administration.fields_breadcrumb.FieldsRelations;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class RemoveFields
{
    private static Logger logger = Logger.getLogger(RemoveFields.class);

    @Test(priority = 1)
    public void removeFieldRelations() throws InterruptedException
    {
        Fields fieldsTab = new DashboardPage().getSideBar()
                                              .clickAdministration()
                                              .clickFieldsTab();

        FieldsRelations fieldsRelations = fieldsTab.clickFieldsRelations();

        // Expand all 'Fields related to'...
        Selenide.executeJavaScript("$('.admin-fields-relations__field-category').click()");

        for( int n = 1; n <= 6; n++ )
        {
            fieldsRelations.removeField("Field" + n);
            Thread.sleep(500);
        }

        fieldsTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-related:contains(\"Field\")').length === 0"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void removeFields()
    {
        Fields fieldsPage = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsPage.clickContractFields();

        for( int i = 1; i <= 7; i++ )
        {
            contractFields.removeField("Field" + i).clickDelete();
        }

        fieldsPage.clickSave();

        logger.info("Assert that fields were completely removed...");
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        for( int i = 1; i <= 7; i++ )
        {
            Assert.assertFalse($("input[value='Field" + i + "']").isDisplayed());
        }
    }
}
