package tests.requests.at251;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import forms.ContractInformation;
import forms.ContractRequest;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;
import pages.subelements.SideBar;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class RejectRequestApprovalTest
{
    private SideBar sideBar;
    private OpenedContract openedContract;


    private static Logger logger = Logger.getLogger(RejectRequestApprovalTest.class);

    @Test
    public void rejectRequestApprovalTest() throws InterruptedException
    {
        asRequesterAddRequest();
        asApproverMakeReject();
        asUserWithFullRightsCheckCancelledContract();
    }

    @Step
    public void asRequesterAddRequest() throws InterruptedException
    {
        sideBar = new DashboardPage().getSideBar();

        LoginPage loginPage = sideBar.logout();
        loginPage.setEmail( Const.PREDEFINED_REQUESTER.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_REQUESTER.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        ContractRequest contractRequest = sideBar.clickInProgressContracts(true).clickNewRequestButton();

        contractRequest.setRequestTitle("AT-251 Request");
        contractRequest.setValueForSelect("AT251-RequestF", "v1");
        contractRequest.uploadCounterpartyDocuments(new File[]{Const.DOCUMENT_DISCUSSIONS_SAMPLE});
        contractRequest.clickSubmitRequest();

        $(byText("AT-251 Request")).shouldBe(Condition.visible);
    }

    @Step
    public void asApproverMakeReject()
    {
        LoginPage loginPage = sideBar.logout();
        loginPage.setEmail( Const.PREDEFINED_APPROVER_USER_1.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_APPROVER_USER_1.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract("AT-251 Request");
        openedContract = new OpenedContract();

        logger.info("Check that both buttons (REJECT REQUEST and APPROVE) are present...");
        $$(".modal-footer button").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("REJECT REQUEST", "APPROVE"));

        new ContractInfo(true).clickRejectRequest().setMessage("AT-251 rejecting message").clickReject();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract Request was successfully rejected"));
    }

    @Step("Login as FELIX(full rights user) and check that after rejection contract become cancelled.")
    public void asUserWithFullRightsCheckCancelledContract() throws InterruptedException
    {
        LoginPage loginPage = sideBar.logout();
        loginPage.setEmail( Const.USER_FELIX.getEmail() );
        loginPage.setPassword( Const.USER_FELIX.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        String curUrl = WebDriverRunner.getWebDriver().getCurrentUrl();
        String urlForCancelledContracts = curUrl.substring(0, curUrl.lastIndexOf("/")) + "/contracts?filter=cancelled";

        Selenide.open(urlForCancelledContracts);

        InProgressContractsPage inProgressContractsPage = new InProgressContractsPage(false);
        inProgressContractsPage.selectContract("AT-251 Request");

        logger.info("Check that cancelled is shown in the request header and document header...");
        $(".contract-header__right .label").shouldBe(Condition.visible).shouldHave(Condition.text("CANCELLED"));
        $(".document__header-info .active").shouldBe(Condition.visible).shouldHave(Condition.text("CANCELLED"));

        openedContract = new OpenedContract();
        ContractInformation contractInformation = openedContract.clickContractInfo();

        $(".reject-request-message").shouldBe(Condition.visible).shouldHave(Condition.text("Contract request has been rejected by Approval_User_1"));
        $(".reject-request-message").shouldHave(Condition.text("Comment: AT-251 rejecting message"));

        Screenshoter.makeScreenshot();

        logger.info("Delete this cancelled contract...");
        openedContract.clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract AT-251 Request has been deleted"));

        logger.info("Wait for 60 sec... Check that email with subject 'Contract request approval rejected' has arrived...");
        Thread.sleep(60_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Contract request approval rejected"),
                "Email with subject: 'Contract request approval rejected' was not found !!!");
    }

    @AfterMethod
    public void cleanUp() throws InterruptedException
    {
        LoginPage loginPage = sideBar.logout();
        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        // remove workflows
        sideBar.clickAdministration().clickWorkflowsTab().clickActionMenu("Approval Workflow AT251").clickDelete().clickDelete();
        Thread.sleep(1_000);
        sideBar.clickAdministration().clickWorkflowsTab().clickActionMenu("Routing Workflow AT251").clickDelete().clickDelete();
        Thread.sleep(1_000);

        $(withText("Approval Workflow AT251")).shouldNot(Condition.visible);
        $(withText("Routing Workflow AT251")).shouldNot(Condition.visible);
    }
}
