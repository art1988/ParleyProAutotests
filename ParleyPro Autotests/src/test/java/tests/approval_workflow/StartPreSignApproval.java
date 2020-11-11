package tests.approval_workflow;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import forms.ConfirmApprovers;
import forms.EmailWillBeSentToTheCounterparty;
import forms.StartNegotiation;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.OpenedContract;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class StartPreSignApproval
{
    private static Logger logger = Logger.getLogger(StartPreNegotiateApproval.class);

    @Test(priority = 1)
    @Description("This test moves document to negotiate stage")
    public void moveToNegotiate() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        StartNegotiation startNegotiationForm = openedContract.switchDocumentToNegotiate("pramata");
        startNegotiationForm.clickNext();

        EmailWillBeSentToTheCounterparty emailWillBeSentToTheCounterpartyForm = startNegotiationForm.clickNext();
        emailWillBeSentToTheCounterpartyForm.setCounterpartyOrganization("CounterpartyAT");
        Thread.sleep(1_000);
        emailWillBeSentToTheCounterpartyForm.setCounterpartyChiefNegotiator("arthur.khasanov+cpat@parleypro.com");
        emailWillBeSentToTheCounterpartyForm.clickStart();

        logger.info("Assert visible to the counterparty notification...");
        $(".notification-stack").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Contract Approval workflow positive is now visible to the Counterparty. The email was sent to arthur.khasanov+cpat@parleypro.com"));

        logger.info("Assert that status was changed to NEGOTIATE...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("NEGOTIATE\n(1)", "NEGOTIATE"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test starts pre-sign approval. Verifies that approvers list consist of Approval_User_2 and Team #2")
    public void startPreSignApproval()
    {
        OpenedContract openedContract = new OpenedContract();

        ConfirmApprovers confirmApproversForm = openedContract.switchDocumentToPreSignApproval("pramata");

        logger.info("Assert that we see Approval_User_2 as first approver and Team #2 as second approver...");
        $$(".document-approval__user").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("A\nApproval_User_2 (arthur.khasanov+approval2@parleypro.com)", "Team #2\n2 members"));

        confirmApproversForm.clickStartApproval();

        logger.info("Assert that approvers icons are visible: Approval_User_2 and Team#2");
        $(".header-users .user").waitUntil(Condition.appear, 15_000); // wait until users icons will appear
        $$(".header-users .user").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("A")); // one User...
        $$(".header-users .team").shouldHave(CollectionCondition.size(1)); // ...and one Team
    }
}
