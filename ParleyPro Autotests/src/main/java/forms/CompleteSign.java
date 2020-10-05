package forms;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import pages.ContractInfo;

import static com.codeborne.selenide.Selenide.$;

public class CompleteSign
{
    private SelenideElement title = $(".modal-body-title");



    private static Logger logger = Logger.getLogger(CompleteSign.class);

    public CompleteSign(String contractName)
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Complete sign process for contract “" + contractName + "”."));
    }

    public ContractInfo clickComplete()
    {
        Selenide.executeJavaScript("$(\".modal-footer button:contains('Complete')\").click()");

        logger.info("Complete button was clicked");

        return new ContractInfo();
    }
}
