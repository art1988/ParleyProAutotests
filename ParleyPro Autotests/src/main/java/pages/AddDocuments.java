package pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AddDocuments
{

    public AddDocuments()
    {
        $(".documents-add__title").waitUntil(Condition.visible, 5_000);

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
}
