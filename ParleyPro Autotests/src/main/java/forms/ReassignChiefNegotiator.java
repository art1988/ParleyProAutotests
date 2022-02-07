package forms;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ReassignChiefNegotiator
{
    private static Logger logger = Logger.getLogger(ReassignChiefNegotiator.class);


    public ReassignChiefNegotiator()
    {
        $(".modal-body-title").shouldBe(Condition.visible).shouldHave(Condition.exactText("Are you sure you want to reassign the Chief Negotiator role?"));
        $(".modal-content input").shouldBe(Condition.visible);
        $$(".modal-content button").shouldHave(CollectionCondition.size(2));
    }

    public ReassignChiefNegotiator setChiefNegotiator(String cnEmail) throws InterruptedException
    {
        SelenideElement input = $(".modal-content input");

        input.sendKeys(cnEmail);
        $(".new_select__loading-indicator").waitUntil(Condition.disappear, 25_000);

        Thread.sleep(1_000);
        input.sendKeys(Keys.ARROW_DOWN);
        Thread.sleep(500);
        input.pressEnter();
        Thread.sleep(500);

        return this;
    }

    public void clickReassign()
    {
        $$(".modal-content button").filterBy(Condition.exactText("Reassign")).first().click();

        logger.info("Reassign button was clicked");
    }
}
