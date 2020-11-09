package tests.approval_workflow;

import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.InProgressContractsPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

@Listeners({ ScreenShotOnFailListener.class})
public class CreateContractNegativeValueRange
{
    private static Logger logger = Logger.getLogger(CreateContractNegativeValueRange.class);

    @Test
    @Description("Document approval workflow negative case : this test creates new contract with value range that differs from approval workflow")
    public void createContractNegativeValueRange()
    {
        // 1. + NEW CONTRACT
        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(true);

        ContractInformation contractInformationForm = inProgressContractsPage.clickNewContractButton();

        contractInformationForm.setContractTitle("Approval workflow negative case [wrong value range]");
        contractInformationForm.setContractCurrency("GBP");
        contractInformationForm.setContractValue("100"); // !!! not in range between [250, 1300]

        Screenshoter.makeScreenshot();

        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department2");
        contractInformationForm.setContractCategory("category2");
        contractInformationForm.setContractType("type2");

        contractInformationForm.clickSave();

        // 2. UPLOAD MY TEAM DOCUMENTS
        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickUploadMyTeamDocuments( Const.CONTRACT_LIFECYCLE_SAMPLE );

        // Wait until document is fully loaded...
        Waiter.smartWaitUntilVisible("$('.document-paragraph__content-text:contains(\"PRAMATA\")')");

        logger.info("Assert that _contract_ header doesn't have APPROVE option...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.contract-header__status .lifecycle').text()"), "DRAFT(1)REVIEWNEGOTIATESIGNMANAGED");

        // hover on DRAFT to show all available stages...
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"pramata\")').parent().parent().parent().next().find('.lifecycle')";
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString+ ".find(\"div:contains('REVIEW')\")");

        logger.info("Assert that _document_ header doesn't have APPROVE option...");
        Assert.assertEquals(Selenide.executeJavaScript("return $('.document__header-info .lifecycle').text()"), "DRAFTREVIEWNEGOTIATESIGN");

        Screenshoter.makeScreenshot();
    }
}
