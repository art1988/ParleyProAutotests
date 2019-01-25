package com.parley.testing.pages.impl;

import com.google.common.base.Strings;
import com.parley.testing.model.ExecutedContract;
import com.parley.testing.model.InProgressContract;
import com.parley.testing.pages.AbstractPage;
import org.codehaus.plexus.util.ExceptionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.parley.testing.utils.AsyncAssert.waitForSuccess;
import static java.lang.String.format;
import static junit.framework.TestCase.assertTrue;
import static org.testng.Assert.fail;

public class ExecutedContractsPage extends AbstractPage {

    private static final int TRY_TIMES = 40;
    private static final int TRY_DELAY = 3000;

    private static final By EXECUTED_MENU = By.xpath("//a[contains(@class,'page-menu__item_executed-contracts')]");
    private static final By CREATE_CONTRACT_BUTTON =  By.xpath("//button[contains(text(), 'NEW CONTRACT')]");

    //contract form
    public static final By CONTRACT_LIST_ITEM = By.xpath("//a[contains(@class, 'ui-tr') and contains(@class ,'contract-item')]");
    public static final By CONTRACT_TITLE = By.xpath(".//div[boolean(@title)]");
    public static final By CONTRACT_COUNTERPARTY = By.xpath(".//div[contains(@class, 'contracts-list__counterparty-cell')]");
    public static final By CONTRACT_STAGE = By.xpath(".//div[contains(@class, 'contracts-list__cell-stage')]/div/div");

    public ExecutedContractsPage(WebDriver webDriverProvider) {
        super(webDriverProvider);
    }

    @Override
    public void checkCurrentPage() {
        try {
            waitForSuccess(TRY_TIMES, TRY_DELAY, () -> assertTrue(findElement(EXECUTED_MENU).isDisplayed()));
        } catch (AssertionError e) {
            fail("Current page is not In-Progress Contracts Page!");
        } catch (Throwable e) {
            fail(format("Got an unexpected exception: '%1$s' with stack trace: %2$s",
                    e, ExceptionUtils.getFullStackTrace(e)));
        }
    }

    public List<ExecutedContract> getExecutedContracts(){
        List<ExecutedContract> list = new ArrayList<ExecutedContract>();
        List<WebElement> webElements = findElements(CONTRACT_LIST_ITEM);
        for(WebElement element : webElements){
            ExecutedContract executedContract = new ExecutedContract();
            executedContract.setLink(element.getAttribute("href"));
            executedContract.setTitle(element.findElement(CONTRACT_TITLE).getAttribute("title"));
            executedContract.setCounterparty(element.findElement(CONTRACT_COUNTERPARTY).getText());
            executedContract.setStage(element.findElement(CONTRACT_STAGE).getText());

            list.add(executedContract);
        }
        return list;
    }

    public void checkCreateContractButtonExists(){
        waitUntilElementIsDisplayed(CREATE_CONTRACT_BUTTON);
    }

    public void checkCreateContractButtonNotDisplayed(){
        checkElementDoesNotExist(CREATE_CONTRACT_BUTTON);
    }

    public void checkContractRequiredFieldsNotEmpty(List<ExecutedContract> executedContracts){
        for(ExecutedContract contract : executedContracts){
            Assert.assertTrue(!Strings.isNullOrEmpty(contract.getTitle()));
            Assert.assertTrue(!Strings.isNullOrEmpty(contract.getStage()));
        }
    }

    public void moveToExecutedContracts(){
        findElement(EXECUTED_MENU).click();
    }

}
