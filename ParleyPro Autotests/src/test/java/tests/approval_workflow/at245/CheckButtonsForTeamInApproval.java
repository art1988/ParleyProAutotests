package tests.approval_workflow.at245;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import constants.Const;
import forms.ContractInformation;
import forms.workflows.ApprovalWorkflow;
import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.subelements.SideBar;
import utils.ScreenShotOnFailListener;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class CheckButtonsForTeamInApproval
{
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

        contractInformationForm.setContractTitle("AT-245 - ResendInvite_and_SendMessage");
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
    }

    @Step
    public void checkSendReminderPresenceAndClick() throws InterruptedException
    {
        logger.info("Hover Team 2...");

        $(".header-users").shouldBe(Condition.visible).hover();

        ElementsCollection userIcons = $$(".header-users span").shouldHave(CollectionCondition.size(5));

        for( int i = 0; i < userIcons.size(); i++ )
        {
            userIcons.get(i).shouldBe(Condition.visible, Condition.enabled).hover();

            if( $(".users-tooltip__team-name").getText().contains("Team #2") )
            {
                $(".rc-tooltip-content button").shouldBe(Condition.visible, Condition.enabled).shouldHave(Condition.exactText("SEND A REMINDER")).click();
            }
        }
    }
}
