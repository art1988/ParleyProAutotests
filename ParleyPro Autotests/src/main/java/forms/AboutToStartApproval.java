package forms;

import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Form that appears after clicking by APPROVAL lifecycle icon
 */
public class AboutToStartApproval
{
    private String contractName;

    private static Logger logger = Logger.getLogger(AboutToStartApproval.class);


    public AboutToStartApproval(String contractName)
    {
        this.contractName = contractName;

        $(".modal-body-title").shouldBe(Condition.visible).shouldHave(Condition.exactText("You are about to start approval for contract \"" + contractName + "\""));
    }

    public ConfirmApprovers clickNext()
    {
        $$(".modal-footer button").filterBy(Condition.exactText("NEXT")).first().shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("NEXT button was clicked");

        return new ConfirmApprovers(contractName);
    }

    public void clickCancel()
    {
        $$(".modal-footer button").filterBy(Condition.exactText("CANCEL")).first().shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("CANCEL button was clicked");
    }
}
