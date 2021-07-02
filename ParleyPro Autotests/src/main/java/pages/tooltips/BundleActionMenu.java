package pages.tooltips;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.add.CreateBundle;
import forms.delete.DeleteBundle;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BundleActionMenu
{
    private String bundleName;
    private static Logger logger = Logger.getLogger(BundleActionMenu.class);


    public BundleActionMenu(String bundleName)
    {
        this.bundleName = bundleName;

        $(".dropdown.open.btn-group ul").waitUntil(Condition.visible, 6_000);
        $$(".dropdown.open.btn-group ul li").shouldHaveSize(2).shouldHave(CollectionCondition.exactTexts("Bundle info", "Delete"));
    }

    public CreateBundle clickBundleInfo()
    {
        Selenide.executeJavaScript("$('.dropdown.open.btn-group ul').find('li:contains(\"Bundle info\")').find('a')[0].click()");

        logger.info("Bundle info menu item was clicked...");

        return new CreateBundle(true, bundleName);
    }

    public DeleteBundle clickDelete()
    {
        Selenide.executeJavaScript("$('.dropdown.open.btn-group ul').find('li:contains(\"Delete\")').find('a')[0].click()");

        logger.info("Delete menu item was clicked for bundle: " + bundleName);

        return new DeleteBundle(bundleName);
    }
}
