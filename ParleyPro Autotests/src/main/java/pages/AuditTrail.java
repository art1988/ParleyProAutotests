package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import model.AuditTrailEvent;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AuditTrail
{
    private SelenideElement title    = $(".modal-title");
    private SelenideElement okButton = $(".modal-content button[type='submit']");


    private static Logger logger = Logger.getLogger(AuditTrail.class);

    public AuditTrail()
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Audit trail"));

        $(".timeline").waitUntil(Condition.visible, 7_000);
    }

    public List<AuditTrailEvent> collectAllEvents()
    {
        List<AuditTrailEvent> allEvents = new ArrayList<>();

        $$(".timeline-heading h4").stream().forEach(eventTitle -> allEvents.add(new AuditTrailEvent(eventTitle.getText())));

        return allEvents;
    }

    public void clickOk()
    {
        okButton.click();

        logger.info("OK button was clicked");

        $(".modal-content").waitUntil(Condition.disappear, 10_000);
    }
}
