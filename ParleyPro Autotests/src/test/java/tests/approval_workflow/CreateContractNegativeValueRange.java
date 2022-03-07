package tests.approval_workflow;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ ScreenShotOnFailListener.class})
public class CreateContractNegativeValueRange
{
    private static Logger logger = Logger.getLogger(CreateContractNegativeValueRange.class);

    @Test
    @Description("Document approval workflow negative case : this test creates new contract with value range that differs from approval workflow")
    public void createContractNegativeValueRange()
    {
        ContractInformation contractInformationForm = new DashboardPage().getSideBar().clickInProgressContracts(false).clickNewContractButton();

        contractInformationForm.setContractTitle("Approval workflow negative case [wrong value range]");
        contractInformationForm.setContractCurrency("GBP");
        contractInformationForm.setContractValue("100"); // !!! not in range between [250, 1300]
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department2");
        contractInformationForm.setContractCategory("category2");
        contractInformationForm.setContractType("type2");
        contractInformationForm.clickSave();


        new AddDocuments().clickUploadMyTeamDocuments( Const.DOCUMENT_LIFECYCLE_SAMPLE );
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        logger.info("Assert upload notification...");
        $(".notification-stack").should(Condition.appear).shouldHave(Condition.exactText("Document pramata has been successfully uploaded."));
        $(".notification-stack").should(Condition.disappear);

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
