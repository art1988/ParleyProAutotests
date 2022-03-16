package tests.fields.at236;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.FieldType;
import forms.add.AddNewParentField;
import forms.add.AddRelatedField;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.administration.fields_breadcrumb.FieldsRelations;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckTextAndValues
{
    private SideBar sideBar;
    private Fields fieldsTab;

    private static Logger logger = Logger.getLogger(CheckTextAndValues.class);


    @BeforeMethod
    public void addCustomSummaryFieldsAndCustomRequestFields()
    {
        sideBar = new DashboardPage().getSideBar();

        fieldsTab = sideBar.clickAdministration().clickFieldsTab();

        ContractFields contractFields = fieldsTab.clickContractFields();

        logger.info("Adding custom summary fields...");
        contractFields.createNewFiled("Summary", "SF1", FieldType.SELECT, false);
        contractFields.addValues("SF1", "V1-1");
        contractFields.addValues("SF1", "V1-2");

        contractFields.createNewFiled("Summary", "SF2", FieldType.SELECT, false);
        contractFields.addValues("SF2", "V2-1");
        contractFields.addValues("SF2", "V2-2");

        contractFields.createNewFiled("Summary", "SF3", FieldType.SELECT, false);
        contractFields.addValues("SF3", "V3-1");
        contractFields.addValues("SF3", "V3-2");

        logger.info("Adding custom contract request fields...");
        contractFields.createNewFiled("Contract Request", "RF1", FieldType.MULTI_SELECT, false);
        contractFields.addValues("RF1", "V4-1");
        contractFields.addValues("RF1", "V4-2");

        contractFields.createNewFiled("Contract Request", "RF2", FieldType.MULTI_SELECT, false);
        contractFields.addValues("RF2", "V5-1");
        contractFields.addValues("RF2", "V5-2");

        contractFields.createNewFiled("Contract Request", "RF3", FieldType.MULTI_SELECT, false);
        contractFields.addValues("RF3", "V6-1");
        contractFields.addValues("RF3", "V6-2");

        fieldsTab.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract fields have been saved."));
    }

    @Test
    public void checkTextAndValues() throws InterruptedException
    {
        addParentFields();
        checkTextAndValuesForSF1AndAddOneMoreValue();
        checkTextAndValuesForRF2AndAddOneMoreValue();
        checkRelatedFields();
    }

    @Step("Go to FIELD RELATIONS and add parent fields")
    public void addParentFields()
    {
        FieldsRelations fieldsRelations = fieldsTab.clickFieldsRelations();

        // Summary
        AddNewParentField addNewParentField = fieldsRelations.addNewParentFieldForSummary();
        addNewParentField.selectParentField("SF1");
        addNewParentField.clickCreate();
        fieldsRelations.addRelatedField("SF1").selectValueForField("SF1", "V1-1").selectRelatedField("SF2").clickMakeRelated();

        addNewParentField = fieldsRelations.addNewParentFieldForSummary();
        addNewParentField.selectParentField("SF2");
        addNewParentField.clickCreate();
        fieldsRelations.addRelatedField("SF2").selectValueForField("SF2", "V2-1").selectRelatedField("SF3").clickMakeRelated();

        // Contract Request
        addNewParentField = fieldsRelations.addNewParentFieldForRequest();
        addNewParentField.selectParentField("RF1");
        addNewParentField.clickCreate();
        fieldsRelations.addRelatedField("RF1").selectValueForField("RF1", "V4-1").selectRelatedField("RF2").clickMakeRelated();

        addNewParentField = fieldsRelations.addNewParentFieldForRequest();
        addNewParentField.selectParentField("RF2");
        addNewParentField.clickCreate();
        fieldsRelations.addRelatedField("RF2").selectValueForField("RF2", "V5-1").selectRelatedField("RF3").clickMakeRelated();

        fieldsTab.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract fields have been saved."));
    }

    @Step("Click by 'Add related field' link for SF1 and check text")
    public void checkTextAndValuesForSF1AndAddOneMoreValue() throws InterruptedException
    {
        FieldsRelations fieldsRelations = fieldsTab.clickFieldsRelations();

        AddRelatedField addRelatedField = fieldsRelations.addRelatedField("SF1");
        $(".modal-body-description").shouldHave(Condition.text("Select the sf1 and related field"));

        Selenide.executeJavaScript("$('input[type=text]').eq(0).attr('id', 'inp')");
        // expand dropdown
        Actions actionProvider = new Actions(WebDriverRunner.getWebDriver());
        actionProvider.clickAndHold($("#inp").toWebElement()).build().perform();
        Thread.sleep(1_000);

        $$(".new-select__menu .new-select__option").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("V1-1", "V1-2")).filterBy(Condition.exactText("V1-2")).first().click();
        addRelatedField.selectRelatedField("SF2").clickMakeRelated();

        logger.info("Check that SF1 has 2 relations and SF2 has 1 relation...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"SF1\")').next().find('.admin-fields-relations__field-item').length === 2"), "SF1 doesn't have 2 related fields !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"SF1\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V1-1 SF1\")"), "First related field for SF1 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"SF1\")').next().find('.admin-fields-relations__field-item').eq(1).text().includes(\"V1-2 SF1\")"), "Second related field for SF1 is missing !!!");
    }

    @Step("Click by 'Add related field' link for RF2 and check text")
    public void checkTextAndValuesForRF2AndAddOneMoreValue() throws InterruptedException
    {
        FieldsRelations fieldsRelations = fieldsTab.clickFieldsRelations();

        AddRelatedField addRelatedField = fieldsRelations.addRelatedField("RF2");
        $(".modal-body-description").shouldHave(Condition.text("Select the rf2 and related field"));

        Selenide.executeJavaScript("$('input[type=text]').eq(0).attr('id', 'inp2')");
        // expand dropdown
        Actions actionProvider = new Actions(WebDriverRunner.getWebDriver());
        actionProvider.clickAndHold($("#inp2").toWebElement()).build().perform();
        Thread.sleep(1_000);

        $$(".new-select__menu .new-select__option").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("V5-1", "V5-2")).filterBy(Condition.exactText("V5-2")).first().click();
        addRelatedField.selectRelatedField("RF3").clickMakeRelated();

        logger.info("Check that RF1 has 1 relation and RF2 has 2 relations...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"RF2\")').next().find('.admin-fields-relations__field-item').length === 2"), "RF2 doesn't have 2 related fields !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"RF1\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V4-1 RF1\")"), "Related field for RF1 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"RF2\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V5-1 RF2\")"), "First related field for RF2 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"RF2\")').next().find('.admin-fields-relations__field-item').eq(1).text().includes(\"V5-2 RF2\")"), "Second related field for RF2 is missing !!!");
    }

    @Step("Check related fields for SF1, SF2, RF1, RF2")
    public void checkRelatedFields()
    {
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-related').clone().children().remove().end().text() === 'SF2SF2SF3RF2RF3RF3'"), "Some of related field inside expandable box is missing !!!");
    }
}
