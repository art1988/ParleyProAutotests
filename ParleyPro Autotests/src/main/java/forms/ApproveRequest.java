package forms;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Represents popup that appears after clicking by APPROVE button of _request_
 */
public class ApproveRequest
{
    private static Logger logger = Logger.getLogger(ApproveRequest.class);


    public ApproveRequest()
    {
        $(".modal-body-title").shouldBe(Condition.visible).shouldHave(Condition.text("Are you sure"));
        $$(".modal-content .modal-footer button").shouldHave(CollectionCondition.size(2));
    }

    public void clickApprove()
    {
        $$(".modal-content .modal-footer button").findBy(Condition.exactText("APPROVE")).click();

        logger.info("APPROVE _request_ button was clicked");
    }
}
