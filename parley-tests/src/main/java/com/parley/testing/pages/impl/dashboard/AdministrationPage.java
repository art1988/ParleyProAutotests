package com.parley.testing.pages.impl.dashboard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class AdministrationPage extends AbstractDashboardPage  {

    private static final By ADMINISTRATION_MENU = By.xpath("//a[contains(@class,'page-menu__item_settings')]");


    public AdministrationPage(WebDriver driverProvider) {
        super(driverProvider, ADMINISTRATION_MENU);
    }



}
