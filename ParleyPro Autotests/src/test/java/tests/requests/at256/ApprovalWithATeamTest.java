package tests.requests.at256;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.ContractRequest;
import io.qameta.allure.Step;
import model.AuditTrailEvent;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AuditTrail;
import pages.ContractInfo;
import pages.LoginPage;
import pages.OpenedContract;
import pages.administration.Fields;
import pages.administration.Teams;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.util.List;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

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
        loginAsTeamMember1();
        loginAsTeamMember3();
        asMyTeamCNConvertRequestIntoContract();
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

    @Step("Team member 1 is +team1@parleypro.com")
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
        $$(".team-icon").shouldHave(CollectionCondition.size(2));

        logger.info("Approve by TeamMember1...");
        new ContractInfo(true).clickApproveRequest().clickApprove();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract Request was successfully approved"));
    }

    @Step("Team member 3 is GREG")
    public void loginAsTeamMember3()
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail( Const.USER_GREG.getEmail() );
        loginPage.setPassword( Const.USER_GREG.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();

        logger.info("Open request as team member 3(User Greg)...");
        sideBar.clickInProgressContracts(false).selectContract(REQUEST_NAME);

        $$(".team-icon").shouldHave(CollectionCondition.size(2));
        $$(".team-icon._show-checkbox_yes").shouldHave(CollectionCondition.size(1)); // only one team with checkmark
        $$(".team-icon._show-checkbox_yes").first().hover();
        $(".users-tooltip__team-name").shouldBe(Condition.visible).shouldHave(Condition.exactText("T1")); // it should be T1 team

        logger.info("Approve by TeamMember3(Greg)...");
        new ContractInfo(true).clickApproveRequest().clickApprove();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Contract Request was successfully approved"));
    }

    @Step
    public void asMyTeamCNConvertRequestIntoContract()
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail( Const.PREDEFINED_USER_CN_ROLE.getEmail() );
        loginPage.setPassword( Const.PREDEFINED_USER_CN_ROLE.getPassword() );

        sideBar = loginPage.clickSignIn().getSideBar();
        sideBar.clickInProgressContracts(false).selectContract(REQUEST_NAME);

        logger.info("Check that only CN and Requester avatars are visible...");
        $$(".contract-header-users__list .user").shouldHave(CollectionCondition.size(2))
                .shouldHave(CollectionCondition.textsInAnyOrder("AL", "RL"));

        AuditTrail auditTrail = new OpenedContract().clickAuditTrail();
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

        logger.info("Check that status become DRAFT...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));
    }

    @AfterMethod
    public void cleanUp() throws InterruptedException
    {
        ///
        logger.info("remove workflows...");
        sideBar.clickAdministration().clickWorkflowsTab().clickActionMenu("Approval Workflow AT256").clickDelete().clickDelete();
        Thread.sleep(1_000);
        sideBar.clickAdministration().clickWorkflowsTab().clickActionMenu("Routing Workflow AT256").clickDelete().clickDelete();
        Thread.sleep(1_000);

        $(withText("Approval Workflow AT256")).shouldNotBe(Condition.visible);
        $(withText("Routing Workflow AT256")).shouldNotBe(Condition.visible);

        ///
        logger.info("remove field...");
        Fields fieldsTab = sideBar.clickAdministration().clickFieldsTab();
        fieldsTab.clickContractFields().removeField("AT256-RequestF").clickDelete();
        fieldsTab.clickSave();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.exactText("Contract fields have been saved."));

        ///
        logger.info("remove teams...");
        Teams teams = sideBar.clickAdministration().clickTeamsTab();
        teams.clickActionMenu("T1").clickDelete().clickDelete();
        Thread.sleep(1_000);
        teams.clickActionMenu("T2").clickDelete().clickDelete();
        Thread.sleep(1_000);

        $(withText("T1")).shouldNotBe(Condition.visible);
        $(withText("T2")).shouldNotBe(Condition.visible);
    }
}
