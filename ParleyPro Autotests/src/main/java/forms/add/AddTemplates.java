package forms.add;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

/**
 * Represents Add Templates form with one button 'Upload templates'
 */
public class AddTemplates
{
    public AddTemplates()
    {
        $(".templates-upload__head").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Add Templates"));
        $(".upload__title").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Start uploading your templates"));
        $(".button.btn-common.btn-internal.undefined.btn.btn-default").shouldBe(Condition.visible).shouldBe(Condition.enabled);
    }

    public void clickUploadTemplatesButton(File fileToUpload)
    {
        // 1. make <input> visible
        Selenide.executeJavaScript("$('.button.btn-common.btn-internal.undefined.btn.btn-default').parent().parent().find(\"input\").css(\"height\",\"auto\")");
        Selenide.executeJavaScript("$('.button.btn-common.btn-internal.undefined.btn.btn-default').parent().parent().find(\"input\").css(\"visibility\",\"visible\")");
        Selenide.executeJavaScript("$('.button.btn-common.btn-internal.undefined.btn.btn-default').parent().parent().find(\"input\").css(\"display\",\"block\")");

        // 2. trying to upload...
        SelenideElement uploadTemplatesButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

        uploadTemplatesButton.uploadFile(fileToUpload);

        $(".templates-upload").waitUntil(Condition.disappear, 15_000);
    }
}
