package tests.regression.at142;

import forms.ContractInformation;
import org.testng.annotations.Test;
import pages.DashboardPage;


public class CreateContract1AndContract2
{
    @Test(priority = 1)
    public void createContract1()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(false).clickNewContractButton();

        contractInformation.setContractTitle("Contract 1");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("Dep1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();
    }

    @Test(priority = 2)
    public void createContract2()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickExecutedContracts(false).clickNewContractButton();

        contractInformation.setContractTitle("Contract 2");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("Dep2");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();
    }
}
