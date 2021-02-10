package forms.delete;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class DeleteContract
{
    private SelenideElement title = $(".modal-body-title");



    private static Logger logger = Logger.getLogger(DeleteContract.class);

    public DeleteContract(String contractName)
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Are you sure you want to delete contract “" + contractName + "”?"));
        $(".modal-body-description").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("The contract and contract documents will be permanently removed."));
    }

    public void clickDelete()
    {
        Selenide.executeJavaScript("$('.modal-footer button:contains(\"DELETE\")').click()");

        logger.info("Delete button was clicked");
    }
}
