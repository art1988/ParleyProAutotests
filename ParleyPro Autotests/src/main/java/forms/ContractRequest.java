package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class ContractRequest
{
    private SelenideElement submitRequestButton = $("._button.scheme_blue.size_lg");
    private SelenideElement cancelButton        = $("._button.scheme_gray.size_lg");

    private static Logger logger = Logger.getLogger(ContractRequest.class);

    public ContractRequest()
    {
        $(".spinner").waitUntil(Condition.disappear, 15_000);
        $(".modal-title").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Contract Request"));
        $(".upload-field__head").waitUntil(Condition.visible, 10_000).shouldHave(Condition.exactText("Add documents"));

        submitRequestButton.shouldBe(Condition.visible).shouldBe(Condition.enabled);
        cancelButton.shouldBe(Condition.visible).shouldBe(Condition.enabled);
    }

    public void clickCancel()
    {
        cancelButton.click();

        logger.info("CANCEL button was clicked.");
    }
}
