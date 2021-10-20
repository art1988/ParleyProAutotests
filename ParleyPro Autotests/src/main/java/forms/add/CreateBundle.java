package forms.add;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Form that appears after clicking of '+ NEW BUNDLE' button, or by invoking 'Bundle info' action menu
 */
public class CreateBundle
{
    private SelenideElement bundleNameField         = $("input[data-label='Bundle Name']");
    private SelenideElement selectTemplatesDropdown = $("input[data-label='Select templates to place into bundle']");

    private SelenideElement cancelButton = $("._button.scheme_gray.size_lg");
    private SelenideElement nextButton   = $("._button.scheme_blue.size_lg");
    private SelenideElement backArrow    = $(".manage-users-new__back");


    private static Logger logger = Logger.getLogger(CreateBundle.class);

    public CreateBundle()
    {
        $(".modal-body-title").waitUntil(Condition.visible, 30_000).shouldHave(Condition.exactText("Create bundle"));

        bundleNameField.shouldBe(Condition.visible);
        selectTemplatesDropdown.shouldBe(Condition.visible);
    }

    /**
     * Use this after invoking 'Bundle info' action menu
     * @param inEditMode just marker
     * @param bundleName
     */
    public CreateBundle(boolean inEditMode, String bundleName)
    {
        $(".modal-body-title").waitUntil(Condition.visible, 30_000).shouldHave(Condition.exactText(bundleName));
        $(".modal-body .tab-menu").waitUntil(Condition.visible, 30_000);

        bundleNameField.shouldBe(Condition.visible);
        selectTemplatesDropdown.shouldBe(Condition.visible);
    }

    public void setBundleName(String bundleName)
    {
        bundleNameField.sendKeys(bundleName);
    }

    /**
     * Expands "Select templates to place into bundle" dropdown and choose all templates that were passed
     * @param templates array of templates to choose
     */
    public void selectTemplates(String[] templates)
    {
        selectTemplatesDropdown.click(); // expand dropdown
        $(".modal-body .dropdown-menu").waitUntil(Condition.visible, 30_000);

        outer:
        for( int n = 0; n < $$(".checkbox__label").size(); n++ )
        {
            SelenideElement item = $$(".checkbox__label").get(n);

            for( int i = 0; i < templates.length; i++ )
            {
                if( templates[i].equals(item.getText()) )
                {
                    item.click();
                    logger.info("Template " + templates[i] + " was selected...");
                    continue outer;
                }
            }
        }

        selectTemplatesDropdown.click(); // collapse dropdown
    }

    /**
     * Removes template by name ( CONTAINS ! ) by clicking x
     * @param templateName name of the template to be removed
     * @return
     */
    public CreateBundle removeTemplate(String templateName)
    {
        Selenide.executeJavaScript("$('.template-bundle__item-name:contains(\"" + templateName + "\")').parent().find('.template-bundle__item-remove').click()");

        logger.info("Template " + templateName + " was removed...");

        return this;
    }

    public CreateBundle clickNext()
    {
        nextButton.click();

        logger.info("NEXT button was clicked.");

        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return this;
    }

    public CreateBundle clickBack()
    {
        backArrow.waitUntil(Condition.visible, 30_000).click();

        logger.info("< BACK button was clicked.");

        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return this;
    }

    public void clickCreate()
    {
        $((WebElement) Selenide.executeJavaScript("return $('.template-bundle__footer-buttons button:contains(\"Create\")')[0]")).click();

        logger.info("CREATE button was clicked.");
    }

    public void clickSave()
    {
        $((WebElement) Selenide.executeJavaScript("return $('.template-bundle__footer-buttons button:contains(\"Save\")')[0]")).click();

        logger.info("SAVE button was clicked.");
    }

    public void clickCancel()
    {
        cancelButton.click();

        logger.info("CANCEL button was clicked.");
    }

    public CreateBundle setRegion(String region)
    {
        $("#template_region div").waitUntil(Condition.visible, 7_000).click(); // expand dropdown
        $(".Select-menu-outer").waitUntil(Condition.appear, 7_000);
        WebElement item = Selenide.executeJavaScript("return $('.Select-menu-outer .Select-option:contains(\"" + region + "\")')[0]");
        $(item).click();

        return this;
    }

    public CreateBundle setDepartment(String department)
    {
        $("#template_department div").waitUntil(Condition.visible, 7_000).click(); // expand dropdown
        $(".Select-menu-outer").waitUntil(Condition.appear, 7_000);
        WebElement item = Selenide.executeJavaScript("return $('.Select-menu-outer .Select-option:contains(\"" + department + "\")')[0]");
        $(item).click();

        return this;
    }

    public CreateBundle setCategory(String category)
    {
        $("#template_category div").waitUntil(Condition.visible, 7_000).click(); // expand dropdown
        $(".Select-menu-outer").waitUntil(Condition.appear, 7_000);
        WebElement item = Selenide.executeJavaScript("return $('.Select-menu-outer .Select-option:contains(\"" + category + "\")')[0]");
        $(item).click();

        return this;
    }

    public CreateBundle setType(String type)
    {
        $("input[data-id='contract-types']").waitUntil(Condition.visible, 7_000).click(); // expand dropdown
        $(".modal-body .dropdown-menu").waitUntil(Condition.appear, 7_000);
        WebElement item = Selenide.executeJavaScript("return $('.modal-body .dropdown-menu label:contains(\"" + type + "\")')[0]");
        $(item).click();

        return this;
    }

    public CreateBundle setDescription(String description)
    {
        $(".modal-body textarea").sendKeys(description);

        return this;
    }

    public CreateBundle togglePublishBundle()
    {
        $(".template-bundle__tumbler .tumbler").click();

        logger.info("Publish bundle tumbler was clicked...");

        return this;
    }

    public CreateBundle clickInformationTab()
    {
        Selenide.executeJavaScript("$('.modal-body .tab-menu__item:contains(\"INFO\")').click()");

        logger.info("INFORMATION tab was clicked...");

        return this;
    }
}
