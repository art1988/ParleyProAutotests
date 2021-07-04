package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

/**
 * Modal form that appears after clicking 'REVIEW AND REPLACE' button
 */
public class Replace
{
    private SelenideElement saveButton = $("._button.scheme_blue.size_lg");

    private static Logger logger = Logger.getLogger(Replace.class);


    public Replace()
    {
        $(".modal-content").waitUntil(Condition.visible, 10_000);

        saveButton.shouldBe(Condition.enabled).shouldBe(Condition.visible);
        $(".modal-content .scrollbar-container").shouldBe(Condition.visible);
    }

    public void clickSave()
    {
        saveButton.click();

        logger.info("SAVE button was clicked...");

        $(".modal-content").waitUntil(Condition.disappear, 30_000);
    }
}
