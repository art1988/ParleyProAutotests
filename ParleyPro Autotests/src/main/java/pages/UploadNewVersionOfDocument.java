package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

/**
 * Form that appears after clicking on 'Upload new version' button
 */
public class UploadNewVersionOfDocument
{
    private SelenideElement title = $(".modal-body-title");

    private static Logger logger = Logger.getLogger(UploadNewVersionOfDocument.class);


    public UploadNewVersionOfDocument(String documentName)
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Upload a new version of document \"" + documentName + "\"."));
        $(".js-upload-cp-document-btn").waitUntil(Condition.visible, 15_000);

        try { Thread.sleep(1_000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public DocumentComparePreview clickUploadCounterpartyDocument(File fileToUpload, String documentName, String contractName)
    {
        try { Thread.sleep(1_000); } catch (InterruptedException e) { e.printStackTrace(); }

        try
        {
            $(".js-upload-cp-document-btn").waitUntil(Condition.visible, 7_000);
            $(".js-upload-cp-document-btn").waitUntil(Condition.enabled, 7_000);

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
            Thread.sleep(2_000);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        return new DocumentComparePreview(documentName, contractName);
    }

    /**
     * Important! : Green button 'Upload my team document' may not be present on this form.
     * To make it available, issue setDomainConfig('<domain_name>')
     * @param fileToUpload
     */
    public DocumentComparePreview clickUploadMyTeamDocument(File fileToUpload, String documentName, String contractName)
    {
        $(".js-upload-my-team-document-btn").shouldBe(Condition.enabled).shouldBe(Condition.visible);

        // 1. make <input> visible
        Selenide.executeJavaScript("$('.js-upload-my-team-document-btn').parent().parent().find(\"input\").css(\"height\",\"auto\")");
        Selenide.executeJavaScript("$('.js-upload-my-team-document-btn').parent().parent().find(\"input\").css(\"visibility\",\"visible\")");
        Selenide.executeJavaScript("$('.js-upload-my-team-document-btn').parent().parent().find(\"input\").css(\"display\",\"block\")");

        // 2. trying to upload...
        SelenideElement uploadMyTeamDocumentButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

        uploadMyTeamDocumentButton.uploadFile(fileToUpload);

        return new DocumentComparePreview(documentName, contractName);
    }
}
