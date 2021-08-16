package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class ConfirmApprovers
{
    private SelenideElement title = $(".modal-body-title");
    private SelenideElement addParticipantField = $("#document-approval_select");
    private SelenideElement startApprovalButton = $("button[type='submit']");
    private SelenideElement cancelButton        = $(".btn.btn-common.btn-link.btn-link-pseudo");


    private static Logger logger = Logger.getLogger(ConfirmApprovers.class);

    public ConfirmApprovers(String documentName)
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Confirm approvers for document \"" + documentName + "\""));
        $(".modal-content .spinner").waitUntil(Condition.disappear, 20_000);

        try { Thread.sleep(1_000); } catch (InterruptedException e) { logger.error(e); }

        // In case if there is no list of approvers => User sees [You need to assign at least 1 approver] message => Button START APPROVAL is disabled
        // This uses in scenario AT-162
        if( $(".document-approval__selected-users.is_error").is(Condition.visible) )
        {
            logger.info("There is no list of approvers...");
            logger.info("[You need to assign at least 1 approver] message is visible...");
            return;
        }
        else
        {
            // ...otherwise wait until list of approval users is visible
            $(".document-approval__users").waitUntil(Condition.visible, 7_000);
        }
    }

    public String getListOfApprovers()
    {
        return $(".document-approval__users").waitUntil(Condition.visible, 7_000).getText();
    }

    /**
     * Delete approver by name
     * @param approverName
     */
    public void deleteApprover(String approverName)
    {
        Selenide.executeJavaScript("$('.document-approval__user-info:contains(\"" + approverName + "\")').next().find(\".document-approval__user-remove\").click()");

        logger.info("The following approver was deleted: " + approverName);
    }

    public void switchTumblerSetApprovalOrder()
    {
        Selenide.executeJavaScript("$('.document-approval__set-order .tumbler').click()");

        logger.info("Set approval order tumbler was switched");
    }

    public ConfirmApprovers addParticipant(String participantName)
    {
        addParticipantField.setValue(participantName);
        addParticipantField.sendKeys(Keys.DOWN);
        addParticipantField.sendKeys(Keys.ENTER);

        // press Tab key to switch focus and close dropdown
        addParticipantField.sendKeys(Keys.TAB);

        return this;
    }

    public void clickStartApproval()
    {
        startApprovalButton.click();

        logger.info("Start Approval button was clicked");
    }

    public void clickCancel()
    {
        cancelButton.click();

        logger.info("CANCEL was clicked");
    }
}
