package tests.approval_workflow.at245;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import constants.Const;
import forms.ContractInformation;
import forms.ResendInviteForm;
import forms.SendMessage;
import forms.workflows.ApprovalWorkflow;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.LoginPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.EmailChecker;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckButtonsForTeamInApproval
{
    private static final String CONTRACT_NAME = "AT-245 - ResendInvite_and_SendMessage";

    private SideBar sideBar;
    private OpenedContract openedContract;

    private static Logger logger = Logger.getLogger(CheckButtonsForTeamInApproval.class);


    @BeforeMethod
    public void addOneMoreTeamToWorkflowAndCreateContract()
    {
        sideBar = new DashboardPage().getSideBar();

        sideBar.clickAdministration().clickWorkflowsTab().clickActionMenu("Approval_WFL_AT").clickEdit();

        logger.info("Add one more team to approval workflow...");
        ApprovalWorkflow editApprovalWorkflowForm = new ApprovalWorkflow(true);
        editApprovalWorkflowForm.setPriorToNegotiateParticipant("Team #2");
        editApprovalWorkflowForm.clickSave();

        logger.info("Creating contract...");
        ContractInformation contractInformationForm = sideBar.clickInProgressContracts(true).clickNewContractButton();

        contractInformationForm.setContractTitle(CONTRACT_NAME);
        contractInformationForm.setContractCurrency("GBP");
        contractInformationForm.setContractValue("1000");
        contractInformationForm.setContractingRegion("region1");
        contractInformationForm.setContractingCountry("country1");
        contractInformationForm.setContractEntity("entity1");
        contractInformationForm.setContractingDepartment("department2");
        contractInformationForm.setContractCategory("category2");
        contractInformationForm.setContractType("type2");
        contractInformationForm.clickSave();

        new AddDocuments().clickUploadMyTeamDocuments(Const.DOCUMENT_DISCUSSIONS_SAMPLE);
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("DRAFT\n(1)", "DRAFT"));

        openedContract = new OpenedContract();
        openedContract.switchDocumentToPreNegotiateApproval("AT-14").clickNext().clickStartApproval();
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("APPROVAL\n(1)", "APPROVAL"));

        logger.info("Check that 2 teams were added as approvers...");
        $$(".team-icon.team").shouldHave(CollectionCondition.size(2));
    }

    @Test
    public void checkButtonsForTeamInApproval() throws InterruptedException
    {
        checkSendReminderPresenceAndClick();
        checkContractApprovalRequestEmail();
        loginAsTeamMemberAndVisitContract();
        loginAsMyTeamCNAndCheckMessageButtonForTeam3();
        checkContractNewMessageEmail();
    }

    @Step("Hover team icons in document header to find team by the name 'Team #2', check that it has SEND A REMINDER button, click it.")
    public void checkSendReminderPresenceAndClick() throws InterruptedException
    {
        logger.info("Hover 'Team #2'...");

        ElementsCollection teamIcons = $$(".header-users > .team-icon.team").shouldHave(CollectionCondition.size(2));
        for( int i = 0; i < teamIcons.size(); i++ )
        {
            StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true}); ");
            jsCode.append("$('.header-users > .team-icon.team').eq(" + i + ")[0].dispatchEvent(event);");
            Selenide.executeJavaScript(jsCode.toString());

            Thread.sleep(2_000);
            if( $(".users-tooltip__team-name").getText().contains("Team #2") )
            {
                $(".rc-tooltip-content button").shouldBe(Condition.visible, Condition.enabled).shouldHave(Condition.exactText("SEND A REMINDER")).click();
                new ResendInviteForm(null).clickResend(); // there is no parentForm => pass null
                $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Reminder email has been successfully sent"));
                Screenshoter.makeScreenshot();
                break;
            }
            else
            {
                Thread.sleep(1_000);
                Selenide.executeJavaScript("$('.rc-tooltip-inner').hide()");
                Thread.sleep(1_000);
                continue;
            }
        }
    }

    @Step("Check that email with subject: 'Contract approval request' is present.")
    public void checkContractApprovalRequestEmail() throws InterruptedException
    {
        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Contract \"" + CONTRACT_NAME + "\" approval request"),
                "Email with subject: " + "Contract \"" + CONTRACT_NAME + "\" approval request" + " was not found !!!");
    }

    @Step("Login as User Felix and visit contract.")
    public void loginAsTeamMemberAndVisitContract()
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail(Const.USER_FELIX.getEmail());
        loginPage.setPassword(Const.USER_FELIX.getPassword());

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract(CONTRACT_NAME);
        openedContract = new OpenedContract();
    }

    @Step("Check that for team Autotest_TEAM_3 [EDITED] button MESSAGE become visible. Click it.")
    public void loginAsMyTeamCNAndCheckMessageButtonForTeam3() throws InterruptedException
    {
        LoginPage loginPage = sideBar.logout();

        loginPage.setEmail(Const.PREDEFINED_USER_CN_ROLE.getEmail());
        loginPage.setPassword(Const.PREDEFINED_USER_CN_ROLE.getPassword());

        sideBar = loginPage.clickSignIn().getSideBar();

        sideBar.clickInProgressContracts(false).selectContract(CONTRACT_NAME);
        openedContract = new OpenedContract();

        logger.info("Hover 'Autotest_TEAM_3 [EDITED]' team...");

        ElementsCollection teamIcons = $$(".header-users > .team-icon.team").shouldHave(CollectionCondition.size(2));
        for( int i = 0; i < teamIcons.size(); i++ )
        {
            StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true}); ");
            jsCode.append("$('.header-users > .team-icon.team').eq(" + i + ")[0].dispatchEvent(event);");
            Selenide.executeJavaScript(jsCode.toString());

            Thread.sleep(2_000);
            if( $(".users-tooltip__team-name").getText().contains("Autotest_TEAM_3") )
            {
                $(".rc-tooltip-content button").shouldBe(Condition.visible, Condition.enabled).shouldHave(Condition.exactText("MESSAGE")).click();
                Selenide.executeJavaScript("$('.rc-tooltip-inner').hide()"); // hide tooltip because it overlaps MESSAGE button
                Thread.sleep(2_000);
                new SendMessage("Autotest_TEAM_3 [EDITED]").setMessage("Notify all users from team 3...").clickSend();
                Thread.sleep(2_000);
                $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text("Message has been successfully sent"));
                Screenshoter.makeScreenshot();
                break;
            }
            else
            {
                Thread.sleep(1_000);
                Selenide.executeJavaScript("$('.rc-tooltip-inner').hide()");
                Thread.sleep(1_000);
                continue;
            }
        }
    }

    @Step("Check that email with subject: 'Contract new message' is present.")
    public void checkContractNewMessageEmail() throws InterruptedException
    {
        logger.info("Waiting for 60 seconds to make sure that email has been delivered...");
        Thread.sleep(60_000);
        Assert.assertTrue(EmailChecker.assertEmailBySubject(Const.HOST_GMAIL, Const.USERNAME_GMAIL, Const.PASSWORD_GMAIL, "Contract \"" + CONTRACT_NAME + "\": new message"),
                "Email with subject: " + "Contract \"" + CONTRACT_NAME + "\": new message" + " was not found !!!");
        EmailChecker.assertEmailBodyText("Notify all users from team 3"); // assert presence of message in this email too
    }

    @AfterMethod
    public void deleteContract()
    {
        sideBar.clickInProgressContracts(false).selectContract(CONTRACT_NAME);
        openedContract = new OpenedContract();
        openedContract.clickContractActionsMenu().clickDeleteContract().clickDelete();
        $(".notification-stack").shouldBe(Condition.visible).shouldHave(Condition.text(" has been deleted."));
    }
}
