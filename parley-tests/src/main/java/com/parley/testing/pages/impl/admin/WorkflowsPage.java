package com.parley.testing.pages.impl.admin;

import com.parley.testing.pages.impl.dashboard.AdministrationPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WorkflowsPage extends AdministrationPage {

    private static final By WORKFLOWS_TAB = By.xpath("//a[contains(text(), 'Workflows')]");
    //Buttons
    private static final By NEW_WORKFLOW =  By.xpath("//button[contains(text(), 'NEW WORKFLOW')]");

    public WorkflowsPage(WebDriver driverProvider) {
        super(driverProvider);
    }

    public void checkWorkFlowsTabExists(){
        waitUntilElementIsDisplayed(WORKFLOWS_TAB);
    }

    public void checkNewWorkFlowButtonExists(){
        waitUntilElementIsDisplayed(WORKFLOWS_TAB);
        move(WORKFLOWS_TAB);
        waitUntilElementIsDisplayed(NEW_WORKFLOW);
    }
}
