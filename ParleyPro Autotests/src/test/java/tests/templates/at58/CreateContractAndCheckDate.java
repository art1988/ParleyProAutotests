package tests.templates.at58;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.FieldType;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.OpenedContract;
import pages.administration.Fields;
import pages.administration.fields_breadcrumb.ContractFields;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class CreateContractAndCheckDate
{
    private String contactName = "Nurix: getDateFromCustomField";

    private static Logger logger = Logger.getLogger(CreateContractAndCheckDate.class);

    @Test(priority = 1)
    @Description("This test creates new contract and fills 'Effective Date' custom field with 'Jul 1, 2020' value")
    public void createContract()
    {
        ContractInformation contractInformation = new SideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle(contactName);
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.setValueForCustomField("Effective Date", FieldType.DATE, "Jul 1, 2020");

        contractInformation.clickSave();
    }

    @Test(priority = 2)
    public void selectTemplateAndCheckDate()
    {
        OpenedContract openedContract = new AddDocuments().clickSelectTemplateTab().selectTemplate("nurix_date_problem");

        $(".notification-stack").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Document nurix_date_problem has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 7_000);

        logger.info("Assert that date is equal to: Date: 07/01/2020");
        Assert.assertEquals($(".document-paragraph__content-text").getText().trim(), "Date: 07/01/2020");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test opens Contract Info and checks that 'Effective Date' field has value 'Jul 1, 2020'")
    public void checkContractInformation() throws InterruptedException
    {
        ContractInformation contractInformation = new OpenedContract().clickContractInfo();

        Thread.sleep(1_000);
        Selenide.executeJavaScript("$('.input__label:contains(\"Effective Date\")')[0].scrollIntoView({});");

        logger.info("Assert that date in Contract Info page is equal to: Jul 1, 2020");
        Assert.assertEquals(contractInformation.getValueFromCustomField("Effective Date"), "Jul 1, 2020");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    @Description("This test goes to Administration -> Fields and remove just created custom field by the name 'Effective Date'")
    public void deleteCustomFileld() throws InterruptedException
    {
        Fields fieldsTab = new SideBar().clickAdministration().clickFieldsTab();
        ContractFields contractFields = fieldsTab.clickContractFields();

        contractFields.removeField("Effective Date").clickDelete();
        fieldsTab.clickSave();
        Thread.sleep(1_000);

        logger.info("Assert that field was deleted...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('input[label=\"Field name\"][value=\"Effective Date\"]').length === 0"));
    }
}
