package tests.templates.bundles.at144;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import constants.Const;
import forms.add.CreateBundle;
import io.qameta.allure.Description;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.TemplatesPage;
import utils.Screenshoter;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class UploadTemplatesCreateBundlesAndCheck
{
    private TemplatesPage templatesPage;
    private static Logger logger = Logger.getLogger(UploadTemplatesCreateBundlesAndCheck.class);

    @Test(priority = 1)
    public void uploadTemplates()
    {
        templatesPage = new DashboardPage().getSideBar()
                                           .clickTemplates(true);

        templatesPage.clickNewTemplate()
                     .clickUploadTemplatesButton(new File[]{ Const.REGRESSION_DOC_AT141, Const.TEMPLATE_AT135, Const.REGRESSION_IMG_DOC });

        $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.text("Template"));

        logger.info("Checking that templates were added...");
        $$(".template__title").shouldHave(CollectionCondition.size(3))
                                        .shouldHave(CollectionCondition.textsInAnyOrder("image", "dummyAT141", "AT-135_Template_identical"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    public void createBundles()
    {
        String[] bundleNames = {"qwer", "asdf", "qwas"};
        String[][] templatesToPick = {
                                       {"dummyAT141", "image"},
                                       {"AT-135_Template_identical"},
                                       {"AT-135_Template_identical", "dummyAT141", "image"}
                                     };

        for( int i = 0; i < bundleNames.length; i++ )
        {
            CreateBundle createBundle = templatesPage.clickBundlesTab().clickNewBundle();

            createBundle.setBundleName( bundleNames[i] );
            createBundle.selectTemplates( templatesToPick[i] );
            createBundle.clickNext().clickCreate();

            $(".notification-stack").waitUntil(Condition.appear, 15_000).shouldHave(Condition.exactText("Bundle " + bundleNames[i] + " was added."));
            $(".notification-stack").waitUntil(Condition.disappear, 25_000);
        }

        logger.info("Checking that bundles were added...");
        $$(".bundle__info").shouldHave(CollectionCondition.size(3))
                                     .shouldHave(CollectionCondition.textsInAnyOrder("asdf\n1 template (1 not published)", "qwas\n3 templates (3 not published)", "qwer\n2 templates (2 not published)"));

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 3)
    @Description("This test searches bundle by the name 'qwer' and checks that only one is found and no grey screen happened.")
    public void searchBundles()
    {
        templatesPage.clickBundlesTab().search("qwer");

        logger.info("Checking that only 'qwer' bundle was found and no grey screen happened...");
        $$(".bundle__info").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("qwer\n2 templates (2 not published)"));
        $(".page-head__left").shouldBe(Condition.visible).shouldHave(Condition.exactText("Templates"));
        $(".templates-board__list").shouldBe(Condition.visible);

        Screenshoter.makeScreenshot();

        // clear search input...
        $(".templates-search__close").click();

        // ...and check again that all 3 bundles are on place
        $$(".bundle__info").shouldHave(CollectionCondition.size(3))
                                     .shouldHave(CollectionCondition.textsInAnyOrder("asdf\n1 template (1 not published)", "qwas\n3 templates (3 not published)", "qwer\n2 templates (2 not published)"));
    }
}
