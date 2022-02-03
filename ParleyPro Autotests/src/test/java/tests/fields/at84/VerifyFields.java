package tests.fields.at84;

import com.codeborne.selenide.Selenide;
import constants.FieldType;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;


public class VerifyFields
{
    private static Logger logger = Logger.getLogger(VerifyFields.class);

    @Test
    @Description("This test set value v1/v2 for Field1 and checks that Field2 has only v11/v22 and Field3 has only v111/v222. ")
    public void verifyFields() throws InterruptedException
    {
        ContractInformation contractInformationForm = new DashboardPage().getSideBar()
                                                                         .clickInProgressContracts(true)
                                                                         .clickNewContractButton();

        contractInformationForm.setValueForCustomField("Field1", FieldType.SELECT, "v1");
        expandFieldAndCheck("Field2", "v11");
        expandFieldAndCheck("Field3", "v111");

        // Close form...
        contractInformationForm.clickCancel();

        //  ...and open again
        contractInformationForm = new DashboardPage().getSideBar()
                                                     .clickInProgressContracts(true)
                                                     .clickNewContractButton();

        contractInformationForm.setValueForCustomField("Field1", FieldType.SELECT, "v2");
        expandFieldAndCheck("Field2", "v22");
        expandFieldAndCheck("Field3", "v222");

        contractInformationForm.clickCancel();
    }

    // Helper method that expands dropdown by the name 'fieldName' and asserts that only 'valueToCheck' is present in the list
    private void expandFieldAndCheck(String fieldName, String valueToCheck) throws InterruptedException
    {
        logger.info("Expand '" + fieldName + "' and check that only '" + valueToCheck + "' is available...");
        Selenide.executeJavaScript("$('.input__label:contains(\"" + fieldName + "\")').parent().find('input').attr('id', '" + fieldName + "')"); // set id dynamically for fieldName
        $("#" + fieldName).click(); // expand dropdown
        Thread.sleep(500);
        Assert.assertEquals(Selenide.executeJavaScript("return $('.dropdown-menu:visible').find(\".multi-select__option\").text()"), valueToCheck,
                "Looks like that " + fieldName + " has more than '" + valueToCheck + "' values to select !!!");

        Screenshoter.makeScreenshot();
    }
}
