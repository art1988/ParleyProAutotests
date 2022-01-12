package tests.requests.at212;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractRequest;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsRequesterAddRequestUpdateValidate
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(LoginAsRequesterAddRequestUpdateValidate.class);


    @Test(priority = 1)
    public void addRequestAndSubmit()
    {
        LoginPage loginPage = new LoginPage();

        logger.info("Login as requester...");
        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        logger.info("Setting request fields...");
        ContractRequest contractRequest = sideBar.clickInProgressContracts(true).clickNewRequestButton();

        contractRequest.setRequestTitle("Request_AT-212");
        contractRequest.selectValueForField("ReqField_AT212_Trigger", "val_1");
        contractRequest.setValueForSelect("ReqField_AT212_SELECT", "SEL_val_1");
        $("input[data-id='reqfield_at212_text']").setValue("arbitrary text ABC_123");
        $("input[data-id='reqfield_at212_num']").setValue("555888");
        contractRequest.clickSubmitRequest();

        $(byText("Request_AT-212")).shouldBe(Condition.visible);
        $(".label_theme_purple").shouldBe(Condition.visible);
    }

    @Test(priority = 2)
    public void openRequestAndUpdateFields() throws InterruptedException
    {
        logger.info("Opening request...");
        sideBar.clickInProgressContracts(false).selectContract("Request_AT-212");
        $(".modal-content").shouldBe(Condition.visible);
        ContractRequest contractRequestForm = new ContractRequest(true);

        logger.info("Updating fields...");
        contractRequestForm.setValueForSelect("ReqField_AT212_SELECT", "SEL_val_2");
        $("input[data-id='reqfield_at212_text']").clear();
        $("input[data-id='reqfield_at212_text']").setValue("Updated val for text field_at212");

        $("input[data-id='reqfield_at212_num']").clear();
        $("input[data-id='reqfield_at212_num']").setValue("333199");

        contractRequestForm.uploadCounterpartyDocuments(new File[]{Const.DOCUMENT_DISCUSSIONS_SAMPLE});

        contractRequestForm.clickUpdateRequest();

        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Your contract request has been updated"));
    }

    @Test(priority = 3)
    @Description("Main assertion happens here. Tests verifies that request fields were updated.")
    public void openAndValidate()
    {
        logger.info("Opening request and check request fields...");

        sideBar.clickInProgressContracts(false).selectContract("Request_AT-212");

        Assert.assertEquals($("#reqfield_at212_trigger").getValue(), "val_1", "Value of ReqField_AT212_Trigger field is wrong !!!");
        Assert.assertEquals($("#reqfield_at212_select").getValue(), "SEL_val_2", "Value of ReqField_AT212_SELECT field is wrong !!!");
        Assert.assertEquals($("#reqfield_at212_text").getValue(), "Updated val for text field_at212", "Value of ReqField_AT212_TEXT field is wrong !!!");
        Assert.assertEquals($("#reqfield_at212_num").getValue(), "333199", "Value of ReqField_AT212_NUM field is wrong !!!");
        $(".upload-field__file").shouldBe(Condition.visible).shouldHave(Condition.text("AT-14.docx"));

        Screenshoter.makeScreenshot();

        new ContractRequest(true).clickCancel();
    }

    @Test(priority = 4)
    public void logoutAsRequester()
    {
        logger.info("Logout as requester...");
        sideBar.logout();
    }
}
