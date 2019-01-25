package com.parley.testing.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class AbstractPage extends AbstractElement implements Page {
    public AbstractPage(WebDriver driverProvider) {
        super(driverProvider);
    }

    public void refresh() {
        getDriver().navigate().refresh();
    }

    public WebElement findElement(By element){
        return getDriver().findElement(element);
    }

    public List<WebElement> findElements(By element){
        return getDriver().findElements(element);
    }

}
