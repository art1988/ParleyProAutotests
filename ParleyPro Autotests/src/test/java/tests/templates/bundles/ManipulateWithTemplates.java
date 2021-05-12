package tests.templates.bundles;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import forms.add.CreateBundle;
import org.apache.log4j.Logger;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.TemplatesPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class ManipulateWithTemplates
{
    private static Logger logger = Logger.getLogger(ManipulateWithTemplates.class);

    @Test(priority = 1)
    public void publishAllTemplates() throws InterruptedException
    {
        TemplatesPage templatesPage = new DashboardPage().getSideBar().clickTemplates(false);

        for( int i = 0; i < $$(".templates-board__list tbody .template__title").size(); i++ )
        {
            Selenide.executeJavaScript("$('.templates-board__list tbody .template__title').eq(" + i + ").find(\".template__belongs\").detach()"); // remove counter near with title
            templatesPage.selectTemplate($$(".templates-board__list tbody .template__title").get(i).getText()).clickPublishButton(); // selects and publish
            Thread.sleep(500);
        }

        logger.info("Assert that all templates become published...");
        $$(".templates-board__list tbody .template__status").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.exactTexts("Published", "Published", "Published"));

        templatesPage.clickBundlesTab();

        logger.info("Assert that unpublished template label is gone...");
        $(".templates-board__list tbody .bundle__info").shouldHave(Condition.exactText("TEST Bundle AKM\n2 templates "));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void addThirdTemplateAndPublishBundle()
    {
        CreateBundle editBundleForm = new DashboardPage().getSideBar()
                                                         .clickTemplates(false)
                                                         .clickBundlesTab()
                                                         .clickActionMenuBundle("TEST Bundle AKM")
                                                         .clickBundleInfo();

        // Adding third template to the bundle
        editBundleForm.selectTemplates(new String[]{"Template_AT-86_text_cut_off"});

        // Saving order of templates
        new Saver($$(".template-bundle__item-name").stream().map( item -> item.getText() ).collect(Collectors.toList()));

        logger.info("Assert that all templates are selected...");
        $$(".template-bundle__item-name").shouldHave(CollectionCondition.size(3))
                .shouldHave(CollectionCondition.textsInAnyOrder("Template_AT48", "Template_AT-86_text_cut_off", "Template_AT-77_dummy"));

        editBundleForm.clickInformationTab()
                      .togglePublishBundle()
                      .clickSave();
        logger.info("Assert that status become Published...");
        $(".templates-board__list tbody .template__status").shouldHave(Condition.exactText("Published"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    public void hoverOverBundleIcons()
    {
        new DashboardPage().getSideBar().clickTemplates(false).clickTemplatesTab();

        logger.info("Assert that all 3 tooltips have bundle name in it...");
        $$(".template__belongs").shouldHave(CollectionCondition.size(3));
        for( int i = 0; i < $$(".template__belongs").size(); i++ )
        {
            $$(".template__belongs").get(i).hover();
            $(".rc-tooltip-inner").waitUntil(Condition.visible, 7_000);
            $(".rc-tooltip-inner .template-depends__bundle").shouldHave(Condition.exactText("TEST Bundle AKM"));
        }

        $$(".template__belongs").first().hover();
        $(".rc-tooltip-inner").waitUntil(Condition.visible, 7_000);
        Selenide.executeJavaScript("$('.rc-tooltip-inner a')[0].click()");

        CreateBundle createBundleForm = new CreateBundle(true, "TEST Bundle AKM");
        createBundleForm.clickCancel();
    }
}
