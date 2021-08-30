package forms;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents 'Are you sure you want to unlink contracts?' popup that appears after clicking of 'x' button for linked contracts
 */
public class UnlinkContract
{
    private static Logger logger = Logger.getLogger(UnlinkContract.class);

    public UnlinkContract()
    {
        $(".modal-title").waitUntil(Condition.visible, 15_000)
                .shouldHave(Condition.exactText("Are you sure you want to unlink contracts?"));

        $$(".modal-footer ._button").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("CANCEL", "UNLINK"));
    }

    public void clickUnlink()
    {
        $$(".modal-footer ._button").filter(Condition.exactText("UNLINK")).first().click();

        logger.info("UNLINK button was clicked...");
    }
}
