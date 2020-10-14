package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;

import static com.codeborne.selenide.Selenide.$;

public class SignContract
{
    private SelenideElement title = $(".modal-body-title");



    private static Logger logger = Logger.getLogger(SignContract.class);

    public SignContract(String contractName)
    {
        title.waitUntil(Condition.visible, 5_000).shouldHave(Condition.exactText("You have requested to manually sign contract “" + contractName + "”."));
    }

    /**
     * Clicking by Start button will download document
     * @throws FileNotFoundException
     */
    public void clickStart() throws FileNotFoundException
    {
        $("#manual-sign-start-button").download();

        logger.info("Start button was clicked");
    }
}
