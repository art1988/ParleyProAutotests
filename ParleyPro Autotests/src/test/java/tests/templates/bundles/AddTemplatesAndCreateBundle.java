package tests.templates.bundles;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import forms.add.CreateBundle;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.DashboardPage;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.File;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AddTemplatesAndCreateBundle
{
    private LinkedList<String> uploadedTemplates = new LinkedList<>(); // stores uploaded templates as it was shown on UI
    private CreateBundle createBundleForm;
    private String bundleName = "TEST Bundle AKM";

    private static Logger logger = Logger.getLogger(AddTemplatesAndCreateBundle.class);

    @Test(priority = 1)
    public void addTemplates()
    {
        new DashboardPage().getSideBar()
                           .clickTemplates(true)
                           .clickNewTemplate()
                           .clickUploadTemplatesButton( new File[]{ Const.TEMPLATE_AT48, Const.TEMPLATE_AT77, Const.TEMPLATE_AT86 } );

        $(".notification-stack").waitUntil(Condition.appear, 7_000).shouldHave(Condition.text("Template"));

        logger.info("Assert that number of TEMPLATES become equals 3 ...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('.tab-menu__item:contains(\"TEMPLATES\")').text().endsWith(\"3\")"),
                "Number of TEMPLATES on tab should be equal 3 !!!");

        Screenshoter.makeScreenshot();

        Selenide.refresh(); // to remove 3 notifications

        new WebDriverWait(WebDriverRunner.getWebDriver(), 15).until(webDriver ->
                Selenide.executeJavaScript("return document.readyState").equals("complete"));

        // Wait until table with uploaded templates become visible
        $(".templates-board__list tbody tr").waitUntil(Condition.visible, 7_000);

        Screenshoter.makeScreenshot();

        // Save order of uploaded templates
        $$(".templates-board__list tbody tr .template__title").stream().forEach( elem -> uploadedTemplates.add(elem.getText()));
    }

    @Test(priority = 2)
    public void addBundle()
    {
        createBundleForm = new DashboardPage().getSideBar()
                                              .clickTemplates(false)
                                              .clickBundlesTab()
                                              .clickNewBundle();

        logger.info("Assert that NEXT button is disabled...");
        Assert.assertTrue(Selenide.executeJavaScript("return $('._button.scheme_blue.size_lg').is(':disabled')"),
                "NEXT button should be disabled !!!");

        createBundleForm.setBundleName(bundleName);
        createBundleForm.selectTemplates( new String[]{ FilenameUtils.removeExtension(Const.TEMPLATE_AT48.getName()),
                                                        FilenameUtils.removeExtension(Const.TEMPLATE_AT77.getName()),
                                                        FilenameUtils.removeExtension(Const.TEMPLATE_AT86.getName()),
        });

        logger.info("Assert that NEXT button become enabled...");
        Assert.assertFalse(Selenide.executeJavaScript("return $('._button.scheme_blue.size_lg').is(':disabled')"),
                "NEXT button should be enabled !!!");
        $(".modal-body .template-bundle__footer-left").shouldHave(Condition.exactText("3 templates selected"));
    }

    @Test(priority = 3)
    public void checkInitialOrder_DragNDrop_AndCheckOrderAgain() throws InterruptedException
    {
        Actions actions = new Actions(WebDriverRunner.getWebDriver());
        LinkedList<String> selectedTemplates = new LinkedList<>();

        $$(".template-bundle__item-name").stream().forEach( item -> selectedTemplates.add(item.getText()) );

        Assert.assertTrue( uploadedTemplates.equals(selectedTemplates) , "The order of templates on 'Create bundle form' is wrong !!!");

        String first  = selectedTemplates.get(0),
               second = selectedTemplates.get(1);

        WebElement firstItem  = Selenide.executeJavaScript("return $('.template-bundle__item-name:contains(\"" + first + "\")').parent()[0]"),
                   secondItem = Selenide.executeJavaScript("return $('.template-bundle__item-name:contains(\"" + second + "\")').parent()[0]");

        // Drag n drop first and second items
        $(firstItem).hover();
        Thread.sleep(500);
        actions.clickAndHold($(firstItem)).build().perform();
        Thread.sleep(500);

        $(secondItem).hover();
        actions.moveToElement($(secondItem), 0, +30).build().perform();
        Thread.sleep(500);
        actions.release().build().perform();
        Thread.sleep(500);

        // init selectedTemplates again after drag'n'drop
        selectedTemplates.clear();
        $$(".template-bundle__item-name").stream().forEach( item -> selectedTemplates.add(item.getText()) );

        Assert.assertTrue($$(".template-bundle__item-name").stream().map( item -> item.getText() ).collect(Collectors.toList()).equals(selectedTemplates),
                "Drag'n'Drop of 1rst and 2nd items wasn't successful !!!");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 4)
    public void deleteOneTemplateGoNextAndGoBack()
    {
        createBundleForm.removeTemplate("86");

        $(".modal-body .template-bundle__footer-left").shouldHave(Condition.exactText("2 templates selected"));

        LinkedList<String> templates = new LinkedList<>();
        $$(".template-bundle__item-name").stream().forEach( item -> templates.add(item.getText()) );

        createBundleForm.clickNext().clickBack();

        logger.info("Assert that 2 templates are still selected...");
        $(".modal-body .template-bundle__footer-left").shouldHave(Condition.exactText("2 templates selected"));
        Assert.assertTrue($$(".template-bundle__item-name").stream().map( item -> item.getText() ).collect(Collectors.toList()).equals(templates),
                "The list of templates is different after clicking 'NEXT' and '< BACK' !!!");

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 5)
    public void saveBundle()
    {
        createBundleForm.clickNext()
                        .setRegion("region1")
                        .setDepartment("department1")
                        .setCategory("category1")
                        .setType("type1")
                        .setDescription("Test bundle description.")
                        .togglePublishBundle() // make it unpublished
                        .clickCreate();

        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("Bundle " + bundleName + " was added."));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that bundle was created...");
        $(".templates-board__list tbody .bundle__info").shouldHave(Condition.exactText("TEST Bundle AKM\n2 templates (2 not published)"));
        $(".templates-board__list tbody .template__status").shouldHave(Condition.exactText("Not published"));

        Screenshoter.makeScreenshot();
    }
}
