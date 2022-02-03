package tests.fields.at84;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.FieldType;
import forms.LinkedValues;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddNecessaryFields
{
    @Test
    public void addNecessaryFields() throws InterruptedException
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.createNewFiled("Summary", "Field1", FieldType.SELECT, false);
        contractFields.addValues("Field1", "v1");
        contractFields.addValues("Field1", "v2");

        contractFields.createNewFiled("Summary", "Field2", FieldType.MULTI_SELECT, false);

        LinkedValues linkedValuesForm = contractFields.clickLinkValues("Field2");
        linkedValuesForm.selectAssociatedField("Field1");
        linkedValuesForm.clickLink();

        contractFields.addValueAndLinkedValue("Field2", "v11", "v1");
        Selenide.executeJavaScript("$('input[value=\"Field2\"]').parent().parent().parent().parent().parent().find(\"i:contains('add')\").click()"); // click + Add value
        contractFields.addValueAndLinkedValue("Field2", "v22", "v2");

        contractFields.createNewFiled("Summary", "Field3", FieldType.MULTI_SELECT, false);

        linkedValuesForm = contractFields.clickLinkValues("Field3");
        linkedValuesForm.selectAssociatedField("Field1");
        linkedValuesForm.clickLink();

        contractFields.addValueAndLinkedValue("Field3", "v111", "v1");
        Selenide.executeJavaScript("$('input[value=\"Field3\"]').parent().parent().parent().parent().parent().find(\"i:contains('add')\").click()"); // click + Add value
        contractFields.addValueAndLinkedValue("Field3", "v222", "v2");

        fieldsTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
