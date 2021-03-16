package tests.fields.at96_99_100;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.FieldType;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
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
    public void addFieldsAndDragThem() throws InterruptedException
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.createNewFiled("Summary", "f1", FieldType.TEXT, false);
        contractFields.createNewFiled("Summary", "f2", FieldType.TEXT, false);

        contractFields.createNewFiled("Post-execution", "pe_f1", FieldType.TEXT, false);
        contractFields.createNewFiled("Post-execution", "pe_f2", FieldType.TEXT, false);

        contractFields.createNewFiled("Contract Request", "cr_f1", FieldType.TEXT, false);
        contractFields.createNewFiled("Contract Request", "cr_f2", FieldType.TEXT, false);

        Layout layout = fieldsTab.clickLayout();

        logger.info("Check that order is default on Layout page...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.row').find(\".admin-fields-layout__sortable .admin-fields-layout-field__label\").parent().text()"),
                "f1f2pe_f1pe_f2cr_f1cr_f2",
                "The default order of fields is wrong !!!");

        // Summary fields
        WebElement field_1 = Selenide.executeJavaScript("return $('.admin-fields-layout-field__label:contains(\"f1\")').parent().parent()[0]"),
                   field_2 = Selenide.executeJavaScript("return $('.admin-fields-layout-field__label:contains(\"f2\")').parent().parent()[0]");

        logger.info("Perform drag&drop of summary fields...");
        dragAndDropFields(field_1, field_2);

        // Post-execution fields
        field_1 = Selenide.executeJavaScript("return $('.admin-fields-layout-field__label:contains(\"pe_f1\")').parent().parent()[0]");
        field_2 = Selenide.executeJavaScript("return $('.admin-fields-layout-field__label:contains(\"pe_f2\")').parent().parent()[0]");

        logger.info("Perform drag&drop of post-execution fields...");
        dragAndDropFields(field_1, field_2);

        // Contract Request fields
        field_1 = Selenide.executeJavaScript("return $('.admin-fields-layout-field__label:contains(\"cr_f1\")').parent().parent()[0]");
        field_2 = Selenide.executeJavaScript("return $('.admin-fields-layout-field__label:contains(\"cr_f2\")').parent().parent()[0]");

        logger.info("Perform drag&drop of contract request fields...");
        dragAndDropFields(field_1, field_2);

        logger.info("Check that order was changed on Layout page...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.row').find(\".admin-fields-layout__sortable .admin-fields-layout-field__label\").parent().text()"),
                "f2f1pe_f2pe_f1cr_f2cr_f1",
                "Order of fields wasn't changed !!!");

        Screenshoter.makeScreenshot();

        fieldsTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    private void dragAndDropFields(WebElement field_1, WebElement field_2) throws InterruptedException
    {
        Actions actions = new Actions(WebDriverRunner.getWebDriver());

        $(field_1).hover(); // to make drag-handle <i> element visible
        Thread.sleep(500);
        actions.clickAndHold($(field_1).find(".admin-fields-layout__drag-handle")).build().perform();
        Thread.sleep(500);

        $(field_2).hover(); // to make drag-handle <i> element visible
        Thread.sleep(500);

        actions.moveToElement($(field_2).find(".admin-fields-layout__drag-handle")).build().perform();
        Thread.sleep(500);
        actions.release().build().perform();
        Thread.sleep(500);
    }
}
