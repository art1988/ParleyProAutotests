package com.parley.testing.pages.impl.dashboard;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class AdministrationPage extends AbstractDashboardPage  {

    private static final By ADMINISTRATION_MENU = By.xpath("//a[contains(@class,'page-menu__item_settings')]");

    //Tabs
    private static final By MANAGE_USERS_TAB = By.xpath("//a[contains(text(), 'Manage users')]");
    private static final By INTEGRATIONS_TAB = By.xpath("//a[contains(text(), 'Integrations')]");
    private static final By WORKFLOWS_TAB = By.xpath("//a[contains(text(), 'Workflows')]");

    //Buttons
    private static final By NEW_USER_BUTTON =  By.xpath("//button[contains(text(), 'NEW USER')]");
    private static final By NEW_WORKFLOW =  By.xpath("//button[contains(text(), 'NEW WORKFLOW')]");


    public AdministrationPage(WebDriver driverProvider) {
        super(driverProvider, ADMINISTRATION_MENU);
    }

    public void checkManageUsersTabExists(){
        waitUntilElementIsDisplayed(MANAGE_USERS_TAB);
    }
    public void checkIntegrationsTabExists(){
        waitUntilElementIsDisplayed(INTEGRATIONS_TAB);
    }
    public void checkWorkFlowsTabExists(){
        waitUntilElementIsDisplayed(WORKFLOWS_TAB);
    }

    public void checkNewUserButtonExists(){
        move(MANAGE_USERS_TAB);
        waitUntilElementIsDisplayed(NEW_USER_BUTTON);
    }
    public void checkNewWorkFlowButtonExists(){
        move(WORKFLOWS_TAB);
        waitUntilElementIsDisplayed(NEW_WORKFLOW);
    }


}
