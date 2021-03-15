package tests.fields.at96at100;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.FieldType;
import io.qameta.allure.Description;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.administration.fields_breadcrumb.Layout;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddFieldsChangeOrderAndSave
{
    private static Logger logger = Logger.getLogger(AddFieldsChangeOrderAndSave.class);

    @Test(priority = 1)
    @Description("This test goes to Administration -> Fields, adds 2 new fields for Summary and Post-execution and change the order of them on Layout page.")
    public void AddFieldsAndDragThem() throws InterruptedException
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.createNewFiled("Summary", "f1", FieldType.TEXT, false);
        contractFields.createNewFiled("Summary", "f2", FieldType.TEXT, false);

        contractFields.createNewFiled("Post-execution", "pe_f1", FieldType.TEXT, false);
        contractFields.createNewFiled("Post-execution", "pe_f2", FieldType.TEXT, false);

        Layout layout = fieldsTab.clickLayout();

        logger.info("Check that order is default on Layout page...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.row').find(\".admin-fields-layout__sortable\").parent().text()"),
                "drag_indicatorf1drag_indicatorf2drag_indicatorpe_f1drag_indicatorpe_f2", "The default order of fields is wrong !!!");

        // Summary fields
        WebElement field_1 = Selenide.executeJavaScript("return $('.admin-fields-layout-field__label:contains(\"f1\")').parent().parent()[0]"),
                   field_2 = Selenide.executeJavaScript("return $('.admin-fields-layout-field__label:contains(\"f2\")').parent().parent()[0]");

        Actions actions = new Actions(WebDriverRunner.getWebDriver());

        $(field_1).hover(); // to make drag-handle <i> element visible
        Thread.sleep(500);
        actions.clickAndHold($(field_1).find(".admin-fields-layout__drag-handle")).build().perform();
        Thread.sleep(500);

        $(field_2).hover(); // to make drag-handle <i> element visible
        Thread.sleep(500);

        logger.info("Perform drag&drop of summary fields f1 and f2...");
        actions.moveToElement($(field_2).find(".admin-fields-layout__drag-handle")).build().perform();
        Thread.sleep(500);
        actions.release().build().perform();
        Thread.sleep(500);

        // Post-execution fields
        field_1 = Selenide.executeJavaScript("return $('.admin-fields-layout-field__label:contains(\"pe_f1\")').parent().parent()[0]");
        field_2 = Selenide.executeJavaScript("return $('.admin-fields-layout-field__label:contains(\"pe_f2\")').parent().parent()[0]");

        $(field_1).hover(); // to make drag-handle <i> element visible
        Thread.sleep(500);
        actions.clickAndHold($(field_1).find(".admin-fields-layout__drag-handle")).build().perform();
        Thread.sleep(500);

        $(field_2).hover(); // to make drag-handle <i> element visible
        Thread.sleep(500);

        logger.info("Perform drag&drop of post-execution fields pe_f1 and pe_f2...");
        actions.moveToElement($(field_2).find(".admin-fields-layout__drag-handle")).build().perform();
        Thread.sleep(500);
        actions.release().build().perform();
        Thread.sleep(500);

        logger.info("Check that order was changed on Layout page...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.row').find(\".admin-fields-layout__sortable\").parent().text()"),
                "drag_indicatorf2drag_indicatorf1drag_indicatorpe_f2drag_indicatorpe_f1", "Order of fields wasn't changed !!!");

        Screenshoter.makeScreenshot();

        fieldsTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
