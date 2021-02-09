package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import forms.UploadDocumentDetectedChanges;
import org.apache.log4j.Logger;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * This class represents Add Documents page with 2 upload buttons:
 * Upload my team documents - green button
 * and
 * Upload Counterparty documents - purple button
 */
public class AddDocuments
{

    private static Logger logger = Logger.getLogger(AddDocuments.class);


    public AddDocuments()
    {
        $(".documents-add__title").waitUntil(Condition.visible, 7_000);

        isInit();
    }

    // void cuz shouldHave() methods have inbuilt assertions
    private void isInit()
    {
        // Check title
        $(".documents-add__title").shouldHave(Condition.exactText("Add Documents"));

        // Check tabs
        $$(".tab-menu span").shouldHave(CollectionCondition.size(3)).shouldHave(CollectionCondition.texts("Upload document", "Select template", "Upload attachment"));

        // Check upload buttons
        $$(".upload__button").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.texts("Upload my team documents", "Upload Counterparty documents"));
    }

    public void clickUploadDocumentTab()
    {
        $(".js-upload-document-tab").waitUntil(Condition.visible, 7_000).click();

        logger.info("'Upload document' tab was selected");
    }

    public AddDocuments clickSelectTemplateTab()
    {
        $(".js-select-template-tab").waitUntil(Condition.visible, 7_000).click();
        $(".spinner").waitUntil(Condition.disappear, 7_000);

        logger.info("'Select template' tab was selected");

        return this;
    }

    public void clickUploadAttachmentTab()
    {
        $(".js-upload-attachment-tab").waitUntil(Condition.visible, 7_000).click();

        logger.info("'Upload attachment' tab was selected");
    }

    /**
     * Click 'Upload my team documents' button
     * @param fileToUpload
     */
    public void clickUploadMyTeamDocuments(File fileToUpload)
    {
        // 1. make <input> visible
        Selenide.executeJavaScript("$('.js-upload-my-team-document-btn').parent().parent().find(\"input\").css(\"height\",\"auto\")");
        Selenide.executeJavaScript("$('.js-upload-my-team-document-btn').parent().parent().find(\"input\").css(\"visibility\",\"visible\")");
        Selenide.executeJavaScript("$('.js-upload-my-team-document-btn').parent().parent().find(\"input\").css(\"display\",\"block\")");

        // 2. trying to upload...
        SelenideElement uploadMyTeamDocumentsButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

        uploadMyTeamDocumentsButton.uploadFile(fileToUpload);
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
        // 1. make <input> visible
        Selenide.executeJavaScript("$('.js-upload-cp-document-btn').parent().parent().find(\"input\").css(\"height\",\"auto\")");
        Selenide.executeJavaScript("$('.js-upload-cp-document-btn').parent().parent().find(\"input\").css(\"visibility\",\"visible\")");
        Selenide.executeJavaScript("$('.js-upload-cp-document-btn').parent().parent().find(\"input\").css(\"display\",\"block\")");

        // 2. trying to upload...
        SelenideElement uploadCounterpartyDocumentsButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

        uploadCounterpartyDocumentsButton.uploadFile(fileToUpload);
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
     * Select template from list in active Select template tab. Select template tab should be active.
     * @param templateName name of template
     */
    public OpenedContract selectTemplate(String templateName)
    {
        Selenide.executeJavaScript("$('.documents-add-templates__list .documents-add-templates-item__title:contains(\"" + templateName + "\")').click()");

        logger.info(templateName + " was selected...");

        return new OpenedContract();
    }
}
