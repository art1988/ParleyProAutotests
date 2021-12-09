package forms.add;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

public class AddMembers
{
    private SelenideElement addParticipantField = $("#teamsAddParticipant");
    private SelenideElement addButton           = $("._button.scheme_blue.size_md");


    private static Logger logger = Logger.getLogger(AddMembers.class);

    public AddMembers()
    {
        $(".modal-header").waitUntil(Condition.visible, 6_000).shouldHave(Condition.exactText("Add members"));
        addParticipantField.shouldBe(Condition.visible).shouldBe(Condition.enabled);
    }

    /**
     * Click by Add participant dropdown and add new member
     * @param nameOrEmail
     */
    public void addParticipant(String nameOrEmail)
    {
        $(".Select-placeholder").waitUntil(Condition.visible, 7_000).shouldHave(Condition.exactText("Add a participant by name or email address"));
        $(".Select-placeholder").click();
        Selenide.executeJavaScript("$('#teamsAddParticipant').val('');"); // Clear previous entered value cuz clear doesn't work here
        addParticipantField.sendKeys(nameOrEmail);

        $(".new-select__indicator").waitUntil(Condition.disappear, 14_000);

        addParticipantField.sendKeys(Keys.DOWN);
        addParticipantField.pressEnter();

        try
        {
            Thread.sleep(200);
        }
        catch (InterruptedException e)
        {
            logger.error("InterruptedException", e);
        }

        $(".modal-header").click(); // Click by form's title to reset opened dropdown
    }

    public void clickAdd()
    {
        addButton.click();

        logger.info("Add button was clicked");
    }
}
