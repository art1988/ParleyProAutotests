package tests.regression.at150;

import forms.ContractInformation;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.ContractInfo;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.ScreenShotOnFailListener;

@Listeners({ScreenShotOnFailListener.class})
public class AddTwoExecutedContracts
{
    @Test
    public void addTwoExecutedContracts() throws InterruptedException
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


        contractInformation = new OpenedContract(true).clickContractInfo();
        contractInformation.clickByAddContractLinkForLinkedContract();
        contractInformation.setRelationType("Extension to");

        Thread.sleep(4_000);
    }
}
