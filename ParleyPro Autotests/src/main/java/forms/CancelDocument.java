package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class CancelDocument
{
    private SelenideElement cancelDocumentButton = $(".button.btn-common.btn.btn-primary");

    private static Logger logger = Logger.getLogger(CancelDocument.class);

    public CancelDocument(String documentName)
    {
        $(".modal-body-title").waitUntil(Condition.visible, 6_000)
                .shouldHave(Condition.exactText("You have asked to cancel document ″" + documentName + "″."));

        $(".modal-body-description").waitUntil(Condition.visible, 6_000)
                .shouldHave(Condition.exactText("Once cancelled, this document will be inactive"));
    }

    public void clickCancel()
    {
        cancelDocumentButton.click();

        logger.info("Cancel Document button was clicked...");
    }
}
