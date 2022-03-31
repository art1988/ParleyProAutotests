package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Form that appears after clicking 'Approve' button in document header
 */
public class ApproveDocument
{
    private SelenideElement title         = $(".modal-body-title");
    private SelenideElement approveButton = $(".button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(ApproveDocument.class);

    public ApproveDocument(String documentName)
    {
        title.shouldBe(Condition.visible).shouldHave(Condition.exactText("Document(s) approval confirmation"));
    }

    public void clickApproveButton()
    {
        approveButton.shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("APPROVE button was clicked");
    }
}
