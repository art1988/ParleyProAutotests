package forms.editor_toolbar;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class TableProperties
{
    private SelenideElement title    = $("div[id^='cke_dialog_title']");
    private SelenideElement okButton = $("td a[title='OK']");


    private static Logger logger = Logger.getLogger(TableProperties.class);

    public TableProperties()
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Table Properties"));
    }

    public void clickOk()
    {
        okButton.click();

        logger.info("OK button was clicked");
    }
}
