package com.parley.testing.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

public abstract class AbstractPage extends AbstractElement implements Page {
    public AbstractPage(WebDriver driverProvider) {
        super(driverProvider);
    }

    public void refresh() {
        getDriver().navigate().refresh();
    }

    public void move(By element) {
        click(element);
    }

    public void click(By element) {
        getDriver().findElement(element).click();
    }

    public WebElement findElement(By element){
        return getDriver().findElement(element);
    }

    public List<WebElement> findElements(By element){
        return getDriver().findElements(element);
    }

    public void uploadFile(String filePath) throws AWTException {
        StringSelection ss = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public void uploadDocument(String name, By selector) throws Throwable {
        File file = new File(
                getClass().getClassLoader().getResource(name).getFile()
        );
        waitUntilElementIsDisplayed(selector);
        findElement(selector).click();
        getDriver().findElement(selector).sendKeys(file.getAbsolutePath());

    }


}
