package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

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

    /**
     * Click checkbox for the given Document to mark to approve
     */
    public ApproveDocument markDocument(String docName)
    {
        //$$(".modal-body .checkbox__label").filterBy(Condition.text(docName)).first().parent().find("input").click();
        $(withText(docName)).click();

        logger.info("The following document was checked: " + docName);

        return this;
    }

    public void clickApproveButton()
    {
        approveButton.shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("APPROVE button was clicked");
    }
}
