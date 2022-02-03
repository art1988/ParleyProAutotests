package tests.requests.at210;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractRequest;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;

import java.io.File;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;


public class AddRequestUploadAndDeleteDoc
{
    private DashboardPage dashboardPage;
    private static Logger logger = Logger.getLogger(AddRequestUploadAndDeleteDoc.class);


    @Test(priority = 1)
    public void addRequestAndSubmit() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        logger.info("Login as requester...");
        loginPage.setEmail(Const.PREDEFINED_REQUESTER.getEmail());
        loginPage.setPassword(Const.PREDEFINED_REQUESTER.getPassword());

        dashboardPage = loginPage.clickSignIn();

        ContractRequest contractRequest = dashboardPage.getSideBar().clickInProgressContracts(true).clickNewRequestButton();

        contractRequest.setRequestTitle("Request for at-210");
        contractRequest.selectValueForField("ReqField_AT210", "val_1");
        contractRequest.uploadCounterpartyDocuments(new File[]{ Const.DOCUMENT_DISCUSSIONS_SAMPLE });
        contractRequest.clickSubmitRequest();

        $(byText("Request for at-210")).shouldBe(Condition.visible);
        $(".label_theme_purple").shouldBe(Condition.visible);
    }

    @Test(priority = 2)
    public void reopenRequestAndDeleteDoc() throws InterruptedException
    {
        dashboardPage.getSideBar().clickInProgressContracts(false).selectContract("Request for at-210");

        logger.info("Trying to delete attached document...");
        ContractRequest contractRequest = new ContractRequest(true);

        $(".upload-field__file").hover();
        Thread.sleep(500);
        $(".upload-field__file-delete").shouldBe(Condition.visible).click();

        $(".upload-field__file").shouldNotBe(Condition.visible);
        contractRequest.clickUpdateRequest();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Your contract request has been updated."));
    }

    @Test(priority = 3)
    public void logoutRequester()
    {
        dashboardPage.getSideBar().logout();
    }
}
