package tests.fields.at98;

import com.codeborne.selenide.Condition;
import constants.FieldType;
import forms.add.AddNewParentField;
import forms.add.AddRelatedField;
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
public class AddRequestFieldsAndAddRelations
{
    @Test()
    public void addRequestFieldsAndAddRelations()
    {
        Fields fieldsPage = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFieldsTab = fieldsPage.clickContractFields();

        contractFieldsTab.createNewFiled("Contract Request", "R1", FieldType.SELECT, false);
        contractFieldsTab.addValues("R1", "val1");
        contractFieldsTab.addValues("R1", "val2");

        contractFieldsTab.createNewFiled("Contract Request", "f1", FieldType.TEXT, false);
        contractFieldsTab.createNewFiled("Contract Request", "f2", FieldType.TEXT, false);

        FieldsRelations fieldsRelationsTab = fieldsPage.clickFieldsRelations();

        AddNewParentField addNewParentFieldForm = fieldsRelationsTab.addNewParentField();
        addNewParentFieldForm.selectParentField("R1");
        addNewParentFieldForm.clickCreate();

        AddRelatedField addRelatedFieldForm = fieldsRelationsTab.addRelatedField("R1");
        addRelatedFieldForm.selectValueForField("R1", "val1");
        addRelatedFieldForm.selectFields("f1");
        addRelatedFieldForm.clickMakeRelated();

        addRelatedFieldForm = fieldsRelationsTab.addRelatedField("R1");
        addRelatedFieldForm.selectValueForField("R1", "val1");
        addRelatedFieldForm.selectFields("f2");
        addRelatedFieldForm.clickMakeRelated();

        fieldsPage.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        Screenshoter.makeScreenshot();

        // Logout
        new DashboardPage().getSideBar().logout();
    }
}
