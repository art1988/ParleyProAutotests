package tests.fields.at97;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.FieldType;
import forms.LinkedValues;
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
public class AddPostExecutionFields
{
    private static Logger logger = Logger.getLogger(AddPostExecutionFields.class);

    @Test
    public void addPostExecutionFields() throws InterruptedException
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.createNewFiled("Post-execution", "ff1", FieldType.SELECT, false);
        contractFields.addValues("ff1", "a");
        contractFields.addValues("ff1", "b");

        contractFields.createNewFiled("Post-execution", "ff2", FieldType.SELECT, false);
        contractFields.addValues("ff2", "c");
        contractFields.addValues("ff2", "d");

        contractFields.createNewFiled("Post-execution", "linked1", FieldType.SELECT, false);
        contractFields.addValues("linked1", "1");
        contractFields.addValues("linked1", "2");

        contractFields.createNewFiled("Post-execution", "linked2", FieldType.SELECT, false);

        LinkedValues linkedValuesForm = contractFields.clickLinkValues("linked2");
        linkedValuesForm.selectAssociatedField("linked1");
        linkedValuesForm.clickLink();

        logger.info("Assert that link was created...");
        $(".admin-fields-field__values-linked").waitUntil(Condition.appear, 7_000)
                .shouldHave(Condition.exactText("Link to \nlinked1\n field created\nBreak link"));

        contractFields.addValueAndLinkedValue("linked2", "11", "1");
        Selenide.executeJavaScript("$('input[value=\"linked2\"]').parent().parent().parent().parent().parent().find(\"i:contains('add')\").click()"); // click + Add value
        contractFields.addValueAndLinkedValue("linked2", "22", "2");

        Layout layout = fieldsTab.clickLayout();

        WebElement linked2_field = Selenide.executeJavaScript("return $('.admin-fields-layout-field__label:contains(\"linked2\")').parent().parent()[0]"),
                   ff1_field     = Selenide.executeJavaScript("return $('.admin-fields-layout-field__label:contains(\"ff1\")').parent().parent()[0]");

        logger.info("Perform drag&drop of linked2 and ff1 post-execution fields...");
        dragAndDropFields(linked2_field, ff1_field);

        logger.info("Check that order was changed on Layout page...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.row').find(\".admin-fields-layout__sortable .admin-fields-layout-field__label\").parent().text()"),
                "linked2ff1ff2linked1",
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

        // xOffset = -50 and yOffset = -100 are important !
        // Because dragging is happening between lower row and upper row.
        actions.moveToElement($(field_2).find(".admin-fields-layout__drag-handle"), -50, -100).build().perform();
        Thread.sleep(500);
        actions.release().build().perform();
        Thread.sleep(500);
    }
}
