package tests.approval_workflow;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import forms.ConfirmApprovers;
import org.apache.log4j.Logger;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.OpenedContract;

import static com.codeborne.selenide.Selenide.$$;

public class StartPreNegotiateApproval
{
    private static Logger logger = Logger.getLogger(StartPreNegotiateApproval.class);

    @Test
    public void startPreNegotiateApproval() throws InterruptedException
    {
        OpenedContract openedContract = new OpenedContract();

        ConfirmApprovers confirmApproversForm = openedContract.switchDocumentToPreNegotiateApproval("pramata");

        logger.info("Assert that we see one team: Autotest_TEAM_3 and one user: Approval_User_1");
        String listOfApprovers = confirmApproversForm.getListOfApprovers();
        Assert.assertTrue(listOfApprovers.contains("Autotest_TEAM_3 [EDITED]") && listOfApprovers.contains("Approval_User_1"));

        confirmApproversForm.switchTumblerSetApprovalOrder();
        confirmApproversForm.addParticipant("Approval_User_2");
        confirmApproversForm.deleteApprover("TEAM");
    }
}
