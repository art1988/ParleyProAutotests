package tests.regression.at123;

import constants.Const;
import forms.ContractInformation;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.LoginPage;
import utils.Screenshoter;


public class CreateContractsUnderDifferentUsers
{
    private static LoginPage loginPage;

    @Test(priority = 1)
    public void createContractUnderAaronAaronson()
    {
        loginPage = new DashboardPage().getSideBar().logout();

        loginPage.setEmail( Const.Aaron_Aaronson.getEmail() );
        loginPage.setPassword( Const.Aaron_Aaronson.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        ContractInformation contractInformation = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("CTR under Aaron Aaronson");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments();
        Screenshoter.makeScreenshot();

        loginPage = dashboardPage.getSideBar().logout();
    }

    @Test(priority = 2)
    public void createContractUnderBartholomewAaronson()
    {
        loginPage.setEmail( Const.Bartholomew_Aaronson.getEmail() );
        loginPage.setPassword( Const.Bartholomew_Aaronson.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        ContractInformation contractInformation = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("CTR under Bartholomew Aaronson");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments();
        Screenshoter.makeScreenshot();

        loginPage = dashboardPage.getSideBar().logout();
    }

    @Test(priority = 3)
    public void createContractUnderBartholomewBronson()
    {
        loginPage.setEmail( Const.Bartholomew_Bronson.getEmail() );
        loginPage.setPassword( Const.Bartholomew_Bronson.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        ContractInformation contractInformation = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.setContractTitle("CTR under Bartholomew Bronson");
        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");
        contractInformation.clickSave();

        new AddDocuments();
        Screenshoter.makeScreenshot();

        loginPage = dashboardPage.getSideBar().logout();
    }
}
