package com.parley.testing.pages.impl.dashboard;

import com.google.common.base.Strings;
import com.parley.testing.model.contracts.ExecutedContract;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.testng.Assert.fail;

public class ExecutedContractsPage extends AbstractDashboardPage {

    private static final By EXECUTED_MENU = By.xpath("//a[contains(@class,'page-menu__item_executed-contracts')]");
    private static final By CREATE_CONTRACT_BUTTON =  By.xpath("//button[contains(text(), 'NEW CONTRACT')]");

    //contract form
    public static final By CONTRACT_LIST_ITEM = By.xpath("//a[contains(@class, 'ui-tr') and contains(@class ,'contract-item')]");
    public static final By CONTRACT_TITLE = By.xpath(".//div[boolean(@title)]");
    public static final By CONTRACT_COUNTERPARTY = By.xpath(".//div[contains(@class, 'contracts-list__counterparty-cell')]");
    public static final By CONTRACT_STAGE = By.xpath(".//div[contains(@class, 'contracts-list__cell-stage')]/div/div");

    public ExecutedContractsPage(WebDriver webDriverProvider) {
        super(webDriverProvider, EXECUTED_MENU);
    }

    public List<ExecutedContract> getExecutedContracts() throws InterruptedException {
        List<ExecutedContract> list = new ArrayList<ExecutedContract>();
        Thread.sleep(3000);
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
        Assert.assertTrue((executedContracts != null) && (!executedContracts.isEmpty()));
        for(ExecutedContract contract : executedContracts){
            Assert.assertTrue(!Strings.isNullOrEmpty(contract.getTitle()));
            Assert.assertTrue(!Strings.isNullOrEmpty(contract.getStage()));
        }
    }


}
