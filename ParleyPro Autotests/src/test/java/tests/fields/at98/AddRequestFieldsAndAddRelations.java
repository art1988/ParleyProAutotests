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
    public void addRequestFieldsAndAddRelations() throws InterruptedException
    {
        Fields fieldsPage = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();

        ContractFields contractFieldsTab = fieldsPage.clickContractFields();

        contractFieldsTab.createNewFiled("Contract Request", "R1", FieldType.SELECT, false);
        contractFieldsTab.addValues("R1", "val1");
        contractFieldsTab.addValues("R1", "val2");

        contractFieldsTab.createNewFiled("Contract Request", "f1", FieldType.TEXT, false);
        contractFieldsTab.createNewFiled("Contract Request", "f2", FieldType.TEXT, false);

        FieldsRelations fieldsRelationsTab = fieldsPage.clickFieldsRelations();
        AddNewParentField addNewParentFieldForm = fieldsRelationsTab.addNewParentFieldForRequest();
        addNewParentFieldForm.selectParentField("R1");
        addNewParentFieldForm.clickCreate();

        AddRelatedField addRelatedFieldForm = fieldsRelationsTab.addRelatedField("R1");
        addRelatedFieldForm.selectValueForField("R1", "val1");
        addRelatedFieldForm.selectRelatedField("f1");
        addRelatedFieldForm.clickMakeRelated();

        addRelatedFieldForm = fieldsRelationsTab.addRelatedField("R1");
        addRelatedFieldForm.selectValueForField("R1", "val1");
        addRelatedFieldForm.selectRelatedField("f2");
        addRelatedFieldForm.clickMakeRelated();

        fieldsPage.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 20_000).shouldHave(Condition.exactText("Contract fields have been saved."));

        Screenshoter.makeScreenshot();

        Thread.sleep(1_000);
        // Logout
        new DashboardPage().getSideBar().logout();
    }
}
