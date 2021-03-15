package tests.fields.at96at100;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;
import pages.AddDocuments;
import pages.ContractInfo;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddExecutedContractAndCheckOrderOfPostExecutionFields
{
    private static Logger logger = Logger.getLogger(AddExecutedContractAndCheckOrderOfPostExecutionFields.class);

    @Test(priority = 1)
    @Description("This test goes to Executed contracts, creates new contract and checks order of post-execution fields.")
    public void addExecutedContractAndCheckOrder()
    {
        // + new contract
        ContractInformation contractInformationForm = new DashboardPage().getSideBar().clickExecutedContracts(true).clickNewContractButton();

        contractInformationForm.setContractTitle("executed for post-execution fields");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country2");
        contractInformationForm.setContractEntity("entity2");
        contractInformationForm.setContractingDepartment("department1");
        contractInformationForm.setContractCategory("category2");
        contractInformationForm.setContractType("type1");

        contractInformationForm.clickSave();

        // upload doc
        new AddDocuments().clickUploadExecutedDocuments( Const.DOCUMENT_DISCUSSIONS_SAMPLE );

        $(".notification-stack").waitUntil(Condition.visible, 25_000).shouldHave(Condition.exactText("Document AT-14 has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 25_000);

        ContractInfo contractInfo = new ContractInfo();

        // Scroll Contract Info right panel to the bottom
        Selenide.executeJavaScript("$('.contract-execute-form .modal__scrollable-body').scrollTop($('.contract-execute-form .modal__scrollable-body')[0].scrollHeight);");
        logger.info("Check post-execution field order on Contract Info right panel...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('span:contains(\"pe_f2\")').parent().parent().parent().parent().parent().text()"),
                "pe_f2pe_f1",
                "The order of post-execution fields pe_f1 and pe_f2 is wrong !!!");

        Screenshoter.makeScreenshot();
    }
}
