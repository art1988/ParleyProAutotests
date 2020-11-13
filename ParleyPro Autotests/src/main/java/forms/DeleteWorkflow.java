package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class DeleteWorkflow
{
    private SelenideElement title        = $(".modal-title");
    private SelenideElement deleteButton = $("button[type='submit']");


    private static Logger logger = Logger.getLogger(DeleteWorkflow.class);

    public DeleteWorkflow(String workflowName)
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Are you sure you want to delete workflow “" + workflowName + "”?"));
        $(".modal-description").shouldHave(Condition.exactText("The workflow will be removed from the contracts"));
    }

    public void clickDelete()
    {
        deleteButton.click();

        logger.info("Delete button was clicked");
    }
}
