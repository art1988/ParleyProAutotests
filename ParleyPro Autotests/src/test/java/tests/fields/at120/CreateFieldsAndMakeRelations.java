package tests.fields.at120;

import com.codeborne.selenide.Condition;
import constants.FieldType;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.administration.fields_breadcrumb.FieldsRelations;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;


public class CreateFieldsAndMakeRelations
{
    @Test(priority = 1)
    public void createFields()
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.createNewFiled("Summary", "Field1", FieldType.CHECKBOX, false);
        contractFields.createNewFiled("Summary", "Field2", FieldType.DATE, false);
        contractFields.createNewFiled("Summary", "Field3", FieldType.NUMERIC, false);
        contractFields.createNewFiled("Summary", "Field4", FieldType.DECIMAL, false);
        contractFields.createNewFiled("Summary", "Field5", FieldType.RADIO_BUTTON, false);
        contractFields.addValues("Field5", "radio 1");
        contractFields.addValues("Field5", "radio 2");
        contractFields.createNewFiled("Summary", "Field6", FieldType.SELECT, false);
        contractFields.addValues("Field6", "select 1");
        contractFields.addValues("Field6", "select 2");
        contractFields.createNewFiled("Summary", "Field7", FieldType.TEXT_AREA, false);

        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test(priority = 2)
    public void makeFieldRelations()
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        FieldsRelations fieldsRelations = fieldsTab.clickFieldsRelations();

        fieldsRelations.addRelatedField("Contract category")
                       .selectValueForField("Category", "category1")
                       .selectRelatedField("Field1")
                       .clickMakeRelated();

        fieldsRelations.addRelatedField("Contract category")
                       .selectValueForField("Category", "category1")
                       .selectRelatedField("Field2")
                       .clickMakeRelated();

        //

        fieldsRelations.addRelatedField("Contract type")
                       .selectValueForField("Type", "type1")
                       .selectRelatedField("Field1")
                       .clickMakeRelated();

        fieldsRelations.addRelatedField("Contract type")
                       .selectValueForField("Type", "type1")
                       .selectRelatedField("Field3")
                       .clickMakeRelated();

        fieldsRelations.addRelatedField("Contract type")
                       .selectValueForField("Type", "type1")
                       .selectRelatedField("Field6")
                       .clickMakeRelated();

        //

        fieldsRelations.addRelatedField("Contract type")
                       .selectValueForField("Type", "type2")
                       .selectRelatedField("Field4")
                       .clickMakeRelated();

        fieldsRelations.addRelatedField("Contract type")
                       .selectValueForField("Type", "type2")
                       .selectRelatedField("Field5")
                       .clickMakeRelated();

        fieldsRelations.addRelatedField("Contract type")
                       .selectValueForField("Type", "type2")
                       .selectRelatedField("Field6")
                       .clickMakeRelated();

        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        Screenshoter.makeScreenshot();
    }
}
