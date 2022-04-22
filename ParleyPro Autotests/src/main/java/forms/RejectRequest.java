package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class RejectRequest
{
    private static Logger logger = Logger.getLogger(RejectRequest.class);


    public RejectRequest()
    {
        $(".modal-body-title").shouldBe(Condition.visible).shouldHave(Condition.text("Are you sure you want to reject contract?"));
    }

    public RejectRequest setMessage(String comment)
    {
        $(".modal-content .input textarea").sendKeys(comment);
        Selenide.sleep(500);

        return this;
    }

    public void clickReject()
    {
        $$(".modal-content button").filterBy(Condition.exactText("REJECT")).first().shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("REJECT button was clicked...");
    }
}
