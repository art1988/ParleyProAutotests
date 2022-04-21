package tests.requests.at250;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractRequest;
import io.qameta.allure.Step;
import model.AuditTrailEvent;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.*;
import pages.subelements.SideBar;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.util.List;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class OrderedApprovalWithSeveralUsersTest
{
    private SideBar sideBar;
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(OrderedApprovalWithSeveralUsersTest.class);


    @Test
    public void orderedApprovalWithSeveralUsersTest() throws InterruptedException
    {
        asRequesterAddRequest();
        asApprover2CheckRequest();
        asApprover1CheckRequest();
        asApprover2CheckRequestAgain();
        asCNCheckRequest();
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

        contractRequest.setRequestTitle("AT-250 Request");
        contractRequest.setValueForSelect("AT250-RequestF", "v1");
        contractRequest.uploadMyTeamDocuments(new File[]{Const.DOCUMENT_DISCUSSIONS_SAMPLE});
        contractRequest.clickSubmitRequest();

        $(byText("AT-250 Request")).shouldBe(Condition.visible);
        $(".label_theme_purple").shouldBe(Condition.visible);
        $(".contract-status").shouldBe(Condition.visible).shouldHave(Condition.exactText("APPROVAL"));

        logger.info("Wait for 60 sec... Check that email with subject 'Contract request approval' has arrived...");
        Thread.sleep(60_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Contract request approval"),
                "Email with subject: 'Contract request approval' was not found !!!");
    }

    @Step
    public void asApprover2CheckRequest()
    {
        LoginPage loginPage = sideBar.logout();
        loginPage.setEmail( Const.PREDEFINED_APPROVER_USER_2.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_APPROVER_USER_2.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract("AT-250 Request");
        openedContract = new OpenedContract();

        $(".lifecycle__item.active").shouldBe(Condition.visible).shouldHave(Condition.exactText("APPROVAL"));
        $(".label").shouldBe(Condition.visible).shouldHave(Condition.exactText("REQUEST"));

        logger.info("Checking users in header...");
        ElementsCollection userIcons = $$(".contract-header-users__list .user").shouldHave(CollectionCondition.size(2));
        for( int i = 0; i < userIcons.size(); i++ )
        {
            StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true}); ");
            jsCode.append("$('.contract-header-users__list .user').eq(" + i + ")[0].dispatchEvent(event);");
            Selenide.executeJavaScript(jsCode.toString());

            $(".rc-tooltip-inner .spinner").should(Condition.disappear);
            $(".contract-user__name").shouldBe(Condition.visible).shouldHave(Condition.text("Approval_User_"));

            Selenide.executeJavaScript("$('.rc-tooltip-inner').hide(); $('.rc-tooltip-inner').remove()");
        }

        logger.info("Check that only CANCEL button is visible in footer...");
        $$(".modal-footer button").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText("CANCEL"));
    }

    @Step
    public void asApprover1CheckRequest() throws InterruptedException
    {
        LoginPage loginPage = sideBar.logout();
        loginPage.setEmail( Const.PREDEFINED_APPROVER_USER_1.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_APPROVER_USER_1.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract("AT-250 Request");
        openedContract = new OpenedContract();

        logger.info("Check that both buttons (REJECT REQUEST and APPROVE) are present...");
        $$(".modal-footer button").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("REJECT REQUEST", "APPROVE"));
        new ContractInfo(true).clickApproveRequest().clickApprove();

        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Contract Request was successfully approved"));

        // reopen request
        sideBar.clickInProgressContracts(false).selectContract("AT-250 Request");
        openedContract = new OpenedContract();

        $(".label").shouldBe(Condition.visible).shouldHave(Condition.exactText("REQUEST"));
        $$(".contract-header-users__list .user").shouldHave(CollectionCondition.size(2)); // 2 user icons
        $$(".user-icon-checked").shouldHave(CollectionCondition.size(1)); // only one checkmark
        $(".user-icon-checked").parent().hover();
        Thread.sleep(1_000);
        $(".rc-tooltip-inner .spinner").should(Condition.disappear);
        $(".contract-user__name").shouldBe(Condition.visible).shouldHave(Condition.text("Approval_User_1"));

        logger.info("Check that only CANCEL button is visible in footer...");
        $$(".modal-footer button").shouldHave(CollectionCondition.size(1)).first().shouldHave(Condition.exactText("CANCEL"));

        logger.info("Wait for 60 sec... Check that email with subject 'Contract request approval' has arrived...");
        Thread.sleep(60_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Contract request approval"),
                "Email with subject: 'Contract request approval' was not found !!!");
    }

    @Step
    public void asApprover2CheckRequestAgain()
    {
        LoginPage loginPage = sideBar.logout();
        loginPage.setEmail( Const.PREDEFINED_APPROVER_USER_2.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_APPROVER_USER_2.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        InProgressContractsPage inProgressContractsPage = sideBar.clickInProgressContracts(false);
        $(".contract-status").shouldBe(Condition.visible).shouldHave(Condition.text("APPROVAL"));
        inProgressContractsPage.selectContract("AT-250 Request");
        openedContract = new OpenedContract();

        $(".label").shouldBe(Condition.visible).shouldHave(Condition.exactText("REQUEST"));

        logger.info("Check that both buttons (REJECT REQUEST and APPROVE) are present...");
        $$(".modal-footer button").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("REJECT REQUEST", "APPROVE"));
        new ContractInfo(true).clickApproveRequest().clickApprove();

        $(".notification-stack").should(Condition.appear).shouldHave(Condition.text("Contract Request was successfully approved"));
    }

    @Step
    public void asCNCheckRequest()
    {
        LoginPage loginPage = sideBar.logout();
        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract("AT-250 Request");
        openedContract = new OpenedContract();

        logger.info("Check user icons => Only CN1 and Requester should be present...");
        $$(".contract-header-users__list .user").shouldHave(CollectionCondition.size(2))
                .shouldHave(CollectionCondition.textsInAnyOrder("AL", "RL"));

        logger.info("Check Audit trail events...");
        AuditTrail auditTrail = openedContract.clickAuditTrail();

        List<AuditTrailEvent> allEvents = auditTrail.collectAllEvents();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(allEvents.contains(new AuditTrailEvent("Contract request approved")), "There is no such event as 'Contract request approved' !!!");
        softAssert.assertTrue(allEvents.contains(new AuditTrailEvent("Contract request approved by all members")), "There is no such event as 'Contract request approved by all members' !!!");
        softAssert.assertAll();

        Screenshoter.makeScreenshot();

        auditTrail.clickOk();

        ContractInfo contractInfo = new ContractInfo(true);
        contractInfo.setCounterpartyOrganization("CounterpartyAT");
        contractInfo.setContractingRegion("region1");
        contractInfo.setContractingCountry("country1");
        contractInfo.setContractEntity("entity1");
        contractInfo.setContractingDepartment("department1");
        contractInfo.setContractCategory("category1");
        contractInfo.setContractType("type1");
        contractInfo.clickSave();

        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));
    }

    @AfterMethod
    public void cleanUp() throws InterruptedException
    {
        // remove workflows
        sideBar.clickAdministration().clickWorkflowsTab().clickActionMenu("Approval Workflow AT250").clickDelete().clickDelete();
        Thread.sleep(1_000);
        sideBar.clickAdministration().clickWorkflowsTab().clickActionMenu("Routing Workflow AT250").clickDelete().clickDelete();
        Thread.sleep(1_000);

        $(withText("Approval Workflow AT250")).shouldNot(Condition.visible);
        $(withText("Routing Workflow AT250")).shouldNot(Condition.visible);
    }
}
