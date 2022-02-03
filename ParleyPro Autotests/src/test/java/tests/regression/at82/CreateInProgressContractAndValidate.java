package tests.regression.at82;

import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.OpenedContract;
import utils.Screenshoter;


public class CreateInProgressContractAndValidate
{
    private String relatedContractName = "3Qcategory1"; // this contract name _should_ always be the same
    private static Logger logger = Logger.getLogger(CreateInProgressContractAndValidate.class);

    @Test
    @Description("This test verifies that after creating contract with linked one, contract title is not empty.")
    public void createInProgressContractAndValidate()
    {
        ContractInformation contractInformation = new DashboardPage().getSideBar().clickInProgressContracts(true).clickNewContractButton();

        contractInformation.clickByAddContractLinkForLinkedContract().setRelationType("Amendment to");
        contractInformation.setRelatedContract(relatedContractName);
        contractInformation.linkedContractAccept();

        contractInformation.setContractingRegion("region1");
        contractInformation.setContractingCountry("country1");
        contractInformation.setContractEntity("entity1");
        contractInformation.setContractingDepartment("department1");
        contractInformation.setContractCategory("category1");
        contractInformation.setContractType("type1");

        contractInformation.clickSave();

        logger.info("Checking that contract title _IS NOT EMPTY_ ...");
        Assert.assertTrue(new OpenedContract().getContractName().startsWith(relatedContractName));

        Screenshoter.makeScreenshot();
    }
}
