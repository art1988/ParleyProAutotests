package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import pages.DocumentComparePreview;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represent form that appears after clicking on 'Upload document' menu item via context menu of the document
 */
public class UploadDocument
{
    private String documentName;

    public UploadDocument(String documentName)
    {
        this.documentName = documentName;

        $(".modal-body-title").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Upload \"" + documentName + "\" document."));
        $(".upload__icon").waitUntil(Condition.visible, 7_000);
        $(".button.btn-common.btn-internal.undefined.btn.btn-default").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Upload document"));
    }

    public DocumentComparePreview clickUploadDocumentButton(File fileToUpload)
    {
        // 1. make <input> visible
        Selenide.executeJavaScript("$('.upload__body input').css(\"height\",\"auto\")");
        Selenide.executeJavaScript("$('.upload__body input').css(\"visibility\",\"visible\")");
        Selenide.executeJavaScript("$('.upload__body input').css(\"display\",\"block\")");

        // 2. trying to upload...
        SelenideElement uploadCounterpartyDocumentsButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

        uploadCounterpartyDocumentsButton.uploadFile(fileToUpload);

        return new DocumentComparePreview(documentName);
    }
}
