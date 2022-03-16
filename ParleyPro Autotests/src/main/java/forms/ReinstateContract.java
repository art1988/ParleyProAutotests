package forms;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ReinstateContract
{
    private static Logger logger = Logger.getLogger(ReinstateContract.class);


    public ReinstateContract(String contractName)
    {
        $(".modal-body-title").shouldBe(Condition.visible).shouldHave(Condition.exactText("You have asked to reinstate contract “" + contractName + "”."));
        $$(".modal-content .modal-footer button").shouldHave(CollectionCondition.size(2)).shouldHave(CollectionCondition.textsInAnyOrder("CANCEL", "REINSTATE"));
    }

    public void clickReinstate()
    {
        $$(".modal-content .modal-footer button").filterBy(Condition.exactText("REINSTATE")).first().click();

        logger.info("REINSTATE button was clicked");
    }
}
