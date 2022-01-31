package forms;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CancelContract
{
    private static Logger logger = Logger.getLogger(CancelContract.class);


    public CancelContract()
    {
        $(".modal-body-title").shouldBe(Condition.visible).shouldHave(Condition.text("You have asked to cancel contract"));

        $$(".modal-content button").shouldHave(CollectionCondition.size(2));
    }

    public void clickCancelContract()
    {
        $$(".modal-content button").filterBy(Condition.exactText("Cancel contract")).first().shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("CANCEL CONTRACT button was clicked.");
    }
}
