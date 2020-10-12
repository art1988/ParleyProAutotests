package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.*;
import org.apache.log4j.Logger;
import org.testng.Assert;
import pages.tooltips.ContractActionsMenu;
import pages.tooltips.DocumentActionsMenu;
import pages.tooltips.ParagraphActionsPopup;
import utils.Waiter;

import static com.codeborne.selenide.Selenide.$;

public class OpenedContract
{
    private SelenideElement contractName       = $(".contract-header__name");
    private SelenideElement actionsMenu        = $(".contract-header__menu .actions-menu button");


    private static Logger logger = Logger.getLogger(OpenedContract.class);

    public OpenedContract()
    {
        Assert.assertTrue( isInit() );
    }

    private boolean isInit()
    {
        $(".contract-header__status").waitUntil(Condition.visible, 7_000);

        return ( contractName.isDisplayed() );
    }

    public StartReview switchDocumentToReview()
    {
        // hover over DRAFT
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append("$('.document__header-info .lifecycle')[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible("$('.document__header-info .lifecycle').find(\"div:contains('REVIEW')\")");

        Selenide.executeJavaScript("$('.document__header-info .lifecycle').find(\"div:contains('REVIEW')\").click()");

        logger.info("REVIEW was clicked");

        return new StartReview(contractName.text());
    }

    public StartNegotiation switchDocumentToNegotiate()
    {
        // hover over DRAFT
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append("$('.document__header-info .lifecycle')[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible("$('.document__header-info .lifecycle').find(\"div:contains('NEGOTIATE')\")");

        Selenide.executeJavaScript("$('.document__header-info .lifecycle').find(\"div:contains('NEGOTIATE')\").click()");

        logger.info("NEGOTIATE was clicked");

        return new StartNegotiation(contractName.text());
    }

    public SignContract switchDocumentToSign()
    {
        // hover over DRAFT
        StringBuffer jsCode = new StringBuffer("var event = new MouseEvent('mouseover', {bubbles: true, cancelable: true});");
        jsCode.append("$('.document__header-info .lifecycle')[0].dispatchEvent(event);");

        Selenide.executeJavaScript(jsCode.toString());

        Waiter.smartWaitUntilVisible("$('.document__header-info .lifecycle').find(\"div:contains('SIGN')\")");

        Selenide.executeJavaScript("$('.document__header-info .lifecycle').find(\"div:contains('SIGN')\").click()");

        logger.info("SIGN was clicked");

        return new SignContract(contractName.text());
    }

    /**
     * Click by EDIT DOCUMENT button. Available in Draft stage
     */
    public EditDocumentPage clickEditDocument(String documentName)
    {
        Selenide.executeJavaScript("$('.document__title button:contains(\"EDIT\")').click()");

        logger.info("Edit Document was clicked");

        return new EditDocumentPage(documentName, false);
    }

    public CompleteSign clickCompleteSign()
    {
        Selenide.executeJavaScript("$('.document__title button[spinnersize=\"xs\"]').click()");

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
        Selenide.executeJavaScript("$('.document__title:contains(\"" + documentName + "\")').parent().find(\"div[class='document__menu']\").find(\"button\").click()");

        return new DocumentActionsMenu(documentName);
    }
}
