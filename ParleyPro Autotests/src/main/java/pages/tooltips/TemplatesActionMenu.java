package pages.tooltips;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.TemplateInformation;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TemplatesActionMenu
{
    private static Logger logger = Logger.getLogger(TemplatesActionMenu.class);

    public TemplatesActionMenu()
    {
        $(".dropdown.open.btn-group ul").waitUntil(Condition.visible, 6_000);
        $$(".dropdown.open.btn-group ul li").shouldHaveSize(3).shouldHave(CollectionCondition.exactTexts("Template Info", "Download", "Delete"));
    }

    public TemplateInformation clickTemplateInfo()
    {
        Selenide.executeJavaScript("$('.dropdown.open.btn-group ul').find('li:contains(\"Template Info\")').find('a')[0].click()");

        logger.info("Template Info menu item was clicked...");

        return new TemplateInformation();
    }
}
