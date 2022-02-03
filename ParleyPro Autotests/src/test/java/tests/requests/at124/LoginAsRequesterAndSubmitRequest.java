package tests.requests.at124;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractRequest;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.InProgressContractsPage;
import pages.LoginPage;
import utils.Screenshoter;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class LoginAsRequesterAndSubmitRequest
{
    private static Logger logger = Logger.getLogger(LoginAsRequesterAndSubmitRequest.class);

    @Test(priority = 1)
    public void loginAsRequesterAndSubmitRequest() throws InterruptedException
    {
        // Logout first
        LoginPage loginPage = new DashboardPage().getSideBar().logout();

        // Login as REQUESTER
        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );

        DashboardPage dashboardPage = loginPage.clickSignIn();

        ContractRequest contractRequest = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewRequestButton();

        contractRequest.setValueForSelect("Contracting region", "region1");
        contractRequest.setValueForSelect("Contracting country", "country2");
        contractRequest.setValueForSelect("Contract entity", "entity1");
        contractRequest.setValueForSelect("Contracting department", "department2");
        contractRequest.setValueForSelect("Contract category", "category2");
        contractRequest.setContractType("type3");
        contractRequest.setContractCurrency("EUR");
        contractRequest.setContractValue("4500");

        contractRequest.uploadMyTeamDocuments(new File[]{ Const.DOCUMENT_DISCUSSIONS_SAMPLE });
        contractRequest.uploadAttachment(new File[]{ Const.IMG_JPEG_SAMPLE });

        logger.info("Assert that 2 files were added. Assert that Attachments label appeared...");
        $$(".upload-field__files").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("AT-14.docx", "IMG_JPEG.jpeg"));

        contractRequest.clickSubmitRequest();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Your contract request has been submitted."));
        $(".notification-stack").waitUntil(Condition.disappear, 20_000);

        logger.info("Assert that REQUEST label appeared...");
        $(".label.label_theme_purple").shouldBe(Condition.visible).shouldHave(Condition.exactText("REQUEST"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test opens just added contract request and adds one more doc, changes Contracting country and updates request.")
    public void updateRequest() throws InterruptedException
    {
        new InProgressContractsPage(false).selectContract("Contract request");

        ContractRequest editContractRequestForm = new ContractRequest(true);

        logger.info("Adding of one more doc...");
        editContractRequestForm.uploadMyTeamDocuments(new File[]{ Const.DOCUMENT_LIFECYCLE_SAMPLE });

        logger.info("Assert that 3 files are in the list...");
        $$(".upload-field__file").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("AT-14.docx", "pramata.docx"));
        $$(".upload-field__document-name").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("IMG_JPEG.jpeg"));

        editContractRequestForm.setValueForSelect("Contracting country", "country1");
        editContractRequestForm.clickUpdateRequest();

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Your contract request has been updated."));
        $(".notification-stack").waitUntil(Condition.disappear, 20_000);

        Screenshoter.makeScreenshot();

        // Logout as REQUESTER
        new DashboardPage().getSideBar().logout();
    }
}
