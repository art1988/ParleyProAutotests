package tests.regression.at80;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.qameta.allure.Description;
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
public class RemoveFieldAndCheckUI
{
    private Fields fieldsTab;
    private static Logger logger = Logger.getLogger(RemoveFieldAndCheckUI.class);

    @Test(priority = 1)
    @Description("This test removes Field2 field, click Next and verifies that no grey screen on FIELD RELATIONS")
    public void removeFieldAndCheckUI() throws InterruptedException
    {
        fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        // Scroll to bottom of page
        Selenide.executeJavaScript("$('.admin-fields__title:contains(\"Contract Request\")')[0].scrollIntoView({});");
        contractFields.removeField("Field2").clickDelete();
        fieldsTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);
        Screenshoter.makeScreenshot();

        fieldsTab.clickNext();
        Thread.sleep(1_000);

        logger.info("Assert that we still can see Field Relations tab...");
        new FieldsRelations();
        Assert.assertEquals($("div[class^='styles__field_value_header']").getText(), "Fields related to \nValue1\n value\n1\narrow_drop_down");
        Assert.assertEquals($("div[class^='styles__field_head']").getText(), "Field1\nadd\nAdd related field");
        Assert.assertEquals($(".admin-fields-relations__field-head").getText(), "Contract category\nadd\nAdd related field");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void cleanUp()
    {
        logger.info("Go back to CONTRACT FIELDS and remove Field1...");

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.removeField("Field1").clickDelete();
        fieldsTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
