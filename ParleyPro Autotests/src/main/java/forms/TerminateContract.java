package forms;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TerminateContract
{
    private static Logger logger = Logger.getLogger(TerminateContract.class);


    public TerminateContract(String contractName)
    {
        $(".modal-body-title").shouldBe(Condition.visible).shouldHave(Condition.exactText("You have asked to terminate contract “" + contractName + "”."));
        $$(".modal-content .modal-footer button").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("CANCEL", "TERMINATE"));
    }

    public void clickTerminate()
    {
        $$(".modal-content .modal-footer button").filterBy(Condition.exactText("TERMINATE")).first().click();

        logger.info("TERMINATE button was clicked");
    }
}
