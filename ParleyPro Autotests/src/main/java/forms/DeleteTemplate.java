package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class DeleteTemplate
{
    private SelenideElement deleteButton = $(".button.btn-common.btn.btn-danger");


    private static Logger logger = Logger.getLogger(DeleteTemplate.class);

    public DeleteTemplate(String templateName)
    {
        $(".modal-body-title").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Are you sure you want to delete \"" + templateName + "\"?"));
        deleteButton.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("DELETE"));
    }

    public void clickDelete()
    {
        deleteButton.click();

        logger.info("DELETE button was clicked...");
    }
}
