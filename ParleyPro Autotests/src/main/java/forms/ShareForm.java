package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import pages.tooltips.RoleSelectorTooltip;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents form that appears after clicking on SHARE button
 */
public class ShareForm
{
    private SelenideElement addParticipantField = $("#manage-users__select");
    private SelenideElement addNoteCheckbox     = $(".active .checkbox__image");
    private SelenideElement messageTextArea     = $(".active textarea");

    private SelenideElement sendButton = $("._button.scheme_blue.size_lg.manage-users-new__submit");
    private SelenideElement doneButton = $(".manage-users-added__foot button");


    private static Logger logger = Logger.getLogger(ShareForm.class);

    public ShareForm(String documentName)
    {
        $(".spinner").waitUntil(Condition.disappear, 7_000);

        $(".modal-title").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Add internal users and assign roles for internal review of document “" + documentName + "”."));

        $(".manage-users-user").waitUntil(Condition.visible, 7_000);
    }

    public ShareForm addParticipant(String nameOrEmail) throws InterruptedException
    {
        addParticipantField.setValue(nameOrEmail);

        Thread.sleep(1_000); // some sleep to make sure that values were populated in dropdown

        addParticipantField.sendKeys(Keys.ARROW_DOWN);
        addParticipantField.pressEnter();

        Thread.sleep(1_000); // some sleep during slide animation

        return this; // we are on the same form
    }

    public RoleSelectorTooltip changeRoleOfInternalUser(String username)
    {
        Selenide.executeJavaScript("$('.active .manage-users-user__fullname:contains(\"" + username + "\")').parent().parent().next().find(\"button\").click()");

        return new RoleSelectorTooltip(username);
    }

    public ShareForm disableUser(String username)
    {
        Selenide.executeJavaScript("$('.active .manage-users-user__fullname:contains(\"" + username + "\")').parent().parent().next().next().find(\".tumbler\").click()");

        logger.info("User " + username + " was disabled...");

        return this;
    }

    /**
     * Click Resend invite button for given user
     * @param username
     */
    public ResendInviteForm resendInvite(String username)
    {
        Selenide.executeJavaScript("$('.manage-users-user__fullname:contains(\"" + username + "\")').parent().parent().find(\"svg\").parent().click()");

        logger.info("Resend invite button was clicked for user: " + username);

        return new ResendInviteForm(this);
    }

    public void clickAddNoteCheckbox()
    {
        addNoteCheckbox.click();

        logger.info("Add a note checkbox was clicked...");
    }

    public void setMessage(String message)
    {
        messageTextArea.sendKeys(message);
    }

    public void clickSend()
    {
        sendButton.click();

        logger.info("SEND button was clicked...");
    }

    public void clickDone()
    {
        doneButton.click();

        logger.info("DONE button was clicked...");
    }
}
