package forms.add;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class AddNewParentField
{
    private SelenideElement createButton = $(".modal-footer button[type=\"submit\"]");


    private static Logger logger = Logger.getLogger(AddNewParentField.class);

    public AddNewParentField()
    {
        $(".modal-body-title").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Add new parent field"));

        $(".modal-body-description").waitUntil(Condition.visible, 7_000)
                .shouldHave(Condition.exactText("Parent field values affect the visibility of child fields"));
    }

    public void selectParentField(String fieldName)
    {
        // Set id for input field dynamically
        Selenide.executeJavaScript("$('.Select-control input').attr('id', 'fieldTypeID');");

        $("#fieldTypeID").setValue(fieldName).sendKeys(Keys.DOWN);
        $("#fieldTypeID").pressEnter();
    }

    public void clickCreate()
    {
        createButton.click();

        logger.info("CREATE button was clicked");

        $(".modal-content").waitUntil(Condition.disappear, 7_000);
    }
}
