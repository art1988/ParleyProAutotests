package tests.amendment.at69;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.FieldType;
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

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddCategoriesFieldsAndRelations
{
    private static Logger logger = Logger.getLogger(AddCategoriesFieldsAndRelations.class);


    @Test(priority = 1)
    @Description("This test adds 2 new Contract Categories : Testcat and Amendment - Testcat")
    public void addTwoContractCategories()
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();
        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.clickEditValues("Contract category");
        contractFields.addValues("Contract category", "Testcat", 3);
        contractFields.addValues("Contract category", "Amendment - Testcat", 4);

        fieldsTab.clickSave();

        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        Assert.assertEquals((long) Selenide.executeJavaScript("return $('input[data-label=\"Field name\"][value=\"Contract category\"]')" +
                ".parent().parent().parent().parent().parent().find(\".admin-fields-field__values-item\").length"), 4, "Looks like that Contract Categories weren't added !!!");
    }

    @Test(priority = 2)
    @Description("This test adds 10 Fields in Summary (Field1 - Field10) and 5 AmendFields in Summary (AmendFld1 - AmendFld5).")
    public void addFields()
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();
        ContractFields contractFields = fieldsTab.clickContractFields();

        logger.info("Adding 10 new fields for Summary...");
        for( int i = 1; i <= 10; i++ )
        {
            contractFields.createNewFiled("Summary", "Field" + i, FieldType.TEXT, false);
        }

        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        Assert.assertEquals((long) Selenide.executeJavaScript("return $('.admin-fields__title:contains(\"Summary\")')" +
                ".parent().find(\".admin-fields-custom .js-item\").length"), 10, "Looks like that Fields[1-10] weren't added correctly !!!");

        logger.info("Adding 5 more AmendFields for Summary...");
        for( int i = 1; i <= 5; i++ )
        {
            contractFields.createNewFiled("Summary", "AmendFld" + i, FieldType.TEXT, false);
        }

        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
        Assert.assertEquals((long) Selenide.executeJavaScript("return $('.admin-fields__title:contains(\"Summary\")')" +
                ".parent().find(\".admin-fields-custom .js-item\").length"), 15, "Looks like that AmendFields[1-5] weren't added correctly !!!");
    }

    @Test(priority = 3)
    public void addFieldRelations()
    {
        Fields fieldsTab = new DashboardPage().getSideBar().clickAdministration().clickFieldsTab();
        FieldsRelations fieldsRelations = fieldsTab.clickFieldsRelations();

        // adding related field for 'Testcat' category
        for( int i = 1; i <= 10; i++ )
        {
            fieldsRelations.addRelatedField("Contract category")
                           .selectValueForField("Category", "Testcat")
                           .selectRelatedField("Field" + i)
                           .clickMakeRelated();
        }

        // adding related fields for 'Amendment - Testcat' category [ Field1 - Field10 ]
        for( int i = 1; i <= 10; i++ )
        {
            fieldsRelations.addRelatedField("Contract category")
                           .selectValueForField("Category", "Amendment - Testcat")
                           .selectRelatedField("Field" + i)
                           .clickMakeRelated();
        }
        // and [ AmendFld1 - AmendFld5 ]
        for( int i = 1; i <= 5; i++)
        {
            fieldsRelations.addRelatedField("Contract category")
                           .selectValueForField("Category", "Amendment - Testcat")
                           .selectRelatedField("AmendFld" + i)
                           .clickMakeRelated();
        }

        fieldsTab.clickSave();
        $(".notification-stack").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Contract fields have been saved."));
    }
}
