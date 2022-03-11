package forms;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import org.apache.log4j.Logger;
import org.openqa.selenium.interactions.Actions;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CancelContract
{
    private static Logger logger = Logger.getLogger(CancelContract.class);


    public CancelContract()
    {
        $(".modal-body-title").shouldBe(Condition.visible).shouldHave(Condition.text("You have asked to cancel contract"));

        $$(".modal-content button").shouldHave(CollectionCondition.size(2));
    }

    public CancelContract checkLeaveCommentCheckbox()
    {
        $("input[type='checkbox']").shouldBe(Condition.visible, Condition.enabled);

        Actions actionProvider = new Actions(WebDriverRunner.getWebDriver());
        actionProvider.click($("input[type='checkbox']").toWebElement()).build().perform();

        return this;
    }

    public CancelContract setMessage(String commentMessage)
    {
        $("textarea").shouldBe(Condition.visible, Condition.enabled).sendKeys(commentMessage);

        return this;
    }

    public void clickCancelContract()
    {
        $$(".modal-content button").filterBy(Condition.exactText("Cancel contract")).first().shouldBe(Condition.visible, Condition.enabled).click();

        logger.info("CANCEL CONTRACT button was clicked.");
    }
}
