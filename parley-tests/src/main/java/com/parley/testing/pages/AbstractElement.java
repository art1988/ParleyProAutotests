package com.parley.testing.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniumhq.selenium.fluent.FluentExecutionStopped;
import org.seleniumhq.selenium.fluent.FluentWebDriver;
import org.seleniumhq.selenium.fluent.FluentWebElement;
import org.seleniumhq.selenium.fluent.Period;
import org.seleniumhq.selenium.fluent.internal.NegatingFluentWebDriver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.springframework.util.ReflectionUtils.findMethod;
import static org.springframework.util.ReflectionUtils.invokeMethod;


public abstract class AbstractElement {

    private static final int WAIT_TIMEOUT = 50;

    private static final String ELEMENT_IS_VISIBLE_ERR_MSG = "Element visible but should not exist";
    private static final String ELEMENT_IS_NOT_VISIBLE_ERR_MSG = "Element isn't visible but should exist";
    private static final String ELEMENT_EXISTS_ERR_MSG = "Element exists but should not";

    private WebDriver driver;

    public AbstractElement(final WebDriver driver) {
        this.driver = driver;
    }

    public final void checkElementDoesNotExist(final By byXpath) {
        try {
            WebElement element = driver.findElement(byXpath);
            assertFalse(ELEMENT_IS_VISIBLE_ERR_MSG, element.isDisplayed());
        } catch (FluentExecutionStopped e) {
            return;
        } catch (NoSuchElementException e) {
            return;
        }
        //throw new AssertionError(ELEMENT_EXISTS_ERR_MSG);
    }

    public final void checkRelativeElementDoesNotExist(WebElement parent, final By byXpath) {
        try {
            WebElement element = parent.findElement(byXpath);
            assertFalse(ELEMENT_IS_VISIBLE_ERR_MSG, element.isDisplayed());
        } catch (FluentExecutionStopped e) {
            return;
        } catch (NoSuchElementException e) {
            return;
        }
        //throw new AssertionError(ELEMENT_EXISTS_ERR_MSG);
    }

    public final void checkElementExists(final By byXpath) {
        try {
            WebElement element = driver.findElement(byXpath);
            assertTrue(ELEMENT_IS_NOT_VISIBLE_ERR_MSG, element.isDisplayed());
        } catch (FluentExecutionStopped e) {
            return;
        } catch (NoSuchElementException e) {
            return;
        }
        //throw new AssertionError(ELEMENT_EXISTS_ERR_MSG);
    }

    public final void waitUntilElementIsDisplayed(final By bySelector) {
        WebDriverWait wait =
                new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until((WebDriver driver) -> !driver.findElements(bySelector).isEmpty()
                && driver.findElement(bySelector).isDisplayed());
    }

    public final void waitUntilElementIsEnabled(final By bySelector) {
        WebDriverWait wait =
                new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until((WebDriver driver) -> !driver.findElements(bySelector).isEmpty()
                && driver.findElement(bySelector).isDisplayed()
                && driver.findElement(bySelector).isEnabled());
    }

    public final void waitUntilElementIsDisabled(final By bySelector) {
        WebDriverWait wait =
                new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until((WebDriver driver) -> !driver.findElements(bySelector).isEmpty()
                && driver.findElement(bySelector).isDisplayed()
                && !driver.findElement(bySelector).isEnabled());
    }

    public final void waitUntilElementIsNotDisplayed(final By bySelector) {
        Wait<WebDriver> wait =
                new WebDriverWait(driver, WAIT_TIMEOUT).ignoring(StaleElementReferenceException.class);
        wait.until((WebDriver driver) -> driver.findElements(bySelector).isEmpty()
                || !driver.findElement(bySelector).isDisplayed());
    }

    /**
     * Wait until select located by given locator has options for selection.
     *
     * @param bySelector locator for target select element to set
     * @throws TimeoutException when a command does not complete in enough time
     */
    public final void waitUntilSelectHasOptions(final By bySelector) {
        WebDriverWait wait =
                new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until((WebDriver driver) -> {
            WebElement selectElement = driver.findElement(bySelector);
            Select select = new Select(selectElement);
            return !select.getOptions().isEmpty();
        });
    }

    public void waitUntilElementHasText(final By bySelector, final String text) {
        WebDriverWait wait =
                new WebDriverWait(driver, WAIT_TIMEOUT);
        wait.until((WebDriver driver) -> {
            WebElement element = driver.findElement(bySelector);
            return element.getText().equals(text);
        });
    }

    public Boolean isSessionLost() {
        try {
            driver.getPageSource();
        } catch (FluentExecutionStopped e) {
            return true;
        }
        return false;
    }

    /**
     * @return the current <code>WebDriver</code> instance
     */
    public WebDriver getDriver() {
        return driver;
    }


    public void selectItemInDropDownMenu(FluentWebElement menu, FluentWebElement item) {
        Actions action = new Actions(getDriver());
        action.moveToElement(menu.getWebElement());
        action.moveToElement(item.getWebElement());
        action.click();
        action.perform();
    }


    protected Period getTimeout() {
        return Period.secs(WAIT_TIMEOUT);
    }




}
