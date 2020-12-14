package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class TemplateInformation
{
    private SelenideElement templateNameField     = $("input[label='Template name']");
    private SelenideElement regionField           = $("#template_region input");
    private SelenideElement categoryField         = $("#template_category input");
    private SelenideElement descriptionTextArea   = $(".modal-content textarea");

    private SelenideElement cancelButton = $(".button.btn-common.btn-link-pseudo.btn.btn-link");
    private SelenideElement saveButton   = $(".button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(TemplateInformation.class);

    public TemplateInformation()
    {
        $(".spinner").waitUntil(Condition.disappear, 7_000);

        $(".modal-body-title").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Template information"));
    }

    public void setTemplateName(String name) throws InterruptedException
    {
        // Clear field first
        Selenide.executeJavaScript("$('input[label=\"Template name\"]').val('')");

        Thread.sleep(1000);

        templateNameField.setValue(name);
    }

    public String getTemplateName()
    {
        return templateNameField.getValue();
    }

    public void setRegion(String regionName) throws InterruptedException
    {
        regionField.setValue(regionName);
        Thread.sleep(500);

        regionField.sendKeys(Keys.ENTER);
        Thread.sleep(500);
    }

    public String getRegion()
    {
        return Selenide.executeJavaScript("return $('#template_region .Select-value').text()");
    }

    public void setCategory(String categoryName) throws InterruptedException
    {
        categoryField.setValue(categoryName);
        Thread.sleep(500);

        categoryField.sendKeys(Keys.ENTER);
        Thread.sleep(500);
    }

    public String getCategory()
    {
        return Selenide.executeJavaScript("return $('#template_category .Select-value').text()");
    }

    public void setType(String type) throws InterruptedException
    {
        Selenide.executeJavaScript("$('input[label=\"Contract types\"]').click()");
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('" + type + "')\").click()");
        Selenide.executeJavaScript("$('input[label=\"Contract types\"]').click()"); // click again to close options menu

        $("input[label='Contract types']").pressTab(); // Press tab to move focus
    }

    public String getType()
    {
        return $("input[label='Contract types']").getValue();
    }

    public void clickIntegrationsCheckmark()
    {
        Selenide.executeJavaScript("$('#templateFormExternal').next().next().click()");

        logger.info("Available through integrations checkmark was selected...");
    }

    public void setDescription(String text)
    {
        descriptionTextArea.setValue(text);
    }

    public String getDescription()
    {
        return descriptionTextArea.getText();
    }

    public void clickCancel()
    {
        cancelButton.click();

        logger.info("CANCEL button was clicked...");
    }

    public void clickSave()
    {
        logger.info("SAVE button was clicked...");

        saveButton.click();
        $(".modal-content").waitUntil(Condition.disappear, 7_000);
    }

}
