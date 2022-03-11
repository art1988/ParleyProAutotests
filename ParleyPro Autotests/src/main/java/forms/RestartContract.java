package forms;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class RestartContract
{
    private static Logger logger = Logger.getLogger(RestartContract.class);


    public RestartContract()
    {
        $(".modal-body-title").shouldBe(Condition.visible).shouldHave(Condition.text("You have asked to restart contract"));

        $$(".modal-content button").shouldHave(CollectionCondition.size(2));
    }

    public void clickRestart()
    {
        $$(".modal-content button").filterBy(Condition.exactText("RESTART")).first().shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("RESTART button was clicked.");
    }
}
