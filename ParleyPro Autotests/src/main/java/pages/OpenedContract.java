package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import pages.subelements.FieldsPanel;
import pages.tooltips.ContractActionsMenu;
import pages.tooltips.DocumentActionsMenu;
import pages.tooltips.ParagraphActionsPopup;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

public class OpenedContract
{
    private SelenideElement contractName          = $(".contract-header__name");
    private SelenideElement auditTrailButton      = $("#contract-audit-trail-action");
    private SelenideElement contractInfoButton    = $("#contract-info-action");
    private SelenideElement actionsMenu           = $(".contract-header__menu .actions-menu button");

    private SelenideElement approveDocumentButton  = $("#APPROVE_DOCUMENT");
    private SelenideElement uploadNewVersionButton = $("#UPLOAD_VERSION_DOCUMENT");

    private SelenideElement newDocumentButton     = $("button[tooltip='Add new document']");

    // In case if document was created from template
    private FieldsPanel fieldsPanel;


    private static Logger logger = Logger.getLogger(OpenedContract.class);

    public OpenedContract()
    {
        Assert.assertTrue( isInit() );
    }

    /**
     * Use this constructor in case if opened contract page was scrolled so no headings are not visible
     * @param disableInitialAssert
     */
    public OpenedContract(boolean disableInitialAssert)
    {
        // ...
    }

    private boolean isInit()
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);
        $(".contract-header__status").waitUntil(Condition.visible, 7_000);

        return ( contractName.isDisplayed() );
    }

    public StartReview switchDocumentToReview(String documentName)
    {
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"" + documentName + "\")').parent().parent().parent().next().find('.lifecycle')";

        // hover over DRAFT
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event)");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString + ".find(\"div:contains('REVIEW')\")");

        Selenide.executeJavaScript(documentLifecycleString + ".find(\"div:contains('REVIEW')\").click()");

        logger.info("REVIEW was clicked");

        return new StartReview(contractName.text());
    }

    public StartNegotiation switchDocumentToNegotiate(String documentName, boolean isClassic)
    {
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"" + documentName + "\")').parent().parent().parent().next().find('.lifecycle')";

        // hover over REVIEW
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString+ ".find(\"div:contains('NEGOTIATE')\")");

        Selenide.executeJavaScript(documentLifecycleString + ".find(\"div:contains('NEGOTIATE')\").click()");

        logger.info("NEGOTIATE was clicked");

        return new StartNegotiation(contractName.text(), isClassic);
    }

    public SignContract switchDocumentToSign(String documentName)
    {
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"" + documentName + "\")').parent().parent().parent().next().find('.lifecycle')";

        // hover over DRAFT
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString + ".find(\"div:contains('SIGN')\")");

        Selenide.executeJavaScript(documentLifecycleString + ".find(\"div:contains('SIGN')\").click()");

        logger.info("SIGN was clicked");

        return new SignContract(contractName.text());
    }

    /**
     * Click by pre-negotiate approval button ( green one )
     * @param documentName
     */
    public ConfirmApprovers switchDocumentToPreNegotiateApproval(String documentName)
    {
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"" + documentName + "\")').parent().parent().parent().next().find('.lifecycle')";

        // hover over REVIEW
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString + ".find(\"div:contains('APPROVAL')\")");

        Selenide.executeJavaScript(documentLifecycleString + ".find('.lifecycle__item.review:contains(\"APPROVAL\")').click()");

        logger.info("Pre-Negotiate APPROVAL was clicked");

        return new ConfirmApprovers(documentName);
    }

    /**
     * Click by pre-sign approval button ( purple one )
     * @param documentName
     * @return
     */
    public ConfirmApprovers switchDocumentToPreSignApproval(String documentName)
    {
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"" + documentName + "\")').parent().parent().parent().next().find('.lifecycle')";

        // hover over NEGOTIATE
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString + ".find(\"div:contains('APPROVAL')\")");

        Selenide.executeJavaScript(documentLifecycleString + ".find('.lifecycle__item:contains(\"APPROVAL\")').click()");

        logger.info("Pre-Sign APPROVAL was clicked");

        return new ConfirmApprovers(documentName);
    }

    public ApproveDocument clickApproveButton(String documentName)
    {
        approveDocumentButton.click();

        logger.info("Approve button was clicked");

        return new ApproveDocument(documentName);
    }

    public UploadNewVersionOfDocument clickUploadNewVersionButton(String documentName)
    {
        uploadNewVersionButton.click();

        logger.info("Upload new version button was clicked");

        return new UploadNewVersionOfDocument(documentName);
    }

    /**
     * Click by EDIT DOCUMENT button for the given documentName. Available in Draft stage
     * @param documentName
     * @return
     */
    public EditDocumentPage clickEditDocument(String documentName)
    {
        Selenide.executeJavaScript("$('.document__header-row span:contains(\"" + documentName + "\")').parent().parent().next().click()");

        logger.info("Edit Document was clicked for: " + documentName);

        return new EditDocumentPage(documentName, false);
    }

    /**
     * Click by SHARE button for the given documentName
     * @param documentName
     */
    public ShareForm clickSHARE(String documentName)
    {
        Selenide.executeJavaScript("$('.document__header-row span:contains(\"" + documentName + "\")').parent().parent().parent().next().find(\"button:contains('SHARE')\").click()");

        logger.info("SHARE button was clicked for: " + documentName);

        return new ShareForm(documentName);
    }

    /**
     * Click by COMPLETE SIGN button for the given documentName.
     * @param documentName
     * @return
     */
    public CompleteSign clickCompleteSign(String documentName)
    {
        Selenide.executeJavaScript("$('.document__header-row span[title]:contains(\"" + documentName + "\")').parent().parent().parent().find(\"#COMPLETE_MANUAL_DOCUMENT\").click()");

        logger.info("COMPLETE SIGN was clicked");

        return new CompleteSign(contractName.text());
    }

    /**
     * Perform hover over paragraph that contains certain text
     * @param paragraph - text that contains in paragraph
     * @return
     */
    public ParagraphActionsPopup hover(String paragraph)
    {
        StringBuffer jsCode = new StringBuffer("var deleteMeP = $('.document-paragraph__content-text:contains(\"" + paragraph + "\")');");
        jsCode.append("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append("deleteMeP[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        return new ParagraphActionsPopup();
    }

    /**
     * Click by discussion icon of the given paragraph
     * @param paragraph - text that contains in paragraph
     * @return
     */
    public OpenedDiscussion clickByDiscussionIcon(String paragraph)
    {
        Selenide.executeJavaScript("$('.document-paragraph__content-text:contains(\"" + paragraph + "\")').prev().click()");

        return new OpenedDiscussion(paragraph);
    }

    public AuditTrail clickAuditTrail()
    {
        auditTrailButton.click();

        logger.info("Audit trail button was clicked");

        return new AuditTrail();
    }

    public ContractInformation clickContractInfo()
    {
        contractInfoButton.click();

        logger.info("Contract info button was clicked");

        return new ContractInformation(true);
    }

    /**
     * Click by button with 3 dots for opened contract
     * @return
     */
    public ContractActionsMenu clickContractActionsMenu()
    {
        actionsMenu.click();

        return new ContractActionsMenu(contractName.text());
    }

    /**
     * Click by button with 3 dots for chosen document
     * @param documentName - name of document for which document actions menu should be invoked
     * @return
     */
    public DocumentActionsMenu clickDocumentActionsMenu(String documentName)
    {
        Selenide.executeJavaScript("$('.document__header-row span:contains(\"" + documentName + "\")').parent().parent().parent().find(\"div[class='document__menu']\").find(\"button\").click()");

        return new DocumentActionsMenu(documentName);
    }

    /**
     * Click by + NEW DOCUMENT button
     * @return
     */
    public AddDocuments clickNewDocument()
    {
        newDocumentButton.click();

        logger.info("+ NEW DOCUMENT button was clicked");

        return new AddDocuments();
    }

    /**
     * Get Fields panel. Invoke only if you are sure that panel is active.
     * @return
     */
    public FieldsPanel getFieldsPanel()
    {
        if( fieldsPanel == null )
        {
            fieldsPanel = new FieldsPanel();
        }

        return fieldsPanel;
    }
}
