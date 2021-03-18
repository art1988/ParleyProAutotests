package tests.fields.at101;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.FieldType;
import forms.LinkedValues;

import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.administration.fields_breadcrumb.Layout;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddSummaryFields
{
    private static Logger logger = Logger.getLogger(AddSummaryFields.class);

    @Test(priority = 1)
    public void addSummaryFields() throws InterruptedException
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.createNewFiled("Summary", "ff1", FieldType.SELECT, false);
        contractFields.addValues("ff1", "a");
        contractFields.addValues("ff1", "b");

        contractFields.createNewFiled("Summary", "ff2", FieldType.SELECT, false);
        contractFields.addValues("ff2", "c");
        contractFields.addValues("ff2", "d");

        contractFields.createNewFiled("Summary", "linked1", FieldType.SELECT, false);
        contractFields.addValues("linked1", "1");
        contractFields.addValues("linked1", "2");

        contractFields.createNewFiled("Summary", "linked2", FieldType.SELECT, false);

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

        // ...
        Thread.sleep(15_000);
    }
}
