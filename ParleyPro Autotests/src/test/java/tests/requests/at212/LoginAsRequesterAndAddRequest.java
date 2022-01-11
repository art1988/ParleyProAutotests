package tests.requests.at212;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractRequest;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import java.io.File;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class LoginAsRequesterAndAddRequest
{
    private SideBar sideBar;
    private static Logger logger = Logger.getLogger(LoginAsRequesterAndAddRequest.class);


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
}
