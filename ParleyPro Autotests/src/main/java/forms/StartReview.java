package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class StartReview
{
    private SelenideElement title = $(".modal-body-title");

    private static Logger logger = Logger.getLogger(StartReview.class);


    public StartReview(String contractName)
    {
        title.waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("You are about to start review for contract \"" + contractName + "\"."));
    }

    public void clickStart()
    {
        Selenide.executeJavaScript("$('.modal-footer button:contains(\"START\")').click()");

        logger.info("Start button was clicked");

        $(".modal-content").waitUntil(Condition.disappear, 20_000);
    }
}
