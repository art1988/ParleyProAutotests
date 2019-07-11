package com.parley.testing.pages.impl.dashboard;


import com.google.common.base.Strings;
import com.parley.testing.model.Template;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

public class TemplatesPage extends AbstractDashboardPage {

    private static final By TEMPLATES_MENU = By.xpath("//a[contains(@class,'page-menu__item_templates')]");

    private static final By NEW_TEMPLATE_BUTTON =  By.xpath("//button[contains(text(), 'NEW TEMPLATE')]");

    private static final By TEMPLATE_LIST_ITEM = By.xpath("//tbody/tr[contains(@class, 'ui-tr') and contains(@class ,'template')]");
    private static final By TEMPLATE_TITLE = By.xpath(".//td[contains(@class, 'template__title')]");
    private static final By TEMPLATE_STATUS = By.xpath(".//td[contains(@class, 'template__status')]");

    public TemplatesPage(WebDriver driverProvider) {
        super(driverProvider, TEMPLATES_MENU);
    }

    public void checkNewTemplateButtonExists(){
        waitUntilElementIsDisplayed(NEW_TEMPLATE_BUTTON);
    }

    public List<Template> getTemplates(){
        List<Template> list = new ArrayList<Template>();
        List<WebElement> webElements = findElements(TEMPLATE_LIST_ITEM);
        for(WebElement element : webElements){
            Template template = new Template();
            template.setTitle(element.findElement(TEMPLATE_TITLE).getText());
            template.setStatus(element.findElement(TEMPLATE_STATUS).getText());
            list.add(template);
        }
        return list;
    }

    public void checkContractRequiredFieldsNotEmpty(List<Template> templates){
        Assert.assertTrue((templates != null) && (!templates.isEmpty()));
        for(Template template : templates){
            Assert.assertTrue(!Strings.isNullOrEmpty(template.getTitle()));
            Assert.assertTrue(!Strings.isNullOrEmpty(template.getStatus()));
        }
    }
}