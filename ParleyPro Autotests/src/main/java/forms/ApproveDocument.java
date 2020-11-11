package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class ApproveDocument
{
    private SelenideElement title         = $(".modal-body-title");
    private SelenideElement approveButton = $(".button.btn-common.btn.btn-primary");


    private static Logger logger = Logger.getLogger(ApproveDocument.class);

    public ApproveDocument(String documentName)
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Are you sure you want to approve document “" + documentName + "”?"));
    }

    public void clickApproveButton()
    {
        approveButton.click();

        logger.info("APPROVE button was clicked");
    }
}
