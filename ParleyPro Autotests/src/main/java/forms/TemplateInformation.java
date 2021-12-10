package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class TemplateInformation
{
    private SelenideElement templateNameField     = $("input[data-label='Template name']");
    private SelenideElement regionField           = $("#template_region input");
    private SelenideElement categoryField         = $("#template_category input");
    private SelenideElement descriptionTextArea   = $(".modal-content textarea");

    private SelenideElement cancelButton = $(".button.btn-common.btn-link-pseudo.btn.btn-link");
    private SelenideElement saveButton   = $(".button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(TemplateInformation.class);

    public TemplateInformation()
    {
        $(".spinner").should(Condition.disappear);

        $(".modal-body-title").shouldBe(Condition.visible).shouldHave(Condition.exactText("Template information"));
    }

    public void setTemplateName(String name) throws InterruptedException
    {
        Thread.sleep(1000);

        // Clear field first
        Selenide.executeJavaScript("$('input[data-label=\"Template name\"]').val('')");

        Thread.sleep(1500);

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
        return Selenide.executeJavaScript("return $('#template_region .new-select__single-value').text()");
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
        return Selenide.executeJavaScript("return $('#template_category .new-select__single-value').text()");
    }

    public void setType(String type) throws InterruptedException
    {
        Selenide.executeJavaScript("$('input[data-label=\"Contract types\"]').click()");
        Selenide.executeJavaScript("$('.multi-select .dropdown-menu').find(\"label:contains('All')\").click()"); // uncheck All
        Selenide.executeJavaScript("$('span:contains(\"Contract type\")').parent().parent().next().find(\"label:contains('" + type + "')\").click()");
        Selenide.executeJavaScript("$('input[data-label=\"Contract types\"]').click()"); // click again to close options menu

        $("input[data-label='Contract types']").pressTab(); // Press tab to move focus
    }

    public String getType()
    {
        return $("input[data-label='Contract types']").getValue();
    }

    /**
     * Click by 'Available through integrations' checkbox
     */
    public TemplateInformation clickIntegrationsCheckmark()
    {
        Selenide.executeJavaScript("$('#templateFormExternal').next().next().click()");

        logger.info("'Available through integrations' checkbox was selected...");

        return this;
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

        $(".modal-content").should(Condition.disappear);
    }

}
