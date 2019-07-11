package com.parley.testing.pages.impl.dashboard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class DashboardPage extends AbstractDashboardPage {

    private static final By DASHBOARD_MENU = By.xpath("//a[contains(@class,'page-menu__item_dashboard')]");

    public DashboardPage(WebDriver driverProvider) {
        super(driverProvider, DASHBOARD_MENU);
    }
}
