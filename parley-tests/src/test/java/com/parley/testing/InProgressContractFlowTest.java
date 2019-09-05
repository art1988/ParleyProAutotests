package com.parley.testing;

import com.parley.testing.model.contracts.InProgressContract;
import com.parley.testing.pages.impl.CreateInProgressContractPage;
import com.parley.testing.pages.impl.InProgressContractPage;
import com.parley.testing.pages.impl.LoginPage;
import com.parley.testing.pages.impl.dashboard.InProgressContractsPage;
import com.parley.testing.runner.AbstractIT;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class InProgressContractFlowTest extends AbstractIT {

    @Test
    public void testFullContractFlow() throws Throwable {

        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria+cn@parleypro.com","Parley650!");
        InProgressContractsPage inProgressContractsPage = pageFactory.inProgressContractsPage();
        inProgressContractsPage.checkCurrentPage();

        inProgressContractsPage.checkCreateContractButtonExists();
        inProgressContractsPage.clickCreateContractButton();
        CreateInProgressContractPage createInProgressContractPage = pageFactory.createInProgressContractPage();
        createInProgressContractPage.checkCurrentPage();
        createInProgressContractPage.createAcmeContract();

        InProgressContractPage inProgressContract = pageFactory.inProgressContractPage();
        inProgressContract.checkUploadDocumentMenuIsAvailable();

        inProgressContract.uploadTemplate("MSA FINAL (2)");
        inProgressContract.checkNewDocumentButtonDisplayed();

        inProgressContract.checkShareButtonIsDisplayed();
        inProgressContract.checkEditDocumentButtonDisplayed();

        //Check document menu buttons
        inProgressContract.clickOnDocumentMenu();
        inProgressContract.checkDownloadDocumentIsDisplayed();
        inProgressContract.checkCancelDocumentIsDisplayed();
        inProgressContract.checkFormatDocumentIsDisplayed();
        inProgressContract.checkDeleteDocumentIsDisplayed();


        //Move to Review
        inProgressContract.moveToReview();
        inProgressContract.checkShareButtonIsDisplayed();

        //Move to Negotiate
        inProgressContract.moveToNegotiate();
        inProgressContract.checkShareButtonIsDisplayed();

        //Log out
        inProgressContractsPage.logout();

        //Login as CCN
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria+ccn@parleypro.com","Parley650!");

        inProgressContractsPage.checkCurrentPage();
        inProgressContractsPage.checkCreateContractButtonNotDisplayed();
        List<InProgressContract> list = inProgressContractsPage.getInProgressContracts();
        inProgressContractsPage.checkContractRequiredFieldsNotEmpty(list);


        String contractLink = getContractLinkByTitle(list, "TestContract");
        assertThat(contractLink, notNullValue());
        inProgressContractsPage.getDriver().get(contractLink);

        inProgressContract.checkNewDocumentButtonDisplayed();
        inProgressContract.checkShareButtonIsDisplayed();

        inProgressContract.clickOnContractMenu();
        inProgressContract.checkContractInfoNotDisplayed();
        inProgressContract.checkAuditTrailNotDisplayed();
        inProgressContract.checkDeleteContractNotDisplayed();
        inProgressContract.checkCancelContractNotDisplayed();
        inProgressContract.checkReassignCNIsDisplayed();

        inProgressContract.clickOnDocumentMenu();
        inProgressContract.checkDownloadDocumentIsDisplayed();
        inProgressContract.checkCancelDocumentNotDisplayed();
        inProgressContract.checkFormatDocumentNotDisplayed();
        inProgressContract.checkDeleteDocumentNotDisplayed();
        inProgressContract.checkReadyForSignatureButtonIsDisplayed();

        inProgressContract.clickReadyForSignatureButton();

    }

}
