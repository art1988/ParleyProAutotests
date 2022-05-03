package tests.requests.at256;

import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractRequest;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import java.io.File;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

@Listeners({ScreenShotOnFailListener.class})
public class ApprovalWithATeamTest
{
    private static final String REQUEST_NAME = "at256 request";
    private SideBar sideBar;

    private static Logger logger = Logger.getLogger(ApprovalWithATeamTest.class);


    @Test
    public void approvalWithATeamTest() throws InterruptedException
    {
        asRequesterAddRequest();
    }

    @Step
    public void asRequesterAddRequest() throws InterruptedException
    {
        LoginPage loginPage = new LoginPage();

        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        ContractRequest contractRequest = sideBar.clickInProgressContracts(true).clickNewRequestButton();

        contractRequest.setRequestTitle(REQUEST_NAME);
        contractRequest.setValueForSelect("AT256-RequestF", "v1");
        contractRequest.uploadMyTeamDocuments(new File[]{Const.DOCUMENT_DISCUSSIONS_SAMPLE});
        contractRequest.clickSubmitRequest();

        $(withText(REQUEST_NAME)).shouldBe(Condition.visible);
        $(".contract-status").shouldBe(Condition.visible).shouldHave(Condition.text("APPROVAL"));
    }

    @Step
    public void loginAsTeamMember1()
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail( Const.PREDEFINED_INTERNAL_USER_1.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_INTERNAL_USER_1.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        logger.info("Open request as team member 1...");
        sideBar.clickInProgressContracts(false).selectContract(REQUEST_NAME);
        $(".label").shouldBe(Condition.visible).shouldHave(Condition.text("REQUEST"));
        $(".lifecycle__item.active").shouldBe(Condition.visible).shouldHave(Condition.text("APPROVAL"));

    }
}
