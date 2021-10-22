package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Attachment;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represent form that appears after clicking of 'Upload my team documents' button and document was with revisions
 */
public class UploadDocumentDetectedChanges
{
    private SelenideElement cpOrganizationField    = $("#cp-organization");
    private SelenideElement cpNegotiatorEmailField = $("#cp-email");
    private SelenideElement okButton = $(".button.btn-common.btn.btn-primary");

    private static Logger logger = Logger.getLogger(DocumentFormattingOption.class);

    public UploadDocumentDetectedChanges()
    {
        $(".select__loading").waitUntil(Condition.disappear, 7_000);
        try
        {
            $(".modal-body-title").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Upload document"));
            $(".modal-body-description").waitUntil(Condition.visible, 7_000)
                    .shouldHave(Condition.exactText("We detected changes in the document. Parley Pro will create discussions based on these changes."));
        }
        catch (ElementNotFound e) // in case if pop up was never shown
        {
            String errMessage = "!!! Looks like that popup 'We detected changes in the document. Parley Pro will create discussions based on these changes.' wasn't shown !!!";

            logger.error(errMessage);

            addLogMessageToAllureReport(errMessage);
        }
    }

    @Attachment
    private byte[] addLogMessageToAllureReport(String message)
    {
        return message.getBytes();
    }

    /**
     * Set Counterparty organization
     * @param cpOrganization
     * @throws InterruptedException
     */
    public void setCounterpartyOrganization(String cpOrganization) throws InterruptedException
    {
        cpOrganizationField.clear();
        Thread.sleep(500);
        cpOrganizationField.sendKeys(cpOrganization);
        Thread.sleep(500);
        cpOrganizationField.sendKeys(Keys.DOWN);
        Thread.sleep(500);
        cpOrganizationField.pressEnter();
        Thread.sleep(500);
    }

    /**
     * Set Counterparty Chief Negotiator
     * @param cpNegotiatorEmail
     * @throws InterruptedException
     */
    public void setCounterpartyNegotiatorEmail(String cpNegotiatorEmail) throws InterruptedException
    {
        cpNegotiatorEmailField.clear();
        Thread.sleep(500);
        cpNegotiatorEmailField.sendKeys(cpNegotiatorEmail);
        Thread.sleep(500);
        cpNegotiatorEmailField.sendKeys(Keys.DOWN);
        Thread.sleep(500);
        cpNegotiatorEmailField.pressEnter();
        Thread.sleep(500);
    }

    public void clickOk()
    {
        okButton.click();

        logger.info("OK button was clicked");
    }
}
