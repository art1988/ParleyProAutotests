package com.parley.testing.pages.impl;

import com.parley.testing.model.Template;
import com.parley.testing.pages.AbstractPage;
import org.codehaus.plexus.util.ExceptionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.print.DocFlavor;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.parley.testing.utils.AsyncAssert.waitForSuccess;
import static java.lang.String.format;
import static junit.framework.Assert.assertTrue;
import static org.testng.Assert.fail;

public class InProgressContractPage extends AbstractPage {
    private static final int TRY_TIMES = 40;
    private static final int TRY_DELAY = 3000;

    public static final By DOCUMENT_LIST = By.xpath("//a[contains(@class, 'documents__list-content')]");
    public static final By NEW_DOCUMENT_BUTTON = By.xpath("//button[text()='NEW DOCUMENT']");
    public static final By EMAIL_CONTRACT_BUTTON = By.xpath("//button[text()='EMAIL CONTRACT']");
    public static final By MY_TEAM_DOCUMENT = By.xpath("//button[text()='Upload my team documents']");
    public static final By CP_DOCUMENT = By.xpath("//button[text()='Upload Counterparty documents']");
    public static final By ATTACHEMENT_DOCUMENT = By.xpath("//button[text()='UPLOAD FROM YOUR COMPUTER']");
    public static final By SHARE_BUTTON = By.xpath("//button[text()='SHARE']");
    public static final By EDIT_DOCUMENT_BUTTON = By.xpath("//button[text()='EDIT DOCUMENT']");
    public static final By START_BUTTON = By.xpath("//button[text()='START']");
    public static final By NEXT_BUTTON = By.xpath("//button[text()='NEXT']");
    public static final By READY_FOR_SIGNATURE_BUTTON = By.xpath("//button[text()='READY FOR SIGNATURE']");

    public static final By APPROVE_BUTTON = By.xpath("//button[text()='APPROVE']");
    public static final By REJECT_BUTTON = By.xpath("//button[text()='REJECT']");

    public static final By TEMPLATES = By.xpath("//div[contains(@class, 'documents-add-templates-item')]");
    public static final By TEMPLATE_TITLE = By.xpath("//div[contains(@class, 'documents-add-templates-item__title')]");

    //Tabs
    public static final By UPLOAD_DOCUMENT_TAB = By.xpath("//span[contains(text(), 'Upload document')]");
    public static final By SELECT_TEMPLATE_TAB = By.xpath("//span[contains(text(), 'Select template')]");
    public static final By UPLOAD_ATTACHEMENT_TAB = By.xpath("//span[contains(text(), 'Upload attachment')]");

    //Contract actions
    public static final By CONTRACT_MENU = By.id("actions-menu");
    public static final By CONTRACT_INFO = By.xpath("//a[text()='Contract Info']");
    public static final By AUDIT_TRAIL = By.xpath("//a[text()='Audit trail']");
    public static final By CANCEL_CONTRACT = By.xpath("//a[text()='Cancel contract']");
    public static final By DELETE_CONTRACT = By.xpath("//a[text()='Delete contract']");
    public static final By REASSIGN_CN = By.xpath("//a[text()='Reassign Chief Negotiator']");

    //LifecycleTabs
    public static final By REVIEW = By.xpath("//div[contains(@class, 'lifecycle__item') and contains(@class, 'review')]");
    public static final By REVIEW_ACTIVE = By.xpath("//div[contains(@class, 'lifecycle__item') and contains(@class, 'review') and contains(@class, 'active')]");
    public static final By PRE_NEGOTIATON_APPROVAL = By.xpath("//div[contains(@class, 'lifecycle__item') and contains(@class, 'review')]");
    public static final By NEGOTIATON = By.xpath("//div[contains(@class, 'lifecycle__item') and contains(@class, 'negotiate')]");
    public static final By PRE_SIGN_APPROVAL = By.xpath("//div[contains(@class, 'lifecycle__item') and contains(@class, 'review')]");
    public static final By SIGN = By.xpath("//div[contains(@class, 'lifecycle__item') and contains(@class, 'sign')]");

    //Document actions
    public static final By DOCUMENT_MENU = By.xpath("//div[contains(@class, 'document__menu')]");
    public static final By FORMAT_DOCUMENT = By.xpath("//a[text()='Format']");
    public static final By DOWNLOAD_DOCUMENT = By.xpath("//a[text()='Download']");
    public static final By CANCEL_DOCUMENT = By.xpath("//a[text()='Cancel']");
    public static final By DELETE_DOCUMENT = By.xpath("//a[text()='Delete']");

    public InProgressContractPage(WebDriver driverProvider) {
        super(driverProvider);
    }


    @Override
    public void checkCurrentPage() throws Throwable {
        try {
            waitForSuccess(TRY_TIMES, TRY_DELAY, () -> assertTrue(findElement(DOCUMENT_LIST).isDisplayed()));
        } catch (AssertionError e) {
            fail("Current page is not In-Progress Contract Page!");
        } catch (Throwable e) {
            fail(format("Got an unexpected exception: '%1$s' with stack trace: %2$s",
                    e, ExceptionUtils.getFullStackTrace(e)));
        }
    }

    public void checkNewDocumentButtonDisplayed() {
        waitUntilElementIsDisplayed(NEW_DOCUMENT_BUTTON);
    }

    public void checkNewDocumentButtonNotDisplayed() {
        checkElementDoesNotExist(NEW_DOCUMENT_BUTTON);
    }

    public void checkEmailContractButtonDisplayed() {
        waitUntilElementIsDisplayed(EMAIL_CONTRACT_BUTTON);
    }

    public void checkEmailContractButtonNotDisplayed() {
        checkElementDoesNotExist(EMAIL_CONTRACT_BUTTON);
    }

    public void clickOnContractMenu() {
        WebDriverWait wait2 = new WebDriverWait(getDriver(), 10);
        wait2.until(ExpectedConditions.elementToBeClickable(CONTRACT_MENU));
        findElement(CONTRACT_MENU).click();
    }

    public void checkContractInfoDisplayed() {
        waitUntilElementIsDisplayed(CONTRACT_INFO);
    }

    public void checkContractInfoNotDisplayed() {
        checkElementDoesNotExist(CONTRACT_INFO);
    }

    public void checkAuditTrailDisplayed() {
        waitUntilElementIsDisplayed(AUDIT_TRAIL);
    }

    public void checkAuditTrailNotDisplayed() {
        checkElementDoesNotExist(AUDIT_TRAIL);
    }

    public void checkCancelContractDisplayed() {
        waitUntilElementIsDisplayed(CANCEL_CONTRACT);
    }

    public void checkCancelContractNotDisplayed() {
        checkElementDoesNotExist(CANCEL_CONTRACT);
    }

    public void checkDeleteContractDisplayed() {
        waitUntilElementIsDisplayed(DELETE_CONTRACT);
    }
    public void checkReassignCNIsDisplayed() {
        waitUntilElementIsDisplayed(REASSIGN_CN);
    }
    public void checkDeleteContractNotDisplayed() {
        checkElementDoesNotExist(DELETE_CONTRACT);
    }

    public void checkShareButtonIsDisplayed() {
        waitUntilElementIsDisplayed(SHARE_BUTTON);
    }

    public void checkEditDocumentButtonDisplayed() {
        waitUntilElementIsDisplayed(EDIT_DOCUMENT_BUTTON);
    }

    public void checkEditDocumentButtonNotDisplayed() {
        checkElementDoesNotExist(EDIT_DOCUMENT_BUTTON);
    }

    public void checkFormatDocumentIsDisplayed() {
        waitUntilElementIsDisplayed(FORMAT_DOCUMENT);
    }
    public void checkDownloadDocumentIsDisplayed() {
        waitUntilElementIsDisplayed(DOWNLOAD_DOCUMENT);
    }
    public void checkCancelDocumentIsDisplayed() {
        waitUntilElementIsDisplayed(CANCEL_DOCUMENT);
    }
    public void checkDeleteDocumentIsDisplayed() {
        waitUntilElementIsDisplayed(DELETE_DOCUMENT);
    }

    public void checkFormatDocumentNotDisplayed() {
        checkElementDoesNotExist(FORMAT_DOCUMENT);
    }
    public void checkCancelDocumentNotDisplayed() {
        checkElementDoesNotExist(CANCEL_DOCUMENT);
    }
    public void checkDeleteDocumentNotDisplayed() {
        checkElementDoesNotExist(DELETE_DOCUMENT);
    }
    public void checkDocumentActionsMenuNotDisplayed() {
        checkElementDoesNotExist(DOCUMENT_MENU);
    }

    public void checkApproveDocumentButtonIsDisplayed() {
        waitUntilElementIsDisplayed(APPROVE_BUTTON);
    }

    public void checkRejectDocumentButtonIsDisplayed() {
        waitUntilElementIsDisplayed(REJECT_BUTTON);
    }

    public void checkReadyForSignatureButtonIsDisplayed() {
        waitUntilElementIsDisplayed(READY_FOR_SIGNATURE_BUTTON);
    }
    public void clickReadyForSignatureButton() {
        findElement(READY_FOR_SIGNATURE_BUTTON).click();
    }


    public void clickOnDocumentMenu() {
        waitUntilElementIsDisplayed(DOCUMENT_MENU);
        findElement(DOCUMENT_MENU).findElement(CONTRACT_MENU).click();
    }

    public void checkUploadDocumentMenuIsAvailable() {
        waitUntilElementIsDisplayed(MY_TEAM_DOCUMENT);
    }

    public void uploadAttachement(String path) throws AWTException {
        getDriver().switchTo().activeElement();
        findElement(UPLOAD_ATTACHEMENT_TAB).click();
        findElement(ATTACHEMENT_DOCUMENT).click();
        uploadFile(path);
    }

    public void uploadTemplate(String name) throws Throwable {
        getDriver().switchTo().activeElement();
        findElement(SELECT_TEMPLATE_TAB).click();
        waitUntilElementIsDisplayed(TEMPLATES);
        Map<String, WebElement> templates = getTemplates();
        WebElement element = templates.get(name);
        if(element != null){
            element.click();
            checkShareButtonIsDisplayed();
        }
    }


    public Map<String, WebElement> getTemplates() {
        Map<String, WebElement> templates = new HashMap<String, WebElement>();
        List<WebElement> webElements = findElements(TEMPLATE_TITLE);
        for (WebElement element : webElements) {
            templates.put(element.getText(), element);
        }
        return templates;
    }

    public void moveToReview() {
       findElement(REVIEW).click();
       getDriver().switchTo().activeElement();
       waitUntilElementIsDisplayed(START_BUTTON);
       findElement(START_BUTTON).click();
       waitUntilElementIsDisplayed(REVIEW_ACTIVE);
    }

    public void moveToNegotiate() {
        findElement(NEGOTIATON).click();
        getDriver().switchTo().activeElement();
        waitUntilElementIsDisplayed(NEXT_BUTTON);
        findElement(NEXT_BUTTON).click();
        getDriver().switchTo().activeElement();
        waitUntilElementIsDisplayed(START_BUTTON);
        findElement(START_BUTTON).click();
    }

}
