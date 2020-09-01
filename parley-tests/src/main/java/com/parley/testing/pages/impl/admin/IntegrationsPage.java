package com.parley.testing.pages.impl.admin;

import com.parley.testing.pages.impl.dashboard.AdministrationPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class IntegrationsPage extends AdministrationPage {

    private static final By INTEGRATIONS_TAB = By.xpath("//a[contains(text(), 'Integrations')]");
    public IntegrationsPage(WebDriver driverProvider) {
        super(driverProvider);
    }

    public void checkIntegrationsTabExists(){
        waitUntilElementIsDisplayed(INTEGRATIONS_TAB);
    }
}
