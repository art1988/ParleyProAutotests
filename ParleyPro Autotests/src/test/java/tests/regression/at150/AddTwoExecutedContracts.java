package tests.regression.at150;

import com.codeborne.selenide.Condition;
import forms.ContractInformation;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class AddTwoExecutedContracts
{
    private static Logger logger = Logger.getLogger(AddTwoExecutedContracts.class);

    @Test(priority = 1)
    public void addTwoExecutedContractsAndAddLinkForContract2() throws InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("contract1");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();
        new AddDocuments();

        contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(false).clickNewContractButton();

        contractInformation.setContractTitle("contract2");
        contractInformation.setContractingRegion("region2");
        contractInformation.setContractingCountry("country2");
        contractInformation.setContractEntity("entity2");
        contractInformation.setContractingDepartment("department2");
        contractInformation.setContractCategory("category2");
        contractInformation.setContractType("type2");
        contractInformation.clickSave();
        new AddDocuments();

        logger.info("Adding linked contract...");
        contractInformation = new OpenedContract(true).clickContractInfo();
        contractInformation.clickByAddContractLinkForLinkedContract();
        contractInformation.setRelationType("Extension to");
        Thread.sleep(1_000);
        contractInformation.setRelatedContract("contract1");
        contractInformation.linkedContractAccept();
        contractInformation.clickSave();

        $(".linked-contracts-label").waitUntil(Condition.appear, 30_000).shouldHave(Condition.text("link 1"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void unlinkContractForContract2() throws InterruptedException
    {
        ContractInformation contractInformation = new OpenedContract().clickContractInfo();
        contractInformation.removeLinkedContract("contract1");

        Thread.sleep(5_000);
    }
}
