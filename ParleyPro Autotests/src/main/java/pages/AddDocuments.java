package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.UploadDocumentDetectedChanges;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * This class represents Add Documents page.
 * The amounts of buttons may vary.
 * For example, for 'In-progress contracts' page it may have:
 * Upload my team documents - green button and Upload Counterparty documents - purple button.
 * For 'Executed contracts' page it may have only 'Upload executed documents' button - blue one.
 */
public class AddDocuments
{
    private static Logger logger = Logger.getLogger(AddDocuments.class);

    public AddDocuments()
    {
        $(".documents-add__title").waitUntil(Condition.visible, 25_000);
        $(".spinner").waitUntil(Condition.disappear, 15_000);
        try { Thread.sleep(1_000); } catch (InterruptedException e) { e.printStackTrace(); }

        Assert.assertTrue( isInit(), "Looks like that Add Documents page wasn't loaded correctly !");
    }

    private boolean isInit()
    {
        $(".documents-add__title").shouldHave(Condition.exactText("Add Documents")); // Check title

        if( $$(".tab-menu span").size() == 3 ) // For 'In-progress contracts' page
        {
            $$(".tab-menu span").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.texts("Upload document", "Select template", "Upload attachment")); // check tabs
            $$(".upload__button").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.texts("Upload my team documents", "Upload Counterparty documents")); // check buttons
        }
        else if( $$(".tab-menu span").size() == 2 ) // For 'Executed contracts' page
        {
            $$(".tab-menu span").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.texts("Upload executed", "Upload attachment")); // check tabs
            $$(".upload__button").shouldHave(CollectionCondition.size(1)).shouldHave(CollectionCondition.exactTexts("Upload executed documents")); // check buttons
        }
        else
        {
            return false;
        }

        return true;
    }

    public String getContractTitle()
    {
        return $(".contract-header__name").getText();
    }

    public void clickUploadDocumentTab()
    {
        $(".js-upload-document-tab").waitUntil(Condition.visible, 7_000).click();

        logger.info("'Upload document' tab was selected");
    }

    public AddDocuments clickSelectTemplateTab()
    {
        $(".js-select-template-tab").waitUntil(Condition.visible, 7_000).click();
        $(".spinner").waitUntil(Condition.disappear, 30_000);

        logger.info("'Select template' tab was selected");

        return this;
    }

    public AddDocuments clickUploadAttachmentTab()
    {
        $(".js-upload-attachment-tab").waitUntil(Condition.visible, 7_000).click();

        logger.info("'Upload attachment' tab was selected");

        return this;
    }

    /**
     * Click 'Upload my team documents' button
     * @param fileToUpload
     */
    public void clickUploadMyTeamDocuments(File fileToUpload)
    {
        try
        {
            // 1. make <input> visible
            Selenide.executeJavaScript("$('.js-upload-my-team-document-btn').parent().parent().find(\"input\").css(\"height\",\"auto\")");
            Thread.sleep(200);
            Selenide.executeJavaScript("$('.js-upload-my-team-document-btn').parent().parent().find(\"input\").css(\"visibility\",\"visible\")");
            Thread.sleep(200);
            Selenide.executeJavaScript("$('.js-upload-my-team-document-btn').parent().parent().find(\"input\").css(\"display\",\"block\")");
            Thread.sleep(200);

            $(".js-upload-my-team-document-btn").parent().parent().find("input").waitUntil(Condition.visible, 7_000);
            $(".js-upload-my-team-document-btn").parent().parent().find("input").waitUntil(Condition.enabled, 7_000);

            Thread.sleep(1_000); // necessary sleep ! Without this sleep test may become flaky.

            // 2. trying to upload...
            SelenideElement uploadMyTeamDocumentsButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

            uploadMyTeamDocumentsButton.shouldBe(Condition.visible).shouldBe(Condition.enabled).uploadFile(fileToUpload);
            Thread.sleep(2_000); // this sleep after firing of uploadFile is necessary too
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }
    }

    /**
     * Upload document with revisions
     * @param fileToUpload
     * @return
     */
    public UploadDocumentDetectedChanges clickUploadMyTeamDocumentsWithDetectedChanges(File fileToUpload)
    {
        // 1. make <input> visible
        Selenide.executeJavaScript("$('.js-upload-my-team-document-btn').parent().parent().find(\"input\").css(\"height\",\"auto\")");
        Selenide.executeJavaScript("$('.js-upload-my-team-document-btn').parent().parent().find(\"input\").css(\"visibility\",\"visible\")");
        Selenide.executeJavaScript("$('.js-upload-my-team-document-btn').parent().parent().find(\"input\").css(\"display\",\"block\")");

        // 2. trying to upload...
        SelenideElement uploadMyTeamDocumentsButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

        uploadMyTeamDocumentsButton.uploadFile(fileToUpload);

        return new UploadDocumentDetectedChanges();
    }

    /**
     * Click 'Upload counterparty documents' button
     * @param fileToUpload
     */
    public void clickUploadCounterpartyDocuments(File fileToUpload)
    {
        try
        {
            // 1. make <input> visible
            Selenide.executeJavaScript("$('.js-upload-cp-document-btn').parent().parent().find(\"input\").css(\"height\",\"auto\")");
            Thread.sleep(200);
            Selenide.executeJavaScript("$('.js-upload-cp-document-btn').parent().parent().find(\"input\").css(\"visibility\",\"visible\")");
            Thread.sleep(200);
            Selenide.executeJavaScript("$('.js-upload-cp-document-btn').parent().parent().find(\"input\").css(\"display\",\"block\")");
            Thread.sleep(200);

            $(".js-upload-cp-document-btn").parent().parent().find("input").waitUntil(Condition.visible, 7_000);
            $(".js-upload-cp-document-btn").parent().parent().find("input").waitUntil(Condition.enabled, 7_000);

            Thread.sleep(1_000);

            // 2. trying to upload...
            SelenideElement uploadCounterpartyDocumentsButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

            uploadCounterpartyDocumentsButton.shouldBe(Condition.visible).shouldBe(Condition.enabled).uploadFile(fileToUpload);
            Thread.sleep(2_000); // this sleep after firing of uploadFile is necessary too
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }
    }

    /**
     * Click 'UPLOAD FROM YOUR COMPUTER' button on 'Upload attachment' tab
     * @param filesToUpload
     */
    public void clickUploadFromYourComputer(File[] filesToUpload)
    {
        // 1. make <input> visible
        Selenide.executeJavaScript("$('.upload__button button').parent().parent().find(\"input\").css(\"height\",\"auto\")");
        Selenide.executeJavaScript("$('.upload__button button').parent().parent().find(\"input\").css(\"visibility\",\"visible\")");
        Selenide.executeJavaScript("$('.upload__button button').parent().parent().find(\"input\").css(\"display\",\"block\")");

        // 2. trying to upload...
        SelenideElement uploadFromYourComputerButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

        uploadFromYourComputerButton.uploadFile(filesToUpload);
    }

    /**
     * Click 'Upload executed documents' button. This button is available on 'Executed contracts' page
     * @param fileToUpload
     */
    public void clickUploadExecutedDocuments(File fileToUpload)
    {
        // 1. make <input> visible
        Selenide.executeJavaScript("$('.upload__button button').parent().parent().find(\"input\").css(\"height\",\"auto\")");
        Selenide.executeJavaScript("$('.upload__button button').parent().parent().find(\"input\").css(\"visibility\",\"visible\")");
        Selenide.executeJavaScript("$('.upload__button button').parent().parent().find(\"input\").css(\"display\",\"block\")");

        // 2. trying to upload...
        SelenideElement uploadExecutedDocumentsButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

        uploadExecutedDocumentsButton.uploadFile(fileToUpload);
    }

    /**
     * Select template ( or bundle ) from list in active Select template tab. Select template tab should be active.
     * @param templateName name of template
     */
    public OpenedContract selectTemplate(String templateName)
    {
        Selenide.executeJavaScript("$('.documents-add-templates__list .documents-add-templates-item__title:contains(\"" + templateName + "\")').click()");

        logger.info(templateName + " was selected...");

        return new OpenedContract();
    }
}
