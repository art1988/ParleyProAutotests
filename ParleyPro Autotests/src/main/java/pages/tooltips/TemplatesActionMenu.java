package pages.tooltips;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.DeleteTemplate;
import forms.TemplateInformation;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TemplatesActionMenu
{
    private String templateName;


    private static Logger logger = Logger.getLogger(TemplatesActionMenu.class);

    public TemplatesActionMenu(String templateName)
    {
        this.templateName = templateName;

        $(".dropdown.open.btn-group ul").waitUntil(Condition.visible, 6_000);
        $$(".dropdown.open.btn-group ul li").shouldHaveSize(3).shouldHave(CollectionCondition.exactTexts("Template Info", "Download", "Delete"));
    }

    public TemplateInformation clickTemplateInfo()
    {
        Selenide.executeJavaScript("$('.dropdown.open.btn-group ul').find('li:contains(\"Template Info\")').find('a')[0].click()");

        logger.info("Template Info menu item was clicked...");

        return new TemplateInformation();
    }

    public void clickDownload() throws FileNotFoundException
    {
        WebElement downloadMenuItemWE = Selenide.executeJavaScript("return $('.dropdown.open.btn-group ul').find('li:contains(\"Download\")').find('a')[0]");
        $(downloadMenuItemWE).download();

        logger.info("Download menu item was clicked...");
    }

    public DeleteTemplate clickDelete()
    {
        Selenide.executeJavaScript("$('.dropdown.open.btn-group ul').find('li:contains(\"Delete\")').find('a')[0].click()");

        logger.info("Delete menu item was clicked...");

        return new DeleteTemplate(templateName);
    }
}
