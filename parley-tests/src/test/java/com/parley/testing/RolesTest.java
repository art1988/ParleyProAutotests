package com.parley.testing;

import com.parley.testing.model.ExecutedContract;
import com.parley.testing.model.InProgressContract;
import com.parley.testing.pages.impl.*;
import com.parley.testing.runner.AbstractIT;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class RolesTest extends AbstractIT {

    @Test
    public void testContractManagerInProgressContracts() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria@parleypro.com","Parley650!");
        InProgressContractsPage inProgressContractsPage = pageFactory.inProgressContractsPage();

        inProgressContractsPage.checkCreateContractButtonExists();

        //Validate in-progress contract list
        List<InProgressContract> list = inProgressContractsPage.getInProgressContracts();
        inProgressContractsPage.checkContractRequiredFieldsNotEmpty(list);

        //Validate in-progress contract
        String contractLink = getContractLinkByTitle(list, "Test contract");
        assertThat(contractLink, notNullValue());
        inProgressContractsPage.getDriver().get(contractLink);
        InProgressContractPage inProgressContract = pageFactory.inProgressContractPage();
        inProgressContract.checkNewDocumentButtonDisplayed();

        inProgressContract.clickOnContractMenu();
        inProgressContract.checkContractInfoDisplayed();
        inProgressContract.checkAuditTrailDisplayed();
        inProgressContract.checkDeleteContractDisplayed();
        inProgressContract.checkCancelContractDisplayed();

        //Validate executed contract list

        ExecutedContractsPage executedContractsPage = pageFactory.executedContractsPage();
        executedContractsPage.moveToExecutedContracts();
        executedContractsPage.checkCreateContractButtonExists();
        List<ExecutedContract> executedContracts = executedContractsPage.getExecutedContracts();
        executedContractsPage.checkContractRequiredFieldsNotEmpty(executedContracts);

        //Validate executed contract
        String executedContractLink = getExecutedContractLinkByTitle(executedContracts, "Test online");
        assertThat(executedContractLink, notNullValue());
        executedContractsPage.getDriver().get(executedContractLink);
        ExecutedContractPage executedContractPage = pageFactory.executedContractPage();

        executedContractPage.clickOnContractMenu();
        executedContractPage.checkContractInfoDisplayed();
        executedContractPage.checkAuditTrailDisplayed();
        executedContractPage.checkDeleteContractDisplayed();
        executedContractPage.checkCancelContractDisplayed();

    }


    private String getContractLinkByTitle(List<InProgressContract> list, String title){
        for(InProgressContract contract: list){
            if(title.equals(contract.getTitle())){
                return contract.getLink();
            }
        }
        return null;
    }

    private String getExecutedContractLinkByTitle(List<ExecutedContract> list, String title){
        for(ExecutedContract contract: list){
            if(title.equals(contract.getTitle())){
                return contract.getLink();
            }
        }
        return null;
    }
}
