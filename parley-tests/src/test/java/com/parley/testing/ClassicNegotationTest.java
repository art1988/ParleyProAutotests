package com.parley.testing;

import com.parley.testing.aspose.WordChangesValidator;
import com.parley.testing.listener.UITestListener;
import com.parley.testing.pages.impl.CreateInProgressContractPage;
import com.parley.testing.pages.impl.InProgressContractPage;
import com.parley.testing.pages.impl.LoginPage;
import com.parley.testing.pages.impl.dashboard.InProgressContractsPage;
import com.parley.testing.runner.AbstractIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.File;

@Listeners(UITestListener.class)
public class ClassicNegotationTest extends AbstractIT {

    @Autowired
    protected WordChangesValidator wordChangesValidator;

    @Test(enabled = false)
    public void testClassicFlow() throws Throwable {
        LoginPage loginPage = pageFactory.loginPage();
        loginPage.getDriver().manage().deleteAllCookies();
        loginPage.login("victoria+cn@parleypro.com","Parley650!");
        InProgressContractsPage inProgressContractsPage = pageFactory.inProgressContractsPage();
        inProgressContractsPage.checkCurrentPage();

        inProgressContractsPage.checkCreateContractButtonExists();
        inProgressContractsPage.clickCreateContractButton();
        CreateInProgressContractPage createInProgressContractPage = pageFactory.createInProgressContractPage();
        createInProgressContractPage.checkCurrentPage();
        createInProgressContractPage.createAcmeContract(true, "ClassicContract");

        InProgressContractPage inProgressContract = pageFactory.inProgressContractPage();
        inProgressContract.checkUploadDocumentMenuIsAvailable();
        //TODO fix method for upload file
        inProgressContract.uploadMyTeamDocument("classic/lists/Numbered_list_original.docx");
        inProgressContract.checkNewDocumentButtonDisplayed();

        //Move to Negotiate
        inProgressContract.moveToNegotiate();
        inProgressContract.checkShareButtonIsDisplayed();

        inProgressContract.uploadNewVersion("classic/lists/Numbered_list_cn_redlined.docx");
        inProgressContract.checkDownloadDocumentIsDisplayed();

        File template = new File(getClass().getClassLoader().getResource("classic/lists/Numbered_list_cn_redlined.docx").getFile());
        File result = inProgressContract.downloadDocument();

        wordChangesValidator.assertRevisions(template, result);

    }
}
