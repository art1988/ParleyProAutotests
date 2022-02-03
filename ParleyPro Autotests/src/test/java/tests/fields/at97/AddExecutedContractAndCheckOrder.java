package tests.fields.at97;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.ContractInfo;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddExecutedContractAndCheckOrder
{
    private static Logger logger = Logger.getLogger(AddExecutedContractAndCheckOrder.class);

    @Test
    public void addExecutedContractAndCheckOrder()
    {
        // + new contract
        ContractInformation contractInformationForm = new DashboardPage().getSideBar().clickExecutedContracts(true).clickNewContractButton();

        contractInformationForm.setContractTitle("Linked post-execution fields");
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
        Assert.assertEquals(Selenide.executeJavaScript("return $('.row span:contains(\"linked2\")').parent().parent().parent().parent().text()"),
                "linked2ff1ff2linked1",
                "The order of post-execution fields linked2, ff1, ff2 and linked1 is wrong !!!");

        Screenshoter.makeScreenshot();
    }
}
