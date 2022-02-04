package tests.customer_params.at223;

import com.codeborne.selenide.Condition;
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

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddExecutedAndCheck
{
    private static Logger logger = Logger.getLogger(AddExecutedAndCheck.class);

    @Test
    public void addExecutedAndCheck()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("AT-223 executed postExecutionForLibertyMutual");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");

        contractInformation.clickSave();

        new AddDocuments().clickUploadExecutedDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);

        ContractInfo contractInfo = new ContractInfo();

        logger.info("Assert that Notification is '1m, 2m' and Expiration email to is filled...");
        Assert.assertEquals(contractInfo.getExpirationDateNotification(), "1m, 2m", "Expiration Notification is wrong !!! Should be '1m, 2m' !");
        $(".tags-input__tag").shouldHave(Condition.text("arthur.khasanov+autotestcn@parleypro.com"));
    }
}
