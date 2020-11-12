package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class AuditTrail
{
    private SelenideElement title    = $(".modal-title");
    private SelenideElement okButton = $("button[type='submit']");


    private static Logger logger = Logger.getLogger(AuditTrail.class);

    public AuditTrail()
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Audit trail"));

        $(".modal-body .timeline-heading").waitUntil(Condition.visible, 7_000);
    }

    public void clickOk()
    {
        okButton.click();

        logger.info("OK button was clicked");
    }
}
