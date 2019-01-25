package com.parley.testing.pages.impl;

import com.parley.testing.pages.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class CreateInProgressContractPage extends AbstractPage {

    private static final By CONTRACT_TITLE = By.id("214");
    private static final By CONTRACT_VALUE_BUTTON= By.id("dropdown-button-6");
    private static final By CONTRACT_CURRENCY_VALUE = By.xpath("//div[contains(@class, 'dropdown__title') and contains(text(), '?']");
    private static final By CONTRACT_VALUE_INPUT= By.xpath("//input[contains(@class, 'form-control') and contains(@class, 'input__input')]");
    private static final By CONTRACT_LIST_ITEM = By.className("ui-tr contract-item");

    public CreateInProgressContractPage(WebDriver driverProvider) {
        super(driverProvider);
    }

    @Override
    public void checkCurrentPage() throws Throwable {
        int tryTimes = 180;
        int tryDelay = 1000;

    }
}
