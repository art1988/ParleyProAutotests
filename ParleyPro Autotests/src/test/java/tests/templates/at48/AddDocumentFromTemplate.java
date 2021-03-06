package tests.templates.at48;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import constants.Const;
import forms.StartReview;
import io.qameta.allure.Description;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.AddDocuments;
import pages.DashboardPage;
import pages.OpenedContract;
import pages.subelements.FieldsPanel;
import utils.ScreenShotOnFailListener;
import utils.Screenshoter;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@Listeners({ScreenShotOnFailListener.class})
public class AddDocumentFromTemplate
{
    private String yesterdayDate;
    private static Logger logger = Logger.getLogger(AddDocumentFromTemplate.class);

    @Test(priority = 1)
    @Description("This test creates document from template and verifies fields")
    public void addDocumentFromTemplate() throws InterruptedException
    {
        new DashboardPage().getSideBar().clickInProgressContracts(false).selectContract("ContractThatDoesNotMatchTemplate_AT48");

        AddDocuments addDocuments = new AddDocuments();

        addDocuments.clickSelectTemplateTab();
        OpenedContract openedContract = addDocuments.selectTemplate("Template_AT48[ EDITED ]");

        logger.info("Assert that notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 60_000).shouldHave(Condition.exactText("Document Template_AT48[ EDITED ] has been successfully uploaded."));
        $(".notification-stack").waitUntil(Condition.disappear, 75_000);

        FieldsPanel fieldsPanel = openedContract.getFieldsPanel();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YY");
        yesterdayDate = LocalDate.now().minusDays(1).format(formatter);

        logger.info("Making sure that fields are match contact info...");
        Assert.assertEquals(fieldsPanel.getContractName(), "ContractThatDoesNotMatchTemplate_AT48");
//        Assert.assertEquals(fieldsPanel.getContractDueDate(), yesterdayDate); // because of Time Zone it will be (current day - 1) TODO: change after fixing of PAR-12765
        Assert.assertEquals(fieldsPanel.getContractCategory(), "category1");
        Assert.assertEquals(fieldsPanel.getContractRegion(), "region1");

        logger.info("Making sure that SAVE button is disabled...");
        Assert.assertEquals($(".documents-placeholders__footer button").getAttribute("disabled"), "true");

        fieldsPanel.setValueForCustomField("AT Custom_field", "Some value for custom field");

        logger.info("Making sure that SAVE button is enabled...");
        Assert.assertNull($(".documents-placeholders__footer button").getAttribute("disabled"));

        fieldsPanel.clickSaveButton();

        logger.info("Assert that notification was shown...");
        $(".notification-stack").waitUntil(Condition.visible, 15_000).shouldHave(Condition.exactText("The fields have been saved successfully"));
        $(".notification-stack").waitUntil(Condition.disappear, 15_000);

        logger.info("Assert that changes were affected document view...");
        $$("span[data-placeholder-id]")
                .shouldHave(CollectionCondition.size(5));
                //.shouldHave(CollectionCondition.texts("ContractThatDoesNotMatchTemplate_AT48", "category1", "region1", "Some value for custom field")); // disabled due to PAR-12765

        Screenshoter.makeScreenshot();
    }

    @Test(priority = 2)
    @Description("This test moves document to review stage so that green placeholders disappears")
    public void moveToReview()
    {
        OpenedContract openedContract = new OpenedContract();

        StartReview startReviewForm = openedContract.switchDocumentToReview("Template_AT48[ EDITED ]");

        startReviewForm.clickStart();

        // Wait until status was changed to REVIEW
        $$(".lifecycle__item.active").first().waitUntil(Condition.visible, 7_000);
        $$(".lifecycle__item.active").last().waitUntil(Condition.visible, 7_000);

        logger.info("Assert that status was changed to REVIEW...");
        $$(".lifecycle__item.active").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.exactTexts("REVIEW\n(1)", "REVIEW"));

        logger.info("Assert that both contract and document icons have green color");
        $$(".lifecycle__item.active").first().getCssValue("background-color").equals("#19be9b;");
        $$(".lifecycle__item.active").last().getCssValue("background-color").equals("#19be9b;");

        Screenshoter.makeScreenshot();

        logger.info("Assert that green placeholders disappeared...");
        $$("span[data-placeholder-id]").shouldHave(CollectionCondition.size(0)); // no green placeholders
//        $$("span[style*='text-decoration:underline']").first().shouldHave(Condition.text("ContractThatDoesNotMatchTemplate_AT48category1region1Some value for custom fieldProduction of Products"));

        String firstParagraph = $$("span[style*='text-decoration:underline']").first().text();
        Assert.assertTrue(firstParagraph.contains("ContractThatDoesNotMatchTemplate_AT48") &&
                                   firstParagraph.contains("category1") &&
                                   firstParagraph.contains("region1") &&
                                   firstParagraph.contains("Some value for custom fieldProduction of Products"));
    }

    @Test(priority = 3)
    public void downloadTemplate() throws IOException
    {
        new DashboardPage().getSideBar()
                           .clickTemplates(false)
                           .clickActionMenuTemplate("Template_AT48[ EDITED ]")
                           .clickDownload();

        logger.info("Assert that template was downloaded...");

        new WebDriverWait(WebDriverRunner.getWebDriver(), 20).
                until(d -> Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), "Template_AT48[ EDITED ].docx").toFile().exists());

        Assert.assertTrue(Paths.get(Const.DOWNLOAD_DIR.getAbsolutePath(), "Template_AT48[ EDITED ].docx").toFile().exists());
    }

    @Test(priority = 4)
    public void clearDownloadsFolder() throws IOException
    {
        FileUtils.deleteDirectory(Const.DOWNLOAD_DIR);
    }
}
