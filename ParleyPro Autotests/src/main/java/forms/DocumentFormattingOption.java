package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import pages.EditDocumentPage;

import static com.codeborne.selenide.Selenide.$;

public class DocumentFormattingOption
{
    private SelenideElement title = $(".modal-body-title"); // title of form
    private String documentName;


    private static Logger logger = Logger.getLogger(DocumentFormattingOption.class);

    public DocumentFormattingOption(String documentName)
    {
        this.documentName = documentName;

        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Select document formatting option."));
        $(".modal-footer").waitUntil(Condition.visible, 7_000);
    }

    public EditDocumentPage clickOnline()
    {
        Selenide.executeJavaScript("$('.modal-footer button:contains(\"Online\")').click()");

        logger.info("Online was clicked...");

        return new EditDocumentPage(documentName, true);
    }
}
