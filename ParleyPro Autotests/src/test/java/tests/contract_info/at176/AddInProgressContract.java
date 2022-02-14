package tests.contract_info.at176;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import constants.FieldType;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddInProgressContract
{
    private SoftAssert softAssert = new SoftAssert();
    private static Logger logger = Logger.getLogger(AddInProgressContract.class);

    @Test(priority = 1)
    @Description("This test adds new contract with and fills all custom fields as well as required.")
    public void addInProgressContract() throws InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("Ipsum aliquam ut consequuntur sint. Velit placeat et veniam sint veritatis esse quidem. Iusto consequatur est fuga at voluptas molestias distinctio. Magni saepe quia qui ipsam quis voluptas qui. Exercitationem sit dolor ullam dolorum enim eaque enim. Optio ratione corrupti eos aut ut ratione velit ea");
        contractInformation.setDueDate("Jan 1, 2040");
        contractInformation.setContractValue("100000000000");
        contractInformation.setContractRadioButton("Buy");
        contractInformation.setMyCompanyTemplate(true);
        contractInformation.setContractVisibility(); // check
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");

        contractInformation.setValueForCustomField("CheckboxFld", FieldType.CHECKBOX, "check");
        contractInformation.setValueForCustomField("DateFld", FieldType.DATE, "Jan 1, 2020");
        contractInformation.setValueForCustomField("MultiSelectFld", FieldType.MULTI_SELECT, "MS1");
        contractInformation.setValueForCustomField("NumericFld", FieldType.NUMERIC, "2147483647");
        contractInformation.setValueForCustomField("DecimalFld", FieldType.DECIMAL, "1234567890123.99");
        contractInformation.setValueForCustomField("RadioFld", FieldType.RADIO_BUTTON, "R1");
        contractInformation.setValueForCustomField("OnlySelectFld", FieldType.SELECT, "S1");

        logger.info("Filling Text field..."); // since it is sendKeys - may take long time to fill field by pressing key by key ~5-10 sec
        contractInformation.setValueForCustomField("TextFld", FieldType.TEXT, "Qui assumenda corporis ipsum voluptatem. Similique saepe omnis dicta provident pariatur atque est. Nemo est aut laborum qui eos. Rerum temporibus et quaerat vitae impedit. Recusandae architecto temporibus eum esse dolore quia expedita iure");

        logger.info("Filling Text Area field..."); // same here
        contractInformation.setValueForCustomField("TextAreaFld", FieldType.TEXT_AREA, "Nulla voluptatum vitae a sapiente nisi natus. Ad dolor omnis deleniti qui recusandae et. Corrupti ducimus rerum tenetur non ut hic. Voluptates sunt beatae corrupti deleniti adipisci nobis cum. Sapiente corporis fugiat minima architecto voluptas est nesciunt. Magni nesciunt accusamus quas eveniet numquam voluptatem odit iure.\n" +
                "\n" +
                "Minus dolores pariatur quas voluptas aut ut. Corporis sed voluptatem aliquid officia pariatur id pariatur nostrum. Omnis nobis qui temporibus voluptatum consequatur voluptas illum molestias.\n" +
                "\n" +
                "Ab pariatur corrupti dolores ipsa provident. Dicta eum nobis porro ab et error quis voluptas. Ea dignissimos nulla eos corporis maiores cum unde quos. Aliquid fuga laboriosam tempore. Labore soluta libero est iure quo molestias harum nostrum.");

        contractInformation.setTag("My tag 1");
        contractInformation.clickSave();

        new AddDocuments(); // to make sure that we are on correct page
    }

    @Test(priority = 2)
    public void reopenContractInfo()
    {
        OpenedContract openedContract = new OpenedContract();
        ContractInformation contractInformation = openedContract.clickContractInfo();

        logger.info("Checking general fields...");
        softAssert.assertEquals(contractInformation.getDueDate(), "Jan 1, 2040", "Due date wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractValue(), "100,000,000,000.00", "Contract value wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractRadioButtonSelection(), "Buy", "Contract radio button wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getMyCompanyTemplateRadioButtonSelection(), "Yes", "");
        softAssert.assertEquals(contractInformation.getContractVisibility(), true, "Contract visibility wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractingRegion(), "region1", "Contracting region wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractingCountry(), "country1", "Contracting country wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractEntity(), "entity1", "Contract entity wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractingDepartment(), "department1", "Contracting department wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractCategory(), "category1", "Contract category wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractType(), "type1", "Contract type wasn't saved !!!");

        logger.info("Checking custom fields...");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('span:contains(\"CheckboxFld\")').closest('.checkbox').find('input')[0].checked"), "Checkbox field wasn't saved !!!");
        softAssert.assertEquals($("#datefld").val(), "Jan 1, 2020", "Date field wasn't saved !!!");
        softAssert.assertEquals($("input[data-id='multiselectfld']").val(), "MS1", "Multi Select field wasn't saved !!!");
        softAssert.assertEquals($("input[data-id='numericfld']").val(), "2147483647", "Numeric field wasn't saved !!!");
        softAssert.assertEquals($("input[data-id='decimalfld']").val(), "1234567890123.99", "Decimal field wasn't saved !!!");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('.radio-group__label-text:contains(\"RadioFld\")').closest(\".radio-group\").find(\"input\")[0].checked"), "Radio button R1 should be selected !!!");
        softAssert.assertEquals($("#onlyselectfld").closest(".new-select__value-container").find(".new-select__single-value").getText(), "S1", "Select field wasn't saved !!!");
        softAssert.assertEquals($("#textfld").val(), "Qui assumenda corporis ipsum voluptatem. Similique saepe omnis dicta provident pariatur atque est. Nemo est aut laborum qui eos. Rerum temporibus et quaerat vitae impedit. Recusandae architecto temporibus eum esse dolore quia expedita iure", "Text field wasn't saved !!!");
        softAssert.assertEquals($("textarea[inputid='textareafld']").val(), "Nulla voluptatum vitae a sapiente nisi natus. Ad dolor omnis deleniti qui recusandae et. Corrupti ducimus rerum tenetur non ut hic. Voluptates sunt beatae corrupti deleniti adipisci nobis cum. Sapiente corporis fugiat minima architecto voluptas est nesciunt. Magni nesciunt accusamus quas eveniet numquam voluptatem odit iure.\n" +
                "\n" +
                "Minus dolores pariatur quas voluptas aut ut. Corporis sed voluptatem aliquid officia pariatur id pariatur nostrum. Omnis nobis qui temporibus voluptatum consequatur voluptas illum molestias.\n" +
                "\n" +
                "Ab pariatur corrupti dolores ipsa provident. Dicta eum nobis porro ab et error quis voluptas. Ea dignissimos nulla eos corporis maiores cum unde quos. Aliquid fuga laboriosam tempore. Labore soluta libero est iure quo molestias harum nostrum.",
                "Text Area field wasn't saved !!!");
        contractInformation.getTags().shouldHaveSize(1).shouldHave(CollectionCondition.exactTexts("My tag 1"));

        softAssert.assertAll();
    }

    @Test(priority = 3)
    public void setNewValues() throws InterruptedException
    {
        ContractInformation contractInformation = new ContractInformation(true);

        for( int n = 0; n < 14; n++ ) $("#dueDate").sendKeys(Keys.BACK_SPACE); // clean DueDate field
        contractInformation.setDueDate("Feb 29, 2040");

        contractInformation.setContractValue("0"); Thread.sleep(500);
        contractInformation.setContractRadioButton("Sell"); Thread.sleep(500);
        contractInformation.setMyCompanyTemplate(false); Thread.sleep(500);
        contractInformation.setContractVisibility(); Thread.sleep(500); // uncheck
        contractInformation.setContractingRegion("region2"); Thread.sleep(500);
        contractInformation.setContractingCountry("country2"); Thread.sleep(500);
        contractInformation.setContractEntity("entity2"); Thread.sleep(500);
        contractInformation.setContractingDepartment("department2"); Thread.sleep(500);
        contractInformation.setContractCategory("category2"); Thread.sleep(500);
        contractInformation.setContractType(new String[]{"type2", "type3"}); Thread.sleep(500);

        contractInformation.setValueForCustomField("CheckboxFld", FieldType.CHECKBOX, "uncheck"); Thread.sleep(500);

        for( int n = 0; n < 14; n++ ) $("#datefld").sendKeys(Keys.BACK_SPACE); // clean custom DateFld before setting new val
        contractInformation.setValueForCustomField("DateFld", FieldType.DATE, "Feb 29, 2032");

        $("input[data-id='multiselectfld']").click(); // expand MULTI_SELECT field
        Selenide.executeJavaScript("$('input[data-id=\"multiselectfld\"]').closest('div[class=\"input\"]').next().find(\".multi-select__option:contains('MS1')\").find(\"span\").click()"); // uncheck prev val MS1
        Selenide.executeJavaScript("$('input[data-id=\"multiselectfld\"]').closest('div[class=\"input\"]').next().find(\".multi-select__option:contains('MS2')\").find(\"span\").click()"); // set new val MS2
        $("input[data-id='multiselectfld']").click(); // collapse MULTI_SELECT field

        Thread.sleep(500);
        contractInformation.setValueForCustomField("NumericFld", FieldType.NUMERIC, "0"); Thread.sleep(500);

        for( int n = 0; n < 20; n++ ) $("#decimalfld").sendKeys(Keys.BACK_SPACE); // clean decimal val
        contractInformation.setValueForCustomField("DecimalFld", FieldType.DECIMAL, "0.01");

        contractInformation.setValueForCustomField("RadioFld", FieldType.RADIO_BUTTON, "R2");

        $("#onlyselectfld").sendKeys(Keys.BACK_SPACE); // clean prev value of Select field
        contractInformation.setValueForCustomField("OnlySelectFld", FieldType.SELECT, "S2");

        logger.info("Updating Text field...");
        contractInformation.setValueForCustomField("TextFld", FieldType.TEXT, " ");

        logger.info("Updating Text Area field...");
        contractInformation.setValueForCustomField("TextAreaFld", FieldType.TEXT_AREA, "TEST dolores pariatur quas voluptas aut ut. Corporis sed voluptatem aliquid officia pariatur id pariatur nostrum. Omnis nobis qui temporibus voluptatum consequatur voluptas illum molestias.\n" +
                "\n" +
                "Ab pariatur corrupti dolores ipsa provident. Dicta eum nobis porro ab et error quis voluptas. Ea dignissimos nulla eos corporis maiores cum unde quos. Aliquid fuga laboriosam tempore. Labore soluta libero est iure quo molestias harum nostrum.");

        // del all prev. added tags
        $(".tags-input").findAll(".tags-input__tag__cross").forEach(tagCrossIco -> tagCrossIco.click());

        contractInformation.setTag("My tag 2");
        contractInformation.setTag("My tag 3");

        contractInformation.clickSave();

        new AddDocuments(); // to make sure that we are on correct page
    }

    @Test(priority = 4)
    public void checkSettedNewValues()
    {
        OpenedContract openedContract = new OpenedContract();
        ContractInformation contractInformation = openedContract.clickContractInfo();

        logger.info("Checking general fields...");
        softAssert.assertEquals(contractInformation.getDueDate(), "Feb 29, 2040", "Due date wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractValue(), "0.00", "Contract value wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractRadioButtonSelection(), "Sell", "Contract radio button wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getMyCompanyTemplateRadioButtonSelection(), "No", "");
        softAssert.assertEquals(contractInformation.getContractVisibility(), false, "Contract visibility wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractingRegion(), "region2", "Contracting region wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractingCountry(), "country2", "Contracting country wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractEntity(), "entity2", "Contract entity wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractingDepartment(), "department2", "Contracting department wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractCategory(), "category2", "Contract category wasn't saved !!!");

        // click by 'Contract type' and check that only type2 and type3 were chosen...
        $("input[data-id='contractType']").click(); // expand dropdown
        softAssert.assertFalse(Selenide.executeJavaScript("return $('input[data-id=\"contractType\"]').closest(\".multi-select\").find(\".dropdown-menu\").find(\"input\")[0].checked"), "All Types should be unchecked !!!");
        softAssert.assertFalse(Selenide.executeJavaScript("return $('input[data-id=\"contractType\"]').closest(\".multi-select\").find(\".dropdown-menu\").find(\"input\")[1].checked"), "type1 should be unchecked !!!");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('input[data-id=\"contractType\"]').closest(\".multi-select\").find(\".dropdown-menu\").find(\"input\")[2].checked"), "type2 should be checked !!!");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('input[data-id=\"contractType\"]').closest(\".multi-select\").find(\".dropdown-menu\").find(\"input\")[3].checked"), "type3 should be checked !!!");
        $("input[data-id='contractType']").click(); // collapse dropdown

        softAssert.assertFalse(Selenide.executeJavaScript("return $('#checkboxfld')[0].checked"), "Checkbox field should be unchecked !!!");
        softAssert.assertEquals($("#datefld").val(), "Feb 29, 2032", "Date field wasn't saved !!!");
        softAssert.assertEquals($("input[data-id='multiselectfld']").val(), "MS2", "Multi Select field wasn't saved !!!");

        softAssert.assertEquals($("input[data-id='numericfld']").val(), "0", "Numeric field wasn't saved !!!");

        softAssert.assertEquals($("input[data-id='decimalfld']").val(), "0.01", "Decimal field wasn't saved !!!");
        softAssert.assertTrue(Selenide.executeJavaScript("return $('.radio-group__label-text:contains(\"RadioFld\")').closest(\".radio-group\").find(\"input\")[1].checked"), "Radio button R2 should be selected !!!");
        softAssert.assertEquals($("#onlyselectfld").closest(".new-select__value-container").find(".new-select__single-value").getText(), "S2", "Select field wasn't saved !!!");
        softAssert.assertEquals($("#textfld").val(), "", "Text field wasn't saved !!!");
        softAssert.assertEquals($("textarea[inputid='textareafld']").val(), "TEST dolores pariatur quas voluptas aut ut. Corporis sed voluptatem aliquid officia pariatur id pariatur nostrum. Omnis nobis qui temporibus voluptatum consequatur voluptas illum molestias.\n" +
                        "\n" +
                        "Ab pariatur corrupti dolores ipsa provident. Dicta eum nobis porro ab et error quis voluptas. Ea dignissimos nulla eos corporis maiores cum unde quos. Aliquid fuga laboriosam tempore. Labore soluta libero est iure quo molestias harum nostrum.",
                "Text Area field wasn't saved !!!");
        contractInformation.getTags().shouldHaveSize(2).shouldHave(CollectionCondition.textsInAnyOrder("My tag 3", "My tag 2"));

        softAssert.assertAll();
        Screenshoter.makeScreenshot();

        contractInformation.clickCancel();

        logger.info("Changing contract title...");
        openedContract.renameContract("TEST aliquam ut consequuntur sint. Velit placeat et veniam sint veritatis esse quidem. Iusto consequatur est fuga at voluptas molestias distinctio. Magni saepe quia qui ipsam quis voluptas qui. Exercitationem sit dolor ullam dolorum enim eaque enim. Optio ratione corrupti eos aut ut ratione velit ea");

        InProgressContractsPage inProgressContractsPage = new DashboardPage().getSideBar().clickInProgressContracts(false);

        logger.info("Checking that new contract's title was saved...");
        softAssert.assertEquals($(".contracts-list__contract-name").attr("title"), "TEST aliquam ut consequuntur sint. Velit placeat et veniam sint veritatis esse quidem. Iusto consequatur est fuga at voluptas molestias distinctio. Magni saepe quia qui ipsam quis voluptas qui. Exercitationem sit dolor ullam dolorum enim eaque enim. Optio ratione corrupti eos aut ut ratione velit ea",
                "Looks like that new contract's title wasn't saved !!!");
        softAssert.assertAll();
        Screenshoter.makeScreenshot();
    }
}
