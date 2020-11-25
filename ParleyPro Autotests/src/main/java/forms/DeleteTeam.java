package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class DeleteTeam
{
    private SelenideElement title = $(".modal-body-title");
    private SelenideElement deleteButton = $(".button.btn-common.btn.btn-danger");


    private static Logger logger = Logger.getLogger(DeleteTeam.class);

    public DeleteTeam()
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Delete team"));
        $(".modal-body-description").shouldHave(Condition.exactText("Are you sure to delete team"));
    }

    public void clickDelete()
    {
        deleteButton.click();

        logger.info("Delete button was clicked...");

        title.waitUntil(Condition.disappear, 6_000);
    }
}
