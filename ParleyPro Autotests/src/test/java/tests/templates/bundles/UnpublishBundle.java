package tests.templates.bundles;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.TemplatesPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class UnpublishBundle
{
    private static Logger logger = Logger.getLogger(UnpublishBundle.class);

    @Test(priority = 1)
    public void unpublishBundle()
    {
        Selenide.refresh();

        logger.info("Unpublishing bundle...");

        new DashboardPage().getSideBar()
                           .clickTemplates(false)
                           .clickBundlesTab()
                           .selectBundle("TEST Bundle AKM")
                           .clickInformationTab()
                           .togglePublishBundle()
                           .clickSave();

        new DashboardPage().getSideBar()
                           .clickInProgressContracts(false)
                           .selectContract("Unmatched settings for bundle");

        new AddDocuments().clickSelectTemplateTab();

        logger.info("Making sure that there is no bundle in the list...");
        $$(".documents-add-templates-item__title").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.textsInAnyOrder("Template_AT-86_text_cut_off", "Template_AT-77_dummy", "Template_AT48"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void deleteOneTemplate()
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(false);

        templatesPage.clickTemplatesTab()
                     .clickActionMenuTemplate("Template_AT48")
                     .clickDelete().clickDelete();

        logger.info("Check counter on Templates tab...");
        $(".tab-menu__item.selected_yes").shouldHave(Condition.exactText("TEMPLATES 2"));

        templatesPage.clickBundlesTab();

        logger.info("Check counter for bundle...");
        $(".bundle__info-templates").waitUntil(Condition.text("2 templates"), 7_000);
        $(".bundle__info-templates").shouldHave(Condition.text("2 templates"));

        Screenshoter.makeScreenshot();
    }
}
