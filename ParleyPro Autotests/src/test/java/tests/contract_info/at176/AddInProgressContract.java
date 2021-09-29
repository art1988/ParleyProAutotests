package tests.contract_info.at176;

import constants.FieldType;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

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
        contractInformation.setContractVisibility();
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

        logger.info("Checking all fields...");
        softAssert.assertEquals(contractInformation.getDueDate(), "Jan 1, 2040", "Due date wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractValue(), "100,000,000,000.00", "Contract value wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractRadioButtonSelection(), "Buy", "Contract radio button wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getMyCompanyTemplateRadioButtonSelection(), "Yes", "");
        softAssert.assertEquals(contractInformation.getContractVisibility(), true, "Contract visibility wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractingRegion(), "region1", "Contracting region wasn't saved !!!");
        softAssert.assertEquals(contractInformation.getContractingCountry(), "country1", "Contracting country wasn't saved !!!");


        softAssert.assertAll();
    }
}
