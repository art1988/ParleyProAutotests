package forms;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AboutToStartApproval
{
    private String contractName;


    public AboutToStartApproval(String contractName)
    {
        this.contractName = contractName;

        $(".modal-body-title").shouldBe(Condition.visible).shouldHave(Condition.exactText("You are about to start approval for contract \"" + contractName + "\""));
    }

    public ConfirmApprovers clickNext()
    {
        $$(".modal-footer button").filterBy(Condition.exactText("NEXT")).first().shouldBe(Condition.visible, Condition.enabled).click();

        return new ConfirmApprovers(contractName);
    }

    public void clickCancel()
    {
        $$(".modal-footer button").filterBy(Condition.exactText("CANCEL")).first().shouldBe(Condition.visible, Condition.enabled).click();
    }
}
