package forms.add;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class AddRelatedField
{
    private SelenideElement makeRelatedButton = $("._button.scheme_blue");

    private static Logger logger = Logger.getLogger(AddRelatedField.class);

    public AddRelatedField()
    {
        $(".modal-body-title").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Related field"));

        $(".modal-body-description").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Select field value and relevant field you wish to relate it to"));
    }

    public void selectValueForField(String fieldName, String value)
    {
        // Set id dynamically for input
        Selenide.executeJavaScript("$('.input__label-title:contains(\"" + fieldName + "\")').parent().parent().find(\"input\").attr('id', 'inp')");

        $("#inp").sendKeys(value);
        $("#inp").sendKeys(Keys.DOWN);
        $("#inp").pressEnter();
    }

    public void selectFields(String field)
    {
        $("input[label='Fields']").click(); // click by input to expand dropdown

        Selenide.executeJavaScript("$('.multi-select__option span:contains(\"" + field + "\")').click()"); // select field
    }

    public void clickMakeRelated()
    {
        makeRelatedButton.click();

        logger.info("MAKE RELATED button was clicked");

        $(".modal-content").waitUntil(Condition.disappear, 7_000);
    }
}