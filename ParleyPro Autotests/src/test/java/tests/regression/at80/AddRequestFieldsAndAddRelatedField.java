package tests.regression.at80;

import com.codeborne.selenide.Condition;
import constants.FieldType;
import forms.add.AddNewParentField;
import forms.add.AddRelatedField;
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
public class AddRequestFieldsAndAddRelatedField
{
    private Fields fieldsTab;
    private static Logger logger = Logger.getLogger(AddRequestFieldsAndAddRelatedField.class);

    @Test(priority = 1)
    @Description("This test goes to Administration -> Fields, adds 2 new fields under Contract Request section, Saves and click Next")
    public void addRequestField()
    {
        fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.createNewFiled("Contract Request", "Field1", FieldType.SELECT, false);
        contractFields.addValues("Field1", "Value1");
        contractFields.addValues("Field1", "Value2");
        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        $(".notification.notification_active_yes.notification_type_success").waitUntil(Condition.disappear, 15_000);

        contractFields.clickHideValues();

        contractFields.createNewFiled("Contract Request", "Field2", FieldType.MULTI_SELECT, false);
        contractFields.addValues("Field2", "MS1");
        contractFields.addValues("Field2", "MS2");
        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        Screenshoter.makeScreenshot();

        fieldsTab.clickNext();
    }

    @Test(priority = 2)
    @Description("This test goes to FIELD RELATIONS and adds new parent field")
    public void addParentField()
    {
        FieldsRelations fieldsRelations = fieldsTab.clickFieldsRelations();

        AddNewParentField addNewParentFieldForm = fieldsRelations.addNewParentField();
        addNewParentFieldForm.selectParentField("Field1");
        addNewParentFieldForm.clickCreate();

        AddRelatedField addRelatedFieldForm = fieldsRelations.addRelatedField("Field1");
        addRelatedFieldForm.selectValueForField("Field1", "Value1");
        addRelatedFieldForm.selectFields("Field2");
        addRelatedFieldForm.clickMakeRelated();

        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        logger.info("Assert that related field was added...");
        Assert.assertEquals($("div[class^='styles__field_value_header']").getText(), "Fields related to \nValue1\n value\n1\narrow_drop_down");

        Screenshoter.makeScreenshot();
    }
}
