package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.*;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import pages.subelements.CKEditorActive;
import pages.subelements.FieldsPanel;
import pages.subelements.FindAndReplacePopup;
import pages.tooltips.ContractActionsMenu;
import pages.tooltips.DocumentActionsMenu;
import pages.tooltips.ParagraphActionsPopup;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

public class OpenedContract
{
    private SelenideElement contractName          = $(".contract-header__name");
    private SelenideElement manageDiscussions     = $(".contract-header__manage-discussions");

    private SelenideElement emailContractToCP     = $("#contract-email-action");
    private SelenideElement auditTrailButton      = $("#contract-audit-trail-action");
    private SelenideElement contractInfoButton    = $("#contract-info-action");
    private SelenideElement actionsMenu           = $(".contract-header__menu .actions-menu button");

    private SelenideElement sendInviteButton      = $("._button.scheme_blue.size_tier2");

    private SelenideElement approveDocumentButton  = $("#APPROVE_DOCUMENT");
    private SelenideElement rejectDocumentButton   = $("#REJECT_DOCUMENT");
    private SelenideElement uploadNewVersionButton = $("#UPLOAD_VERSION_DOCUMENT");

    private SelenideElement newDocumentButton       = $("button[tooltip='Add new document']");
    private SelenideElement readyForSignatureButton = $(".ready_to_sign");

    // Blue button that appears only for PDF's
    private SelenideElement startDiscussionButton   = $(".document__pdf-discussion-button");

    // In case if document was created from template
    private FieldsPanel fieldsPanel;


    private static Logger logger = Logger.getLogger(OpenedContract.class);

    public OpenedContract()
    {
        Assert.assertTrue( isInit() );
    }

    /**
     * Use this constructor in case if opened contract page was scrolled so that headings are not visible
     * @param disableInitialAssert
     */
    public OpenedContract(boolean disableInitialAssert)
    {
        // ...
    }

    private boolean isInit()
    {
        $(".spinner").waitUntil(Condition.disappear, 60_000 * 2);
        $(".contract-header__status").waitUntil(Condition.visible, 7_000);
        $(".document__body .spinner").waitUntil(Condition.disappear, 30_000);

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

        logger.info("REVIEW was clicked for " + documentName);

        return new StartReview(contractName.text());
    }

    public StartNegotiation switchDocumentToNegotiate(String documentName, String counterparty, boolean isClassic)
    {
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"" + documentName + "\")').parent().parent().parent().next().find('.lifecycle')";

        // hover over REVIEW
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString+ ".find(\"div:contains('NEGOTIATE')\")");

        Selenide.executeJavaScript(documentLifecycleString + ".find(\"div:contains('NEGOTIATE')\").click()");

        logger.info("NEGOTIATE was clicked for " + documentName);

        return new StartNegotiation(contractName.text(), counterparty, isClassic);
    }

    /**
     * In case if DISABLE_COUNTERPARTY_DOC_SHARE_ON_TEAM_UPLOAD tenant property is enabled, then 'Start Negotiation' form
     * will _NOT_ appear and document will change it's status to Negotiate.
     * Use this method if that property is enabled.
     */
    public void switchDocumentToNegotiate(String documentName)
    {
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"" + documentName + "\")').parent().parent().parent().next().find('.lifecycle')";

        // hover over REVIEW
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event);");
        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString+ ".find(\"div:contains('NEGOTIATE')\")");
        Selenide.executeJavaScript(documentLifecycleString + ".find(\"div:contains('NEGOTIATE')\").click()");
        logger.info("NEGOTIATE was clicked for " + documentName);
    }

    public SignContract switchDocumentToSign(String documentName, boolean integrationEnabled)
    {
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"" + documentName + "\")').parent().parent().parent().next().find('.lifecycle')";

        // hover over DRAFT
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString + ".find(\"div:contains('SIGN')\")");

        Selenide.executeJavaScript(documentLifecycleString + ".find(\"div:contains('SIGN')\").click()");

        logger.info("SIGN was clicked for " + documentName);

        return new SignContract(contractName.text(), integrationEnabled);
    }

    /**
     * Click by pre-negotiate approval button ( green one )
     * @param documentName
     */
    public AboutToStartApproval switchDocumentToPreNegotiateApproval(String documentName)
    {
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"" + documentName + "\")').parent().parent().parent().next().find('.lifecycle')";

        // hover over REVIEW
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString + ".find(\"div:contains('APPROVAL')\")");

        Selenide.executeJavaScript(documentLifecycleString + ".find('.lifecycle__item.review:contains(\"APPROVAL\")').click()");

        logger.info("Pre-Negotiate APPROVAL was clicked for " + documentName);

        return new AboutToStartApproval(contractName.getText());
    }

    /**
     * Click by pre-negotiate approval button ( green one ) for document (doc/docx/pdf) that _HAS_AT_LEAST_ONE_OPENED_DISCUSSION_
     * I.e. you need to be sure that document has discussion.
     * @param documentName
     */
    public ContractHasOpenDiscussions switchDocumentWithOpenDiscussionToPreNegotiateApproval(String documentName)
    {
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"" + documentName + "\")').parent().parent().parent().next().find('.lifecycle')";

        // hover over REVIEW
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString + ".find(\"div:contains('APPROVAL')\")");

        Selenide.executeJavaScript(documentLifecycleString + ".find('.lifecycle__item.review:contains(\"APPROVAL\")').click()");

        logger.info("Pre-Negotiate APPROVAL was clicked for " + documentName);

        return new ContractHasOpenDiscussions();
    }

    /**
     * Click by pre-sign approval button ( purple one )
     * @param documentName
     * @return
     */
    public AboutToStartApproval switchDocumentToPreSignApproval(String documentName)
    {
        String documentLifecycleString = "$('.document__header-row span[title]:contains(\"" + documentName + "\")').parent().parent().parent().next().find('.lifecycle')";

        // hover over NEGOTIATE
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append(documentLifecycleString + "[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible(documentLifecycleString + ".find(\"div:contains('APPROVAL')\")");
        try { Thread.sleep(1_000); } catch (InterruptedException e) { logger.error("InterruptedException", e); } // necessary sleep too !

        Selenide.executeJavaScript(documentLifecycleString + ".find('.lifecycle__item:contains(\"APPROVAL\")').click()");

        logger.info("Pre-Sign APPROVAL was clicked for " + documentName);

        return new AboutToStartApproval(contractName.getText());
    }

    /**
     * Click by pre-negotiate approval button ( green one ) in contract header.
     * No need to pass arguments because contract is the only one on page and is opened.
     * @return
     */
    public AboutToStartApproval switchContractToPreNegotiateApproval()
    {
        $(".contract-header__status .lifecycle").shouldBe(Condition.visible);

        Selenide.executeJavaScript("$('.contract-header__status .lifecycle div:contains(\"APPROVAL\")').eq(0).click()"); // eq(0) - first APPROVAL button (pre-negotiate)

        return new AboutToStartApproval(contractName.getText());
    }

    public ApproveDocument clickApproveButton(String documentName)
    {
        approveDocumentButton.click();

        logger.info("Approve button was clicked");

        return new ApproveDocument(documentName);
    }

    public RejectDocument clickRejectButton(String documentName)
    {
        rejectDocumentButton.click();

        logger.info("Reject button was clicked");

        return new RejectDocument(documentName);
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
     * Click by magnifying glass icon 'Find And Replace' for the given documentName.
     * This option available in Review and Negotiate stages.
     * @param documentName
     */
    public FindAndReplacePopup clickFindAndReplaceButton(String documentName)
    {
        Selenide.executeJavaScript("$('.document__header-row span:contains(\"" + documentName + "\")').parent().parent().next().find(\".js-document-search-btn\").click()");

        logger.info("Find and Replace button was clicked for: " + documentName);

        return new FindAndReplacePopup();
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
     * @param paragraph text that contains in paragraph
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
     * Perform click by paragraph that contains certain text. After click CKEditor should appear.
     * @param paragraph text that contains in paragraph
     * @return
     */
    public CKEditorActive clickByParagraph(String paragraph)
    {
        $((WebElement) Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"" + paragraph + "\")')[0]")).click();

        return new CKEditorActive();
    }

    /**
     * Click by discussion icon of the given paragraph. Also validates header of opened discussion by contains.
     * @param paragraph text that contains in paragraph
     * @return
     */
    public OpenedDiscussion clickByDiscussionIcon(String paragraph)
    {
        WebElement discussionIcon = Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"" + paragraph + "\")').prev()[0]");
        $(discussionIcon).click();

        return new OpenedDiscussion(paragraph);
    }

    /**
     * Click by discussion icon of the given paragraph. DOESN'T validate discussion header.
     * @param paragraph
     * @return
     */
    public OpenedDiscussion clickByDiscussionIconSoft(String paragraph)
    {
        WebElement discussionIcon = Selenide.executeJavaScript("return $('.document-paragraph__content-text:contains(\"" + paragraph + "\")').prev()[0]");
        $(discussionIcon).click();

        return new OpenedDiscussion(true);
    }

    public ManageDiscussions clickManageDiscussions()
    {
        manageDiscussions.click();

        logger.info("Manage discussions button was clicked");

        return new ManageDiscussions();
    }

    public AboutToEmailContract clickEmailContractToCounterparty(String cpOrganization)
    {
        emailContractToCP.click();

        logger.info("Email contract to Counterparty button was clicked");

        return new AboutToEmailContract(contractName.getText(), cpOrganization);
    }

    public AuditTrail clickAuditTrail()
    {
        auditTrailButton.click();

        logger.info("Audit trail button was clicked");

        return new AuditTrail();
    }

    public ContractInformation clickContractInfo()
    {
        contractInfoButton.shouldBe(Condition.enabled).click();

        logger.info("Contract info button was clicked");

        return new ContractInformation(true);
    }

    /**
     * Click by round blue button 'Start discussion' that available only for uploaded PDF's
     */
    public OpenedDiscussionPDF clickStartDiscussionForPDF()
    {
        startDiscussionButton.shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("Blue round button 'Start discussion' was clicked...");

        return new OpenedDiscussionPDF();
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
     * @param documentName name of document for which document actions menu should be invoked
     * @return
     */
    public DocumentActionsMenu clickDocumentActionsMenu(String documentName)
    {
        Selenide.executeJavaScript("$('.document__header-row span:contains(\"" + documentName + "\")').parent().parent().parent().find(\"div[class='document__menu']\").find(\"button\").click()");

        return new DocumentActionsMenu(documentName);
    }

    public SendInvitation clickSendInvite()
    {
        sendInviteButton.click();

        logger.info("SEND INVITE button was clicked");

        return new SendInvitation(contractName.text());
    }

    /**
     * Click by + NEW DOCUMENT button
     * @return
     */
    public AddDocuments clickNewDocument()
    {
        newDocumentButton.shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("+ NEW DOCUMENT button was clicked");

        return new AddDocuments();
    }

    /**
     * Click by READY FOR SIGNATURE button. CCN may have this button.
     */
    public ReadyToSign clickReadyForSignature()
    {
        readyForSignatureButton.click();

        logger.info("READY FOR SIGNATURE button was clicked");

        return new ReadyToSign();
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

    /**
     * Gets contract title from contract header
     * @return
     */
    public String getContractName()
    {
        return contractName.getText();
    }

    /**
     * Click by 'Rename contract' button, set new name of contract and click blue button to accept.
     */
    public void renameContract(String newNameOfContract)
    {
        contractName.hover(); // hover over contract title to activate 'Rename contract' button

        try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

        $("button[class*='rename__icon']").click();


        SelenideElement contractNameTextArea = $("form[class='rename-form'] textarea");

        // delete previous text
        contractNameTextArea.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        // (Ctrl+A+DEL) adds strange square symbol □ Since Chrome v.98 => del this symbol too
        for( int i = 0; i < 5; i++ ) contractNameTextArea.sendKeys(Keys.BACK_SPACE);
        contractNameTextArea.sendKeys(newNameOfContract);

        $(".rename-form__buttons button[type='submit']").click(); // click blue button to accept
        $(".spinner").should(Condition.disappear); // wait until spinner will disappear
    }

    /**
     * Get total amount of discussion counter for this opened contract.
     * @return 'none' String in case if all discussion are closed, total amount of discussion otherwise
     */
    public String getAmountOfContractDiscussion()
    {
        SelenideElement contractDiscussionCounter = $(".contract-header__status .discussion-indicator__count");

        if( !contractDiscussionCounter.isDisplayed() )
        {
            return "none";
        }
        else
        {
            return $(".contract-header__status .discussion-indicator__count").getText();
        }
    }

    /**
     * Click by DISCUSSIONS tab.
     * @return
     */
    public Discussions clickByDiscussions()
    {
        $(".menu-disc-doc__discussions").shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("DISCUSSIONS tab was clicked...");

        return new Discussions(contractName.getText());
    }
}
