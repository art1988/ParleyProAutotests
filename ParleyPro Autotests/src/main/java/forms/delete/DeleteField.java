package forms.delete;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Popup that appears after clicking on garbage can for field on Fields page
 */
public class DeleteField
{
    private SelenideElement deleteButton = $(".button.btn-common.btn.btn-danger");

    private static Logger logger = Logger.getLogger(DeleteField.class);

    public DeleteField()
    {
        $(".modal-body-title").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Are you sure you want to delete this field?"));

        $(".modal-body-description").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("The field will be removed from all contracts"));

        deleteButton.shouldBe(Condition.visible).shouldBe(Condition.enabled);
    }

    public void clickDelete()
    {
        deleteButton.click();

        logger.info("DELETE button was clicked");

        $(".modal-content").waitUntil(Condition.disappear, 7_000);
    }
}
