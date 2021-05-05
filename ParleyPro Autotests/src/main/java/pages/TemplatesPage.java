package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.add.AddTemplates;
import forms.add.CreateBundle;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import pages.tooltips.TemplatesActionMenu;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

public class TemplatesPage
{
    private SelenideElement newTemplateButton = $(".js-new-template-button");
    private SelenideElement newBundleButton   = $(".js-new-bundle-button");


    private static Logger logger = Logger.getLogger(TemplatesPage.class);

    /**
     * @param isBlank if true then no any template records were added.
     */
    public TemplatesPage(boolean isBlank)
    {
        // Assert page header
        $(".page-head__cell.page-head__left").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Templates"));

        if( isBlank )
        {
            // Assert that title is present
            $(".templates-board__empty").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("There are no templates"));
        }
        else
        {
            $(".spinner").waitUntil(Condition.disappear, 10_000);

            // Assert that heading of table is visible
            $(".templates-board__list thead").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Template name Status Last activity"));

            // Make sure that + NEW TEMPLATE button is also visible
            newTemplateButton.shouldBe(Condition.visible).shouldBe(Condition.enabled);
        }
    }

    public void clickUploadTemplatesButton(File fileToUpload)
    {
        // 1. make <input> visible
        Selenide.executeJavaScript("$('.upload__body input').css(\"height\",\"auto\")");
        Selenide.executeJavaScript("$('.upload__body input').css(\"visibility\",\"visible\")");
        Selenide.executeJavaScript("$('.upload__body input').css(\"display\",\"block\")");

        // 2. trying to upload...
        SelenideElement uploadCounterpartyDocumentsButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

        uploadCounterpartyDocumentsButton.uploadFile(fileToUpload);

        $(".spinner").waitUntil(Condition.disappear, 35_000);
    }

    /**
     * Click by template by templateName
     * @param templateName
     */
    public EditTemplatePage selectTemplate(String templateName)
    {
        Selenide.executeJavaScript("$('.templates-board tbody .template__title:contains(\"" + templateName + "\")').click()");

        logger.info(templateName + " was selected");

        return new EditTemplatePage();
    }

    /**
     * Invoke action menu by clicking of 3 dotted button for given template name
     * @param templateName
     */
    public TemplatesActionMenu clickActionMenu(String templateName)
    {
        // First of all - make button visible
        Selenide.executeJavaScript("$('.template__title:contains(\"" + templateName + "\")').next().next().next().find(\"button\").css('visibility', 'visible');");

        // Second, click that button
        Selenide.executeJavaScript("$('.template__title:contains(\"" + templateName + "\")').next().next().next().find(\"button\").click()");

        // Third, make context menu visible
        Selenide.executeJavaScript("$('.template__title:contains(\"" + templateName + "\")').next().next().next().find(\"button\").next().css('visibility', 'visible')");

        return new TemplatesActionMenu(templateName);
    }

    public AddTemplates clickNewTemplate()
    {
        newTemplateButton.waitUntil(Condition.visible, 15_000).click();

        logger.info("+ NEW TEMPLATE button was clicked");

        return new AddTemplates();
    }

    public CreateBundle clickNewBundle()
    {
        newBundleButton.waitUntil(Condition.visible, 15_000).click();

        logger.info("+ NEW BUNDLE button was clicked");

        return new CreateBundle();
    }

    public TemplatesPage clickBundlesTab()
    {
        WebElement bundlesTabButton = Selenide.executeJavaScript("return $('.tab-menu__item:contains(\"BUNDLES\")')[0]");
        $(bundlesTabButton).click();

        return this;
    }
}
