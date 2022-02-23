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

    /**
     * Match by exact contractName
     * @param contractName
     */
    public DeleteContract(String contractName)
    {
        title.shouldBe(Condition.visible).shouldHave(Condition.exactText("Are you sure you want to delete contract “" + contractName + "”?"));
        $(".modal-body-description").shouldBe(Condition.visible).shouldHave(Condition.exactText("The contract and contract documents will be permanently removed."));
    }

    /**
     * In case of enabled "key": "CONTRACT_NAME_TEMPLATE", contract name may be dynamic => we can't know in advance contract title => do not match by contractName in popup
     * Just match to "Are you sure you want to delete contract" text
     * @param doNotMatchByContractName just flag to indicate
     */
    public DeleteContract(boolean doNotMatchByContractName)
    {
        title.shouldBe(Condition.visible).shouldHave(Condition.text("Are you sure you want to delete contract"));
        $(".modal-body-description").shouldBe(Condition.visible).shouldHave(Condition.exactText("The contract and contract documents will be permanently removed."));
    }

    public void clickDelete()
    {
        Selenide.executeJavaScript("$('.modal-footer button:contains(\"DELETE\")').click()");

        logger.info("Delete button was clicked");
    }
}
