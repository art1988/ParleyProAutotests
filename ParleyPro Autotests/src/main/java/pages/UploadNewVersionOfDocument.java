package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

public class UploadNewVersionOfDocument
{
    private SelenideElement title = $(".modal-body-title");


    public UploadNewVersionOfDocument(String documentName)
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Upload a new version of document \"" + documentName + "\"."));
        $(".upload__title").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Counterparty document"));
        $(".upload__caption").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("All redlines will be created on behalf of the counterparty"));
    }

    public DocumentComparePreview clickUploadCounterpartyDocument(File fileToUpload, String documentName, String contractName)
    {
        // 1. make <input> visible
        Selenide.executeJavaScript("$('.js-upload-cp-document-btn').parent().parent().find(\"input\").css(\"height\",\"auto\")");
        Selenide.executeJavaScript("$('.js-upload-cp-document-btn').parent().parent().find(\"input\").css(\"visibility\",\"visible\")");
        Selenide.executeJavaScript("$('.js-upload-cp-document-btn').parent().parent().find(\"input\").css(\"display\",\"block\")");

        // 2. trying to upload...
        SelenideElement uploadCounterpartyDocumentsButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

        uploadCounterpartyDocumentsButton.uploadFile(fileToUpload);

        return new DocumentComparePreview(documentName, contractName);
    }
}
