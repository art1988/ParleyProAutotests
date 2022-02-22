package tests.fields.at120;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.FieldType;
import forms.ContractInformation;
import forms.add.AddNewParentField;
import io.qameta.allure.Step;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.administration.fields_breadcrumb.FieldsRelations;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class FieldRelationForCustomSummaryField
{
    private SideBar sideBar;
    private Fields fieldsTab;

    @BeforeMethod
    public void preSetupAddFields()
    {
        sideBar = new DashboardPage().getSideBar();

        fieldsTab = sideBar.clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.createNewFiled("Summary", "CustomSummarySelect", FieldType.SELECT, false);
        contractFields.addValues("CustomSummarySelect", "1");
        contractFields.addValues("CustomSummarySelect", "2");
        contractFields.addValues("CustomSummarySelect", "3");

        contractFields.createNewFiled("Summary", "Field8", FieldType.TEXT, false);

        fieldsTab.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test
    public void checkPresenceOnForm() throws InterruptedException
    {
        makeRelation();

        ContractInformation contractInformation = sideBar.clickInProgressContracts(true).clickNewContractButton();

        // Scroll Contract information to bottom
        Selenide.executeJavaScript("$('.modal__scrollable-body').scrollTop($('.modal__scrollable-body')[0].scrollHeight);");

        $(byText("Field8")).shouldNotBe(Condition.visible);

        contractInformation.setValueForCustomField("CustomSummarySelect", FieldType.SELECT, "2");

        $(byText("Field8")).shouldBe(Condition.visible);
    }

    @Step
    public void makeRelation()
    {
        FieldsRelations fieldsRelations = fieldsTab.clickFieldsRelations();

        AddNewParentField addNewParentField = fieldsRelations.addNewParentFieldForSummary();

        addNewParentField.selectParentField("CustomSummarySelect");
        addNewParentField.clickCreate();

        fieldsRelations.addRelatedField("CustomSummarySelect")
                       .selectValueForField("CustomSummarySelect", "2")
                       .selectRelatedField("Field8")
                       .clickMakeRelated();

        fieldsTab.clickSave();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract fields have been saved."));
    }
}
