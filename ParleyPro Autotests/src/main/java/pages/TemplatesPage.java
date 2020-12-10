package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import java.io.File;

import static com.codeborne.selenide.Selenide.$;

public class TemplatesPage
{

    /**
     * @param isBlank if true then no any template records were added.
     */
    public TemplatesPage(boolean isBlank)
    {
        // Assert page header
        $(".page-head__cell.page-head__left").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Templates"));

        if( isBlank )
        {
            // Assert that title is present
            $(".templates-upload__head").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Add Templates"));

            // Assert that button is present
            $(".button.btn-common.btn-internal.undefined.btn.btn-default").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Upload templates"));
        }
        else
        {
            $(".spinner").waitUntil(Condition.disappear, 10_000);

            // Assert that heading of table is visible
            $(".templates-board__list thead").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Template nameStatusLast activity"));
        }
    }

    public void clickUploadTemplatesButton(File fileToUpload)
    {
        // 1. make <input> visible
        Selenide.executeJavaScript("$('.upload__body input').css(\"height\",\"auto\")");
        Selenide.executeJavaScript("$('.upload__body input').css(\"visibility\",\"visible\")");
        Selenide.executeJavaScript("$('.upload__body input').css(\"display\",\"block\")");

        // 2. trying to upload...
        SelenideElement uploadCounterpartyDocumentsButton = $(".upload__body input[style='display: block; height: auto; visibility: visible;']");

        uploadCounterpartyDocumentsButton.uploadFile(fileToUpload);
    }

    /**
     * Click by template by templateName
     * @param templateName
     */
    public EditTemplatePage selectTemplate(String templateName)
    {
        Selenide.executeJavaScript("$('.templates-board tbody .template__title:contains(\"" + templateName + "\")').click()");

        return new EditTemplatePage();
    }

    /**
     * Invoke action menu by clicking of 3 dotted button for given template name
     * @param templateName
     */
    public void clickActionMenu(String templateName)
    {

    }
}
