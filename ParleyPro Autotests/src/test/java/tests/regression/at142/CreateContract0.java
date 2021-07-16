package tests.regression.at142;

import forms.ContractInformation;
import org.testng.annotations.Test;
import pages.DashboardPage;

public class CreateContract0
{
    @Test
    public void createContract0() throws InterruptedException
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("Contract 0");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();


        Thread.sleep(10_000);
    }
}
