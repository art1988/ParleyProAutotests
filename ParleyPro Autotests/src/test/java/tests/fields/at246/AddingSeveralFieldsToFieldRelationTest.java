package tests.fields.at246;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.FieldType;
import forms.add.AddNewParentField;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.administration.fields_breadcrumb.FieldsRelations;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddingSeveralFieldsToFieldRelationTest
{
    private SideBar sideBar;
    private Fields fieldsTab;

    private static Logger logger = Logger.getLogger(AddingSeveralFieldsToFieldRelationTest.class);


    @Description("Add V1 value for Contract type. Add 2 summary custom fields.")
    @BeforeMethod
    public void preSetupFields()
    {
        sideBar = new DashboardPage().getSideBar();

        fieldsTab = sideBar.clickAdministration().clickFieldsTab();
        ContractFields contractFields = fieldsTab.clickContractFields();

        logger.info("Adding one more value (V1) to Contract type...");
        contractFields.clickEditValues("Contract type");
        contractFields.addValues("Contract type", "V1", 4);

        logger.info("Adding 2 summary custom fields (F2 and F3)...");
        contractFields.createNewFiled("Summary", "F2", FieldType.SELECT, false);
        contractFields.addValues("F2", "V2");

        contractFields.createNewFiled("Summary", "F3", FieldType.SELECT, false);
        contractFields.addValues("F3", "V3");

        fieldsTab.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));
    }

    @Test
    public void addingSeveralFieldsToFieldRelationTest() throws InterruptedException
    {
        FieldsRelations fieldsRelations = fieldsTab.clickFieldsRelations();

        fieldsRelations.addRelatedField("Contract type")
                       .selectValueForField("Contract type","V1")
                       .selectRelatedField("F2")
                       .clickMakeRelated();

        logger.info("Check that relation V1-F2 was added...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V1\")"), "V1 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F2\")"), "F2 is missing !!!");

        fieldsTab.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));

        logger.info("Check that relations are still showing after saving...");
        Thread.sleep(1_000);
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V1\")"), "V1 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F2\")"), "F2 is missing !!!");

        fieldsRelations.addRelatedField("Contract type")
                       .selectValueForField("Contract type","V1")
                       .selectRelatedField("F3")
                       .clickMakeRelated();

        logger.info("Check that relations V1-F2 and V1-F3 are shown for Contract type...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V1\")"), "V1 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F2\")"), "F2 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F3\")"), "F3 is missing !!!");

        fieldsTab.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));

        logger.info("Check that relations are still showing after saving...");
        Thread.sleep(1_000);
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V1\")"), "V1 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F2\")"), "F2 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F3\")"), "F3 is missing !!!");

        logger.info("Expand fields related div...");
        $(".admin-fields-relations__field-toggle").click();
        $(byText("F2")).shouldBe(Condition.visible);
        $(byText("F3")).shouldBe(Condition.visible);

        logger.info("Remove V1-F2 relation...");
        Selenide.executeJavaScript("$('.admin-fields-relations__field-related:contains(\"F2\")').find('i').click()");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V1\")"), "V1 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F3\")"), "F3 is missing !!!");

        fieldsTab.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));
        Thread.sleep(1_000);

        logger.info("Check that only relation V1-F3 is shown...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V1\")"), "V1 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F3\")"), "F3 is missing !!!");
        Assert.assertFalse(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F2\")"), "F2 is still in relation, but shouldn't !!!");

        AddNewParentField addNewParentField = fieldsRelations.addNewParentFieldForSummary();
        addNewParentField.selectParentField("F2");
        addNewParentField.clickCreate();

        fieldsRelations.addRelatedField("F2")
                       .selectValueForField("F2", "V2")
                       .selectRelatedField("F3")
                       .clickMakeRelated();

        logger.info("Check that relation V2-F3 is shown for F2 and relation V1-F3 is shown for Contract type");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"F2\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V2\")"), "V2 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"F2\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F3\")"), "F3 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V1\")"), "V1 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F3\")"), "F3 is missing !!!");

        fieldsTab.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));
        Thread.sleep(1_000);

        logger.info("Check that relations are still showing after saving...");
        logger.info("Expand all related fields divs...");
        $(".admin-fields-relations__field-toggle").click();
        Thread.sleep(2_000);

        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"F2\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V2\")"), "V2 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"F2\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F3\")"), "F3 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"V1\")"), "V1 is missing !!!");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.admin-fields-relations__field-head:contains(\"type\")').next().find('.admin-fields-relations__field-item').eq(0).text().includes(\"F3\")"), "F3 is missing !!!");

        Screenshoter.makeScreenshot();
    }

    @AfterMethod
    public void removeAllRelatedFields() throws InterruptedException
    {
        Thread.sleep(1_000);

        logger.info("Remove all related fields...");
        Selenide.executeJavaScript("$('.admin-fields-relations__field-related:contains(\"F3\")').find('i').click()");

        fieldsTab.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
