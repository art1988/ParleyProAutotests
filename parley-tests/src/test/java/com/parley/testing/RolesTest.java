package com.parley.testing;

import com.parley.testing.model.Template;
import com.parley.testing.model.contracts.ExecutedContract;
import com.parley.testing.model.contracts.InProgressContract;
import com.parley.testing.pages.impl.*;
import com.parley.testing.pages.impl.dashboard.*;
import com.parley.testing.runner.AbstractIT;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class RolesTest extends AbstractIT {

    @Test
    public void testChiefNegotiatorPermissions() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria+cn@parleypro.com","Parley650!");
        InProgressContractsPage inProgressContractsPage = pageFactory.inProgressContractsPage();
        inProgressContractsPage.checkCurrentPage();

        inProgressContractsPage.checkCreateContractButtonExists();

        //Validate in-progress contract list
        List<InProgressContract> list = inProgressContractsPage.getInProgressContracts();
        inProgressContractsPage.checkContractRequiredFieldsNotEmpty(list);

        //Validate in-progress contract
        String contractLink = getContractLinkByTitle(list, "TestContract");
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
        executedContractsPage.checkCurrentPage();
        executedContractsPage.moveToPage();
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


        //Validate dashboard is available

        DashboardPage dashboardPage = pageFactory.dashboardPage();
        dashboardPage.checkCurrentPage();

        //Validate templates page isn't available

        TemplatesPage templatesPage = pageFactory.templatesPage();
        templatesPage.checkPageIconNotExists();

        //Validate administration page isn't available

        AdministrationPage administrationPage = pageFactory.administrationPage();
        administrationPage.checkPageIconNotExists();

    }

    @Test
    public void testContractManagerPermissions() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria+cm@parleypro.com","Parley650!");

        //Validate executed contracts

        ExecutedContractsPage executedContractsPage = pageFactory.executedContractsPage();
        executedContractsPage.checkCurrentPage();
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
        executedContractPage.checkCancelContractNotDisplayed();


        //Validate dashboard is available

        DashboardPage dashboardPage = pageFactory.dashboardPage();
        dashboardPage.checkCurrentPage();

        //Validate templates page isn't available

        TemplatesPage templatesPage = pageFactory.templatesPage();
        templatesPage.checkPageIconNotExists();

        //Validate administration page isn't available

        AdministrationPage administrationPage = pageFactory.administrationPage();
        administrationPage.checkPageIconNotExists();

        //Validate in-progress contract page isn't available

        InProgressContractsPage inProgressContractsPage = pageFactory.inProgressContractsPage();
        inProgressContractsPage.checkPageIconNotExists();

    }

    @Test
    public void testAdminPermissions() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria+admin@parleypro.com","Parley650!");

        //Validate in-progress contracts
        InProgressContractsPage inProgressContractsPage = pageFactory.inProgressContractsPage();
        inProgressContractsPage.checkCurrentPage();

        inProgressContractsPage.checkCreateContractButtonNotDisplayed();

        //Validate executed contract list

        ExecutedContractsPage executedContractsPage = pageFactory.executedContractsPage();
        executedContractsPage.checkCurrentPage();
        executedContractsPage.moveToPage();
        executedContractsPage.checkCreateContractButtonNotDisplayed();

        //Validate dashboard is available

        DashboardPage dashboardPage = pageFactory.dashboardPage();
        dashboardPage.checkCurrentPage();

        //Validate administration page is available

        AdministrationPage administrationPage = pageFactory.administrationPage();
        administrationPage.checkCurrentPage();
        administrationPage.moveToPage();

        administrationPage.checkManageUsersTabExists();
        administrationPage.checkNewUserButtonExists();

        administrationPage.checkIntegrationsTabExists();

        administrationPage.checkWorkFlowsTabExists();
        administrationPage.checkNewWorkFlowButtonExists();

        //Validate templates page is available

        TemplatesPage templatesPage = pageFactory.templatesPage();
        templatesPage.checkCurrentPage();
        templatesPage.moveToPage();
        templatesPage.checkNewTemplateButtonExists();

        //Validate templates list
        List<Template> list = templatesPage.getTemplates();
        templatesPage.checkContractRequiredFieldsNotEmpty(list);

    }


    @Test
    public void testViewerPermissions() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria+viewer@parleypro.com","Parley650!");

        //Validate in-progress contract list

        InProgressContractsPage inProgressContractsPage = pageFactory.inProgressContractsPage();
        inProgressContractsPage.checkCurrentPage();
        inProgressContractsPage.checkCreateContractButtonNotDisplayed();
        List<InProgressContract> list = inProgressContractsPage.getInProgressContracts();
        inProgressContractsPage.checkContractRequiredFieldsNotEmpty(list);

        //Validate documents actions menu and new document button don't exist

        String contractLink = getContractLinkByTitle(list, "TestContract");
        assertThat(contractLink, notNullValue());
        inProgressContractsPage.getDriver().get(contractLink);
        InProgressContractPage inProgressContract = pageFactory.inProgressContractPage();
        inProgressContract.checkNewDocumentButtonNotDisplayed();
        inProgressContract.checkDocumentActionsMenuNotDisplayed();

        //Validate executed contract list

        ExecutedContractsPage executedContractsPage = pageFactory.executedContractsPage();
        executedContractsPage.moveToPage();
        executedContractsPage.checkCurrentPage();
        executedContractsPage.checkCreateContractButtonNotDisplayed();
        List<ExecutedContract> executedContracts = executedContractsPage.getExecutedContracts();
        executedContractsPage.checkContractRequiredFieldsNotEmpty(executedContracts);

        //Validate dashboard is available

        DashboardPage dashboardPage = pageFactory.dashboardPage();
        dashboardPage.checkCurrentPage();

        //Validate templates page isn't available

        TemplatesPage templatesPage = pageFactory.templatesPage();
        templatesPage.checkPageIconNotExists();

        //Validate administration page isn't available

        AdministrationPage administrationPage = pageFactory.administrationPage();
        administrationPage.checkPageIconNotExists();

    }

    @Test
    public void testViewerPlusPermissions() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria+viewerplus@parleypro.com","Parley650!");

        //Validate in-progress contract list

        InProgressContractsPage inProgressContractsPage = pageFactory.inProgressContractsPage();
        inProgressContractsPage.checkCurrentPage();
        inProgressContractsPage.checkCreateContractButtonNotDisplayed();
        inProgressContractsPage.sortByLastActivity();
        List<InProgressContract> list = inProgressContractsPage.getInProgressContracts();
        inProgressContractsPage.checkContractRequiredFieldsNotEmpty(list);

        //Validate documents actions menu and new document button don't exist

        String contractLink = getContractLinkByTitle(list, "TestContract");
        assertThat(contractLink, notNullValue());
        inProgressContractsPage.getDriver().get(contractLink);
        InProgressContractPage inProgressContract = pageFactory.inProgressContractPage();
        inProgressContract.checkNewDocumentButtonNotDisplayed();

        inProgressContract.clickOnDocumentMenu();
        inProgressContract.checkDownloadDocumentIsDisplayed();
        inProgressContract.checkCancelDocumentNotDisplayed();
        inProgressContract.checkFormatDocumentNotDisplayed();
        inProgressContract.checkDeleteDocumentNotDisplayed();

        //Validate executed contract list

        ExecutedContractsPage executedContractsPage = pageFactory.executedContractsPage();
        executedContractsPage.checkCurrentPage();
        executedContractsPage.moveToPage();
        executedContractsPage.checkCreateContractButtonNotDisplayed();
        List<ExecutedContract> executedContracts = executedContractsPage.getExecutedContracts();
        executedContractsPage.checkContractRequiredFieldsNotEmpty(executedContracts);

        //Validate dashboard is available

        DashboardPage dashboardPage = pageFactory.dashboardPage();
        dashboardPage.checkCurrentPage();

        //Validate templates page isn't available

        TemplatesPage templatesPage = pageFactory.templatesPage();
        templatesPage.checkPageIconNotExists();

        //Validate administration page isn't available

        AdministrationPage administrationPage = pageFactory.administrationPage();
        administrationPage.checkPageIconNotExists();

    }

    @Test
    public void testApproverPermissions() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria+approver@parleypro.com","Parley650!");

        //Validate in-progress contract list

        InProgressContractsPage inProgressContractsPage = pageFactory.inProgressContractsPage();
        inProgressContractsPage.checkCurrentPage();
        inProgressContractsPage.checkCreateContractButtonNotDisplayed();
        inProgressContractsPage.sortByLastActivity();
        List<InProgressContract> list = inProgressContractsPage.getInProgressContracts();
        inProgressContractsPage.checkContractRequiredFieldsNotEmpty(list);

        //Validate documents actions menu and new document button don't exist

        String contractLink = getContractLinkByStage(list, "approval");
        assertThat(contractLink, notNullValue());
        inProgressContractsPage.getDriver().get(contractLink);
        InProgressContractPage inProgressContract = pageFactory.inProgressContractPage();
        inProgressContract.checkNewDocumentButtonNotDisplayed();

        inProgressContract.clickOnDocumentMenu();
        inProgressContract.checkDownloadDocumentIsDisplayed();
        inProgressContract.checkCancelDocumentNotDisplayed();
        inProgressContract.checkFormatDocumentNotDisplayed();
        inProgressContract.checkDeleteDocumentNotDisplayed();

        //Validate Approval buttons

        inProgressContract.checkApproveDocumentButtonIsDisplayed();
        inProgressContract.checkRejectDocumentButtonIsDisplayed();

        //Validate dashboard is available

        DashboardPage dashboardPage = pageFactory.dashboardPage();
        dashboardPage.checkCurrentPage();

        //Validate templates page isn't available

        TemplatesPage templatesPage = pageFactory.templatesPage();
        templatesPage.checkPageIconNotExists();

        //Validate administration page isn't available

        AdministrationPage administrationPage = pageFactory.administrationPage();
        administrationPage.checkPageIconNotExists();

    }

    @Test
    public void testCCNPermissions() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria+classic@parleypro.com","Parley650!");

        //Validate in-progress contract list

        InProgressContractsPage inProgressContractsPage = pageFactory.inProgressContractsPage();
        inProgressContractsPage.checkCurrentPage();
        inProgressContractsPage.checkCreateContractButtonNotDisplayed();
        List<InProgressContract> list = inProgressContractsPage.getInProgressContracts();
        inProgressContractsPage.checkContractRequiredFieldsNotEmpty(list);


        String contractLink = getContractLinkByTitle(list, "TestContractNoDiscussions");
        assertThat(contractLink, notNullValue());
        inProgressContractsPage.getDriver().get(contractLink);
        InProgressContractPage inProgressContract = pageFactory.inProgressContractPage();
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

        //Validate executed contract list

        ExecutedContractsPage executedContractsPage = pageFactory.executedContractsPage();
        executedContractsPage.moveToPage();
        executedContractsPage.checkCurrentPage();
        executedContractsPage.checkCreateContractButtonNotDisplayed();
        List<ExecutedContract> executedContracts = executedContractsPage.getExecutedContracts();
        executedContractsPage.checkContractRequiredFieldsNotEmpty(executedContracts);

        //Validate dashboard isn't available

        DashboardPage dashboardPage = pageFactory.dashboardPage();
        dashboardPage.checkPageIconNotExists();

        //Validate templates page isn't available

        TemplatesPage templatesPage = pageFactory.templatesPage();
        templatesPage.checkPageIconNotExists();

        //Validate administration page isn't available

        AdministrationPage administrationPage = pageFactory.administrationPage();
        administrationPage.checkPageIconNotExists();

    }


}