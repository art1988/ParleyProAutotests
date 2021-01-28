package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

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
        $(".modal-body-title").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Upload document"));
        $(".modal-body-description").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("We detected changes in the document. Parley Pro will create discussions based on these changes."));
    }

    public void setCounterpartyOrganization(String cpOrganization)
    {
        cpOrganizationField.clear();
        cpOrganizationField.sendKeys(cpOrganization);
        cpOrganizationField.pressEnter();
    }

    public void setCounterpartyNegotiatorEmail(String cpNegotiatorEmail)
    {
        cpNegotiatorEmailField.clear();
        cpNegotiatorEmailField.sendKeys(cpNegotiatorEmail);
        cpNegotiatorEmailField.pressEnter();
    }

    public void clickOk()
    {
        okButton.click();

        logger.info("OK button was clicked");
    }
}
