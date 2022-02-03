package tests.templates.at90;

import com.codeborne.selenide.Condition;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;


public class AddNewContractFromTemplateAndCheckError
{
    private static Logger logger = Logger.getLogger(AddNewContractFromTemplateAndCheckError.class);

    @Test(priority = 1)
    public void addNewContractFromTemplate()
    {
        ContractInformation contractInformationForm = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformationForm.setContractTitle("Silent error when no field setup");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category1");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();


        new AddDocuments().clickSelectTemplateTab().selectTemplate("Template_silent_error_AT-90");

        logger.info("Assert that 'Failed to process the template' error was displayed...");
        $(".notification-stack").waitUntil(Condition.appear, 70_000).shouldHave(Condition.exactText("Failed to process the template. Please, contact the support."));
        Assert.assertEquals($(".notification-stack").getText(), "Failed to process the template. Please, contact the support.", "Didn't get 'Failed to process the template' error message !!!");
        Screenshoter.makeScreenshot();
        $(".notification-stack").waitUntil(Condition.disappear, 75_000);
    }
}
