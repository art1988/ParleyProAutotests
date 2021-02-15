package tests.regression.at80;

import com.codeborne.selenide.Condition;
import constants.FieldType;
import io.qameta.allure.Description;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.FieldsRelations;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

public class AddRequestFieldsAndAddRelatedField
{
    private Fields fieldsTab;

    @Test(priority = 1)
    @Description("This test goes to Administration -> Fields, adds 2 new fiends under Contract Request section")
    public void addRequestField()
    {
        fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.createNewFiled("Contract Request", "Field1", FieldType.SELECT, false);
        contractFields.addValues("Field1", "Value1");
        contractFields.addValues("Field1", "Value2");
        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

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
    public void addParentField()
    {
        FieldsRelations fieldsRelations = fieldsTab.clickFieldsRelations();

        //fieldsRelations.addNewParentField();
    }
}
