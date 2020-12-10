package forms.editor_toolbar;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;

import static com.codeborne.selenide.Selenide.$;

public class Field
{
    private SelenideElement title                 = $("div[id^='cke_dialog_title']");
    private SelenideElement fieldNameInput        = $(".cke_dialog_contents input[type='text']");
    private SelenideElement requiredFieldCheckbox = $(".cke_dialog_contents input[type='checkbox']");
    private SelenideElement okButton = $("td a[title='OK']");


    private static Logger logger = Logger.getLogger(Field.class);

    public Field()
    {
        title.waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Field"));
    }

    public void setFieldName(String name)
    {
        fieldNameInput.setValue(name);
    }

    public void markRequiredFieldCheckbox()
    {
        requiredFieldCheckbox.click();

        logger.info("Required field was marked");
    }

    public void clickOk()
    {
        okButton.click();

        logger.info("OK button was clicked");
    }
}
