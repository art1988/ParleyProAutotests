package com.parley.testing.pages.impl;

import com.parley.testing.pages.AbstractPage;
import org.codehaus.plexus.util.ExceptionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static com.parley.testing.utils.AsyncAssert.waitForSuccess;
import static java.lang.String.format;
import static junit.framework.Assert.assertTrue;
import static org.testng.Assert.fail;

public class InProgressContractPage extends AbstractPage {
    private static final int TRY_TIMES = 40;
    private static final int TRY_DELAY = 3000;

    public static final By DOCUMENT_LIST = By.xpath("//a[contains(@class, 'documents__list-content')]");
    public static final By NEW_DOCUMENT_BUTTON =  By.xpath("//button[text()='NEW DOCUMENT']");
    public static final By EMAIL_CONTRACT_BUTTON =  By.xpath("//button[text()='EMAIL CONTRACT']");

    public static final By CONTRACT_MENU = By.id("actions-menu");
    public static final By CONTRACT_INFO =  By.xpath("//a[text()='Contract Info']");
    public static final By AUDIT_TRAIL =  By.xpath("//a[text()='Audit trail']");
    public static final By CANCEL_CONTRACT =  By.xpath("//a[text()='Cancel contract']");
    public static final By DELETE_CONTRACT =  By.xpath("//a[text()='Delete contract']");

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

    public void checkNewDocumentButtonDisplayed(){
        waitUntilElementIsDisplayed(NEW_DOCUMENT_BUTTON);
    }

    public void checkNewDocumentButtonNotDisplayed(){
        checkElementDoesNotExist(NEW_DOCUMENT_BUTTON);
    }

    public void checkEmailContractButtonDisplayed(){
        waitUntilElementIsDisplayed(EMAIL_CONTRACT_BUTTON);
    }

    public void checkEmailContractButtonNotDisplayed(){
        checkElementDoesNotExist(EMAIL_CONTRACT_BUTTON);
    }

    public void clickOnContractMenu(){
        findElement(CONTRACT_MENU).click();
    }
    public void checkContractInfoDisplayed(){
        waitUntilElementIsDisplayed(CONTRACT_INFO);
    }

    public void checkContractInfoNotDisplayed(){
        checkElementDoesNotExist(CONTRACT_INFO);
    }

    public void checkAuditTrailDisplayed(){
        waitUntilElementIsDisplayed(AUDIT_TRAIL);
    }

    public void checkAuditTrailNotDisplayed(){
        checkElementDoesNotExist(AUDIT_TRAIL);
    }

    public void checkCancelContractDisplayed(){
        waitUntilElementIsDisplayed(CANCEL_CONTRACT);
    }

    public void checkCancelContractNotDisplayed(){
        checkElementDoesNotExist(CANCEL_CONTRACT);
    }

    public void checkDeleteContractDisplayed(){
        waitUntilElementIsDisplayed(DELETE_CONTRACT);
    }

    public void checkDeleteContractNotDisplayed(){
        checkElementDoesNotExist(DELETE_CONTRACT);
    }



}
