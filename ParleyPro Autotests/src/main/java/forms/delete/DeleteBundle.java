package forms.delete;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class DeleteBundle
{
    private static Logger logger = Logger.getLogger(DeleteBundle.class);

    public DeleteBundle(String bundleName)
    {
        $(".modal-body-title").waitUntil(Condition.visible, 10_000)
                .shouldHave(Condition.exactText("Are you sure to delete \"" + bundleName + "\"?"));
    }

    public void clickDelete()
    {
        $(".button.btn-common.btn.btn-danger").click();

        logger.info("DELETE button was clicked.");
    }
}
