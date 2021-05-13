package forms.delete;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class DeleteDocument
{
    private SelenideElement deleteButton = $(".button.btn-common.btn.btn-danger");

    private static Logger logger = Logger.getLogger(DeleteDocument.class);


    public DeleteDocument(String documentName)
    {
        $(".modal-body-title").waitUntil(Condition.visible, 6_000)
                .shouldHave(Condition.exactText("Are you sure you want to delete “" + documentName + "”?"));

        $(".modal-body-description").waitUntil(Condition.visible, 6_000)
                .shouldHave(Condition.exactText("The document will be removed from the contract"));
    }

    public void clickDelete()
    {
        deleteButton.click();

        logger.info("Delete button was clicked...");

        $(".modal-content").waitUntil(Condition.disappear, 10_000);
    }
}
